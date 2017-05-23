
package com.sentifi;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.Module;
import com.sentifi.stockprice.aop.monitoring.MyPerformanceInterceptor;
import com.sentifi.stockprice.jsonserializer.StockPriceJsonModule;
import com.sentifi.stockprice.jsonserializer.TickerSymbolClosePriceSerializer;
import com.sentifi.stockprice.vo.TickerSymbolClosePrice;

@SpringBootApplication
@EnableCaching
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class StockPriceService extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(StockPriceService.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(StockPriceService.class);
	}
	
	@Bean
	public Module tickerJsonModule() {
		StockPriceJsonModule tickerJsonModule = new StockPriceJsonModule();
		// register custom TickerSymbolClosePriceSerializer for TickerSymbolClosePrice 
		tickerJsonModule.addSerializer(TickerSymbolClosePrice.class, new TickerSymbolClosePriceSerializer());
		
	    return tickerJsonModule;
	}
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
	    return restTemplate;
	}
	
	// enable monitoring of service method with MyPerformanceInterceptor
	// to log long running methods so that we can optimize them.
	@Bean
	public Advisor debugAdvisor() {
	   AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
	    pointcut.setExpression("execution(* com.sentifi.stockprice.business.service.impl.*.* (..))");
	    return new DefaultPointcutAdvisor(pointcut, new MyPerformanceInterceptor());
	}
	
}
