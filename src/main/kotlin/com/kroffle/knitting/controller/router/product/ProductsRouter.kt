package com.kroffle.knitting.controller.router.product

import com.kroffle.knitting.controller.handler.draftproduct.DraftProductHandler
import com.kroffle.knitting.controller.handler.product.ProductHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class ProductsRouter(
    private val productHandler: ProductHandler,
    private val draftProductHandler: DraftProductHandler,
) {

    @Bean
    @Deprecated("Use /products APIs")
    fun deprecatedProductRouterFunction() = nest(
        path(DEPRECATED_ROOT_PATH),
        router {
            listOf(
                POST(DRAFT_PRODUCT_PACKAGE_PATH, productHandler::editProductPackage),
                POST(DRAFT_PRODUCT_CONTENT_PATH, productHandler::editProductContent),
                POST(productHandler::registerProduct),
                GET(GET_MY_PRODUCT_PATH, productHandler::getMyProduct),
                GET(GET_MY_PRODUCTS_PATH, productHandler::getMyProducts),
            )
        }
    )

    @Bean
    fun productRouterFunction() = nest(
        path(ROOT_PATH),
        router {
            listOf(
                POST(CREATE_PRODUCT_PATH, productHandler::createProduct),
                PUT(UPDATE_PRODUCT_PATH, productHandler::updateProduct),
                GET(GET_MY_PRODUCT_PATH, productHandler::getMyProduct),
                GET(GET_MY_PRODUCTS_PATH, productHandler::getMyProducts),
            )
        }
    )

    @Bean
    fun draftProductRouterFunction() = nest(
        path(ROOT_PATH),
        router {
            listOf(
                GET(GET_MY_DRAFT_PRODUCTS_PATH, draftProductHandler::getMyDraftProducts),
                GET(GET_MY_DRAFT_PRODUCT_PATH, draftProductHandler::getMyDraftProduct),
                GET(GET_MY_DRAFT_PRODUCT_TO_UPDATE_PATH, draftProductHandler::getMyDraftProductToUpdate),
                POST(SAVE_DRAFT_PATH, draftProductHandler::saveDraft),
                DELETE(DELETE_MY_DRAFT_PRODUCT_PATH, draftProductHandler::deleteMyDraftProduct),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/products"

        // product path
        private const val CREATE_PRODUCT_PATH = ""
        private const val UPDATE_PRODUCT_PATH = "/{productId}"
        private const val GET_MY_PRODUCT_PATH = "/mine/{productId}"
        private const val GET_MY_PRODUCTS_PATH = "/mine"

        // draft product path
        private const val GET_MY_DRAFT_PRODUCTS_PATH = "/draft/mine"
        private const val GET_MY_DRAFT_PRODUCT_PATH = "/draft/mine/{draftProductId}"
        private const val GET_MY_DRAFT_PRODUCT_TO_UPDATE_PATH = "/{productId}/draft/mine"
        private const val SAVE_DRAFT_PATH = "/draft"
        private const val DELETE_MY_DRAFT_PRODUCT_PATH = "/draft/mine/{draftProductId}"

        val PUBLIC_PATHS = listOf<String>()

        @Deprecated(message = "use ROOT_PATH")
        private const val DEPRECATED_ROOT_PATH = "/product"
        @Deprecated(message = "use SAVE_DRAFT_PATH")
        private const val DRAFT_PRODUCT_PACKAGE_PATH = "/package"
        @Deprecated(message = "use SAVE_DRAFT_PATH")
        private const val DRAFT_PRODUCT_CONTENT_PATH = "/content"
    }
}
