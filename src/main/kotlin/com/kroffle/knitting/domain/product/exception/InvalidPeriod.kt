package com.kroffle.knitting.domain.product.exception

import com.kroffle.knitting.domain.DomainException

class InvalidPeriod : DomainException(message = "end date must be greater than start date")
