package com.kroffle.knitting.domain.exception

class InvalidPeriod : DomainException("end date must be greater than start date")
