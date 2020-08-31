package com.acs.trackingproxy;

import com.acs.trackingproxy.routes.RouteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
@ImportResource({ "classpath:http-inbound-outbound-gateway.xml" })
public class Application {

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}

	@Bean(initMethod="init")
	public RouteService routeService() {
		RouteService routeService = new RouteService();
		return routeService;
	}
}
