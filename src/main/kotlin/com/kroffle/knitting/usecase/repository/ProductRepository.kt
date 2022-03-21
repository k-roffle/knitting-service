package com.kroffle.knitting.usecase.repository

import com.kroffle.knitting.usecase.draftproduct.DraftProductService
import com.kroffle.knitting.usecase.product.ProductService
import com.kroffle.knitting.usecase.summary.ProductSummaryService
import com.kroffle.knitting.usecase.summary.ProfileSummaryService

interface ProductRepository :
    ProductService.ProductRepository,
    ProductSummaryService.ProductRepository,
    DraftProductService.ProductRepository,
    ProfileSummaryService.ProductRepository
