package com.kroffle.knitting.domain.design.enum

enum class PatternType(val code: Int, val tag: String) {
    Text(1, "서술형도안"),
    Image(2, "이미지도안"),
    Video(3, "영상도안"),
}
