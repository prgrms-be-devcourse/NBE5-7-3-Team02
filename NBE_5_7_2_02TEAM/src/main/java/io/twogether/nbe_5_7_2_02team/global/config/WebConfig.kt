package io.twogether.nbe_5_7_2_02team.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Paths

@Configuration
class WebConfig : WebMvcConfigurer {
    companion object {
        private const val UPLOAD_PATH = "/uploads/**"
        private const val UPLOAD_DIR = "uploads"
        private const val CACHE_PERIOD = 3600
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val uploadLocation = "file:${Paths.get(UPLOAD_DIR).toAbsolutePath()}/"

        registry
            .addResourceHandler(UPLOAD_PATH)
            .addResourceLocations(uploadLocation)
            .setCachePeriod(CACHE_PERIOD)
    }
}
