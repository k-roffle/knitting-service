package com.kroffle.knitting.usecase.repository

import com.kroffle.knitting.usecase.draftproduct.DraftProductService
import com.kroffle.knitting.usecase.product.ProductService

interface DraftProductRepository : DraftProductService.DraftProductRepository, ProductService.DraftProductRepository
