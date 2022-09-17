package kim.hyunsub.photo.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(PhotoProperties::class)
class PhotoConfiguration
