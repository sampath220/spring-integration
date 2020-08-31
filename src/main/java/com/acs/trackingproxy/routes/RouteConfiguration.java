package com.acs.trackingproxy.routes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("routeConfiguration")
public class RouteConfiguration {
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private static final String PATTERN = "pattern";
	private static final String MAPTO = "mapTo";

	@Autowired
	RouteService routeService;

	public Message<?> post(Message<?> msg) {
		log.info("POST method : ");
		byte[] requestPayload = (byte[]) msg.getPayload();
		JsonParser jsonParser = new BasicJsonParser();
		Map<String, Object> jsonMap = jsonParser.parseMap(new String(requestPayload));
		List<String> mappings = (List<String>) jsonMap.get("mappings");
		Map<String ,String> temp = routeService.getUrlsMap();
		for (String route : mappings) {
			Map<String, Object> routeMap = jsonParser.parseMap(route);
			temp.put((String) routeMap.get(PATTERN), (String) routeMap.get(MAPTO));
		}
		routeService.setUrlsMap(temp);
		return MessageBuilder.withPayload("").copyHeadersIfAbsent(msg.getHeaders()).setHeader("http_statusCode", HttpStatus.OK).build();
	}

	public Message<?> get(Message<?> msg) {
		log.info("GET method : ");
		List<Map<String, String>> routeMapList = new ArrayList<>();
		for (Map.Entry<String, String> routeParam : routeService.getUrlsMap().entrySet()) {
			Map<String, String> routes = new HashMap<>();
			routes.put(PATTERN, routeParam.getKey());
			routes.put(MAPTO, routeParam.getValue());
			routeMapList.add(routes);
		}
		return MessageBuilder.withPayload(routeMapList).build();
	}

	public Message<?> delete(){
		log.info("DELETE method : ");
		routeService.clearUrlsMap();
		return MessageBuilder.withPayload("").build();
	}
}
