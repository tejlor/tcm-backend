package pl.olawa.telech.tcm.commons.config.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

/*
 * Configuration of custom exception handler for async methods.
 */
@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
	    return new CustomAsyncExceptionHandler();
	}
}
