package com.toggl.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "time_entries_tags",
    primaryKeys = ["timeEntryId", "tagId"],
    foreignKeys = [
        ForeignKey(entity = DatabaseTimeEntry::class, parentColumns = ["id"], childColumns = ["timeEntryId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = DatabaseTag::class, parentColumns = ["id"], childColumns = ["tagId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [ Index("timeEntryId"), Index("tagId") ]
)
data class DatabaseTimeEntryTag(
    val timeEntryId: Long,
    val tagId: Long
)
