package com.kroffle.knitting.domain.product.exception

import com.kroffle.knitting.domain.DomainException

class InvalidFullPrice : DomainException(message = "full price must be greater than 0")
