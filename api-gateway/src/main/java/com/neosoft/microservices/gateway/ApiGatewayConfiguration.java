package com.neosoft.microservices.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) { 
		
		return builder.routes()
				.route(p -> p
					.path("/get")
					.filters(f -> f
							.addRequestHeader("CustomHeader", "SKD")
							.addRequestParameter("CustomParam","Demon"))
					.uri("http://httpbin.org:80"))
				.route(p -> p.path("/department/**")
						.uri("lb://department"))
				.route(p -> p.path("/employee/dept-feign/**")
						.uri("lb://employee"))
				.route(p -> p.path("/employee/**")
						.uri("lb://employee"))
				.build();
	}
}
