package com.kroffle.knitting.usecase.exception

class NotFoundEntity(clazz: Class<*>) : Exception("Cannot found ${clazz.name}")
