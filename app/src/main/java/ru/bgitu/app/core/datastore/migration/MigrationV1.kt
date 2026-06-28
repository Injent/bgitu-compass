package ru.bgitu.app.core.datastore.migration

import androidx.datastore.core.DataMigration
import ru.bgitu.app.core.datastore.model.CachedGroup
import ru.bgitu.app.core.datastore.model.UserPrefs
import ru.bgitu.app.core.model.UiTheme
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.time.Instant

class MigrationV1(private val oldProtoFile: File) : DataMigration<UserPrefs> {

    override suspend fun cleanUp() {
        oldProtoFile.delete()
    }

    override suspend fun migrate(currentData: UserPrefs): UserPrefs {
        return ProtobufSettingsParser.parseProtobufFile(oldProtoFile)
    }

    override suspend fun shouldMigrate(currentData: UserPrefs): Boolean {
        return oldProtoFile.exists()
    }
}

private object ProtobufSettingsParser {

    fun parseProtobufFile(protoFile: File): UserPrefs {
        return try {
            val bytes = protoFile.readBytes()
            val reader = ProtoReader(bytes)
            parseSettings(reader)
        } catch (e: Exception) {
            e.printStackTrace()
            UserPrefs()
        }
    }

    private fun parseSettings(reader: ProtoReader): UserPrefs {
        var credentials = CredentialsPb()
        var prefs = UserPrefsPb()
        var userDataVersion = 0

        while (!reader.isAtEnd()) {
            val tag = reader.readTag()
            when (tag) {
                (16 shl 3) or 2 -> {
                    credentials = parseCredentials(ProtoReader(reader.readBytes()))
                }
                (17 shl 3) or 2 -> {
                    prefs = parseUserPrefs(ProtoReader(reader.readBytes()))
                }
                (18 shl 3) or 2 -> {
                    userDataVersion = parseUserDataVersion(ProtoReader(reader.readBytes()))
                }
                else -> reader.skipLastTag()
            }
        }

        val userGroups = mutableListOf<CachedGroup>()
        if (credentials.groupId != null && credentials.groupName != null) {
            userGroups += CachedGroup(id = credentials.groupId, name = credentials.groupName)
        }
        prefs.savedGroups
            .sortedBy { it.slotIndex }
            .forEach { group ->
                userGroups += CachedGroup(
                    id = group.id,
                    name = group.name
                )
            }

        return UserPrefs(
            uiTheme = when (prefs.theme) {
                "LIGHT" -> UiTheme.LIGHT
                "DARK" -> UiTheme.DARK
                else -> UiTheme.AUTO
            },
            userGroups = userGroups,
            liveUpdateNotification = prefs.showPinnedSchedule,
            liveUpdateGroupId = userGroups.firstOrNull()?.id,
            lastResetTimestamp = if (userDataVersion == 2) {
                // Время когда userDataVersion был равен 2
                Instant.parse("2026-01-17T18:33:49Z")
            } else null
        )
    }

    private fun parseUserDataVersion(reader: ProtoReader): Int {
        var userDataVersion = 0

        while (!reader.isAtEnd()) {
            val tag = reader.readTag()
            when (tag) {
                (6 shl 3) or 0 -> userDataVersion = reader.readVarint32()
                else -> reader.skipLastTag()
            }
        }
        return userDataVersion
    }

    private fun parseCredentials(reader: ProtoReader): CredentialsPb {
        var groupId: Int? = null
        var groupName: String? = null

        while (!reader.isAtEnd()) {
            val tag = reader.readTag()
            when (tag) {
                (7 shl 3) or 0 -> groupId = reader.readVarint32().takeIf { it != 0 }
                (8 shl 3) or 2 -> groupName = reader.readString().takeIf { it.isNotEmpty() }
                else -> reader.skipLastTag()
            }
        }
        return CredentialsPb(groupId, groupName)
    }

    private fun parseUserPrefs(reader: ProtoReader): UserPrefsPb {
        var theme = ""
        var showPinned = false
        val savedGroups = mutableListOf<GroupSlotPb?>()

        while (!reader.isAtEnd()) {
            val tag = reader.readTag()
            when (tag) {
                (2 shl 3) or 2 -> theme = reader.readString()
                (3 shl 3) or 0 -> showPinned = reader.readBool()
                (5 shl 3) or 2 -> savedGroups.add(parseGroupSlot(ProtoReader(reader.readBytes())))
                else -> reader.skipLastTag()
            }
        }
        return UserPrefsPb(
            theme = theme,
            showPinnedSchedule = showPinned,
            savedGroups = savedGroups.filterNotNull()
        )
    }

