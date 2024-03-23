package kim.hyunsub.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncConfiguration : AsyncConfigurer {
	override fun getAsyncExecutor(): Executor =
		ThreadPoolTaskExecutor().apply {
			corePoolSize = 128
			maxPoolSize = 512
			queueCapacity = 640
			setThreadNamePrefix("async-")
			setWaitForTasksToCompleteOnShutdown(true)
			initialize()
		}
}
