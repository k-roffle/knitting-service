package com.kroffle.knitting.controller.handler.design.dto

data class MyDesign(
    val name: String,
    val yarn: String,
    val tags: List<String>,
)