package com.kroffle.knitting.domain.product.exception

import com.kroffle.knitting.domain.DomainException

class UnableToRegister : DomainException(message = "unable to register because of domain fields")
