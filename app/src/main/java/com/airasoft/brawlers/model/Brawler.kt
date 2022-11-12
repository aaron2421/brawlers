package com.airasoft.brawlers.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "brawlers_table")
data class Brawler(
    @ColumnInfo(name = "brawler_name")
    var brawlerName: String = "",
    @ColumnInfo(name = "brawler_class")
    var brawlerClass: String = "",
    @ColumnInfo(name = "brawler_type")
    var brawlerType: String = "",
    @ColumnInfo(name = "brawler_health")
    var brawlerHealth: String = "",
    @ColumnInfo(name = "brawler_speed")
    var brawlerSpeed: String = "",
    @ColumnInfo(name = "brawler_atack")
    var brawlerAtack: String = "",
    @ColumnInfo(name = "brawler_damage")
    var brawlerDamage: String = "",
    @ColumnInfo(name = "brawler_range")
    var brawlerRange: String = "",
    @ColumnInfo(name = "brawler_super")
    var brawlerSuper: String = "",
    @ColumnInfo(name = "brawler_image")
    var brawlerImage: String = "",
    @PrimaryKey(autoGenerate = true)
    var brawlerId: Int = 0
    /*@SerializedName("_id")
    val _id: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("clase")
    val clase: String,
    @SerializedName("tipo")
    val tipo: String,
    @SerializedName("salud")
    val salud: Int,
    @SerializedName("velocidad")
    val velocidad: String,
    @SerializedName("ataque")
    val ataque: String,
    @SerializedName("damage")
    val damage: Int,
    @SerializedName("alcance")
    val alcance: String,
    @SerializedName("super")
    val bSuper: String,
    @SerializedName("habilidades") val habilidades: List<Habilidades>,
    @SerializedName("gadgets") val gadgets: List<Gadgets>,
    @SerializedName("skins") val skins: List<Skins>,
    @SerializedName("imagen")
    val imagen: String*/
) : Parcelable