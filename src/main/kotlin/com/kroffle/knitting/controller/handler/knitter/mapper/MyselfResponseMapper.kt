package com.kroffle.knitting.controller.handler.knitter.mapper

import com.kroffle.knitting.controller.handler.knitter.dto.MyProfile
import com.kroffle.knitting.controller.handler.knitter.dto.SalesSummary
import com.kroffle.knitting.domain.knitter.entity.Knitter

object MyselfResponseMapper {
    fun toMyProfileResponse(knitter: Knitter): MyProfile.Response =
        with(knitter) {
            MyProfile.Response(
                email = email,
                name = name,
                profileImageUrl = profileImageUrl,
            )
        }

    fun toSalesSummaryResponse(numberOfProductsOnSales: Long): SalesSummary.Response =
        SalesSummary.Response(
            numberOfProductsOnSales = numberOfProductsOnSales,
            numberOfProductsSold = 0,
        )
}
