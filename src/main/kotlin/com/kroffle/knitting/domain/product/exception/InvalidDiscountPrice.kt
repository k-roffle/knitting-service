package com.kroffle.knitting.domain.product.exception

import com.kroffle.knitting.domain.exception.DomainException

class InvalidDiscountPrice : DomainException("discount price must be between 0 and full price")
