package com.example.oops.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "subtitle",
        foreignKeys = [ForeignKey(entity = Video::class,
                parentColumns = ["id"],
                childColumns = ["videoId"],
                onDelete = ForeignKey.CASCADE)])

class Subtitle(var videoId: Int = 0,
               var title: String? = null,
               var subtitleUrl: String? = null) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}