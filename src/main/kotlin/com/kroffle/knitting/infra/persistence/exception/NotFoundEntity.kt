package com.kroffle.knitting.infra.persistence.exception

class NotFoundEntity(clazz: Class<*>) : Exception("Cannot found ${clazz.name}")
