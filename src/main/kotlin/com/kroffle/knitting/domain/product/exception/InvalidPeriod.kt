package com.kroffle.knitting.domain.product.exception

import com.kroffle.knitting.domain.exception.DomainException

class InvalidPeriod : DomainException("end date must be greater than start date")
