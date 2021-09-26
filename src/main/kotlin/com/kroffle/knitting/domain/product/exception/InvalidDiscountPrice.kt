package com.kroffle.knitting.domain.product.exception

import com.kroffle.knitting.domain.DomainException

class InvalidDiscountPrice : DomainException(message = "discount price must be between 0 and full price")
