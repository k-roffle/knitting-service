package com.kroffle.knitting.domain.product.exception

import com.kroffle.knitting.domain.exception.DomainException

class InvalidInputStatus : DomainException("registered product must have content")
