package kim.hyunsub.auth.config

import org.apache.tomcat.util.http.LegacyCookieProcessor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class ApplicationContext {
	// 쿠키에 서브도메인 기능을 넣기 위해서 필요함
	@Bean
	fun cookieProcessorCustomizer(): WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
		return WebServerFactoryCustomizer { factory ->
			factory.addContextCustomizers(TomcatContextCustomizer { context ->
				context.cookieProcessor = LegacyCookieProcessor()
			})
		}
	}
}
