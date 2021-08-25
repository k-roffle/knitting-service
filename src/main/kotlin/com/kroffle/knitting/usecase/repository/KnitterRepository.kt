package com.kroffle.knitting.usecase.repository

import com.kroffle.knitting.usecase.auth.AuthService
import com.kroffle.knitting.usecase.knitter.KnitterService

interface KnitterRepository :
    AuthService.KnitterRepository, KnitterService.KnitterRepository
