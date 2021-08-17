package com.kroffle.knitting.domain.design.enum

enum class LevelType(val key: String, val label: String, val description: String) {
    // TODO: 레벨 정책 잡아야 함
    EASY(key = "EASY", label = "입문자", description = "이제 막 뜨개질을 시작한 사람!"),
    NORMAL(key = "NORMAL", label = "중급", description = "겉뜨기, 안뜨기, 코잡기는 쉽게 가능한 사람!"),
    HARD(key = "HARD", label = "고급", description = "니트, 양말, 모자 하나 정도는 떠본 사람!");

    companion object {
        fun getFromKey(key: String): LevelType = when (key) {
            "EASY" -> EASY
            "NORMAL" -> NORMAL
            "HARD" -> HARD
            else -> throw NotImplementedError()
        }
    }
}
