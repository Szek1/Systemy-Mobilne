package com.example.wifisignalstrength.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accessPoint_database")
data class AccesPoint (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "SSID")
    val ssid: String,
    @ColumnInfo(name = "Częstotliwość")
    val frequency: String,
    @ColumnInfo(name = "Predkosc_Lacza")
    val speedLink: String,
    @ColumnInfo(name = "RSSI")
    val rssi: String,
    @ColumnInfo(name = "Odległość")
    val distance: String
        )