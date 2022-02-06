package com.kroffle.knitting.usecase.repository

import com.kroffle.knitting.usecase.draftproduct.DraftProductService
import com.kroffle.knitting.usecase.product.ProductService
import com.kroffle.knitting.usecase.summary.ProductSummaryService

interface ProductRepository :
    ProductService.ProductRepository,
    ProductSummaryService.ProductRepository,
    DraftProductService.ProductRepository
