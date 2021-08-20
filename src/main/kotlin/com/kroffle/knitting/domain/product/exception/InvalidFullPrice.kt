package com.kroffle.knitting.domain.product.exception

import com.kroffle.knitting.domain.exception.DomainException

class InvalidFullPrice : DomainException("full price must be greater than 0")