    private fun parseGroupSlot(reader: ProtoReader): GroupSlotPb? {
        var id: Int? = null
        var name: String? = null
        var slotIndex = 0

        while (!reader.isAtEnd()) {
            val tag = reader.readTag()
            when (tag) {
                (1 shl 3) or 2 -> {
                    val wrapperReader = ProtoReader(reader.readBytes())
                    while (!wrapperReader.isAtEnd()) {
                        if (wrapperReader.readTag() == ((1 shl 3) or 0)) {
                            id = wrapperReader.readVarint32()
                        } else wrapperReader.skipLastTag()
                    }
                }
                (2 shl 3) or 2 -> {
                    val wrapperReader = ProtoReader(reader.readBytes())
                    while (!wrapperReader.isAtEnd()) {
                        if (wrapperReader.readTag() == ((1 shl 3) or 2)) {
                            name = wrapperReader.readString()
                        } else wrapperReader.skipLastTag()
                    }
                }
                (3 shl 3) or 0 -> slotIndex = reader.readVarint32()
                else -> reader.skipLastTag()
            }
        }
        return GroupSlotPb(
            id = id ?: return null,
            name = name ?: return null,
            slotIndex = slotIndex
        )
    }

    private class ProtoReader(bytes: ByteArray) {
        private val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        private var lastTag: Int = 0

        fun isAtEnd(): Boolean = !buffer.hasRemaining()

        fun readTag(): Int {
            if (isAtEnd()) { lastTag = 0; return 0 }
            lastTag = readVarint32()
            return lastTag
        }

        fun readVarint32(): Int {
            var result = 0
            var shift = 0
            // Читаем, пока есть байты
            while (buffer.hasRemaining()) {
                val b = buffer.get().toInt()
                result = result or ((b and 0x7F) shl shift)
                if ((b and 0x80) == 0) return result
                shift += 7
            }
            return result // Возвращаем что успели считать, если файл оборвался
        }

        // Безопасное чтение varint для пропуска
        fun readRawVarint64() {
            var shift = 0
            while (shift < 64 && buffer.hasRemaining()) {
                val b = buffer.get().toInt()
                if ((b and 0x80) == 0) return
                shift += 7
            }
        }

        fun readBool(): Boolean = readVarint32() != 0

        fun readString(): String {
            val len = readVarint32()
            if (len <= 0) return ""
            // Защита от переполнения
            val realLen = minOf(len, buffer.remaining())
            val bytes = ByteArray(realLen)
            buffer.get(bytes)
            return String(bytes, Charsets.UTF_8)
        }

        fun readBytes(): ByteArray {
            val len = readVarint32()
            if (len <= 0) return ByteArray(0)
            // Защита от переполнения
            val realLen = minOf(len, buffer.remaining())
            val bytes = ByteArray(realLen)
            buffer.get(bytes)
            return bytes
        }

        // ИСПРАВЛЕННЫЙ МЕТОД
        fun skipLastTag() {
            if (lastTag == 0) return
            when (lastTag and 0x7) {
                0 -> readRawVarint64() // Varint
                1 -> skipSafe(8)       // 64-bit
                2 -> skipSafe(readVarint32()) // Length delimited
                5 -> skipSafe(4)       // 32-bit
            }
        }

        // Вспомогательный метод для безопасного пропуска
        private fun skipSafe(count: Int) {
            if (count <= 0) return
            if (buffer.remaining() < count) {
                // Если просят пропустить больше, чем осталось -> идем в конец
                buffer.position(buffer.limit())
            } else {
                buffer.position(buffer.position() + count)
            }
        }
    }
}

private class CredentialsPb(
    val groupId: Int? = null,
    val groupName: String? = null
)

private class UserPrefsPb(
    val theme: String? = null,
    val showPinnedSchedule: Boolean = false,
    val savedGroups: List<GroupSlotPb> = emptyList()
)

private class GroupSlotPb(
    val id: Int,
    val name: String,
    val slotIndex: Int
)