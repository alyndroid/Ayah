package com.hamalawy.ayah

data class Surah(
    val id: Int,
    val name: String,
    val name_en: String,
    val name_translation: String,
    val words: Int,
    val letters: Int,
    val type: String,
    val type_en: String,
    val ar: String,
    val en: String,
    val array: List<Verses>
)