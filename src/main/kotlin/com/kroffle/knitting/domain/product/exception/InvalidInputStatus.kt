package com.kroffle.knitting.domain.product.exception

import com.kroffle.knitting.domain.DomainException

class InvalidInputStatus : DomainException(message = "registered product must have content")
