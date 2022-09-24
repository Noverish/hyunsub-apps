package kim.hyunsub.video.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(VideoProperties::class)
class VideoConfiguration
