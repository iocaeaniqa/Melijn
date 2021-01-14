package me.melijn.melijnbot.database.role

import me.melijn.melijnbot.database.CacheDBDao
import me.melijn.melijnbot.database.DriverManager
import me.melijn.melijnbot.internals.utils.splitIETEL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SelfRoleGroupDao(driverManager: DriverManager) : CacheDBDao(driverManager) {

    override val table: String = "selfRoleGroups"
    override val tableStructure: String =
        "guildId bigint, groupName varchar(64), messageIds varchar(1024), channelId bigint, isEnabled boolean, pattern varchar(256), isSelfRoleable boolean"
    override val primaryKey: String = "guildId, groupName"

    override val cacheName: String = "selfrole:group"

    init {
        driverManager.registerTable(table, tableStructure, primaryKey)
    }

    suspend fun get(guildId: Long): List<SelfRoleGroup> = suspendCoroutine {
        val list = mutableListOf<SelfRoleGroup>()
        driverManager.executeQuery("SELECT * FROM $table WHERE guildId = ?", { rs ->
            while (rs.next()) {
                val pattern = rs.getString("pattern")
                list.add(
                    SelfRoleGroup(
                        rs.getString("groupName"),
                        rs.getString("messageIds").splitIETEL("%SPLIT%").map { it.toLong() },
                        rs.getLong("channelId"),
                        rs.getBoolean("isEnabled"),
                        if (pattern.isBlank()) null else pattern,
                        rs.getBoolean("isSelfRoleable")
                    )
                )
            }
            it.resume(list)
        }, guildId)
    }


    fun set(
        guildId: Long,
        groupName: String,
        messageIds: String,
        channelId: Long,
        isEnabled: Boolean,
        pattern: String,
        isSelfRoleable: Boolean
    ) {
        driverManager.executeUpdate(
            "INSERT INTO $table (guildId, groupName, messageIds, channelId, isEnabled, pattern, isSelfRoleable) VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT ($primaryKey) DO " +
                "UPDATE SET messageIds = ?, channelId = ?, isEnabled = ?, pattern = ?, isSelfRoleable = ?",
            guildId,
            groupName,
            messageIds,
            channelId,
            isEnabled,
            pattern,
            isSelfRoleable,
            messageIds,
            channelId,
            isEnabled,
            pattern,
            isSelfRoleable
        )
    }

    fun remove(guildId: Long, groupName: String) {
        driverManager.executeUpdate(
            "DELETE FROM $table WHERE guildId = ? AND groupName = ?",
            guildId, groupName
        )
    }
}

data class SelfRoleGroup(
    val groupName: String,
    var messageIds: List<Long>,
    var channelId: Long,
    var isEnabled: Boolean,
    var pattern: String?,
    var isSelfRoleable: Boolean
)