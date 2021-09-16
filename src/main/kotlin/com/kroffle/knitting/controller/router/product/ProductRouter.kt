package com.kroffle.knitting.controller.router.product

import com.kroffle.knitting.controller.handler.product.ProductHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class ProductRouter(private val handler: ProductHandler) {

    @Bean
    fun productRouterFunction() = nest(
        path(ROOT_PATH),
        router {
            listOf(
                POST(DRAFT_PRODUCT_PACKAGE_PATH, handler::draftProductPackage),
                POST(DRAFT_PRODUCT_CONTENT_PATH, handler::draftProductContent),
                POST(handler::registerProduct),
                GET(GET_MY_PRODUCT_PATH, handler::getMyProduct),
                GET(GET_MY_PRODUCTS_PATH, handler::getMyProducts),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/product"
        private const val DRAFT_PRODUCT_PACKAGE_PATH = "/package"
        private const val DRAFT_PRODUCT_CONTENT_PATH = "/content"
        private const val GET_MY_PRODUCT_PATH = "/mine/{id}"
        private const val GET_MY_PRODUCTS_PATH = "/mine"
        val PUBLIC_PATHS = listOf<String>()
    }
}
