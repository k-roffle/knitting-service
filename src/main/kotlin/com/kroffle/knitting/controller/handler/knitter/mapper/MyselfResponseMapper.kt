package com.kroffle.knitting.controller.handler.knitter.mapper

import com.kroffle.knitting.controller.handler.knitter.dto.MyProfile
import com.kroffle.knitting.controller.handler.knitter.dto.MyProfileSummary
import com.kroffle.knitting.controller.handler.knitter.dto.SalesSummary
import com.kroffle.knitting.domain.knitter.entity.Knitter
import com.kroffle.knitting.usecase.summary.dto.ProfileSummary

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

    fun toMyProfileSummaryResponse(summary: ProfileSummary): MyProfileSummary.Response =
        with(summary) {
            MyProfileSummary.Response(
                myDesignsCount = myDesignsCount,
                myProductsCount = myProductsCount,
                purchasedProductsCount = purchasedProductsCount,
            )
        }
}
