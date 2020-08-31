package com.acs.trackingproxy.routes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

public class RouteService {
    private Map<String, String> urlRedirectRuleMap;

    @Autowired
    private Environment env;

    private static String REDIS_KEY = "Tracking_proxy_entry";

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public void init() {
        urlRedirectRuleMap = new HashMap<>();
    }

    /***
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String getOutboundTrackingUrl(String url) throws MalformedURLException {
        log.info("Received Url " + url);
        url = url.replace(env.getProperty("server.servlet.context-path"), "");
        URL formattedURL = new URL(url);
        try {
            for (Map.Entry<String, String> sp : getUrlsMap().entrySet()) {
                Pattern pattern = Pattern.compile(sp.getKey());
                Matcher matcher = pattern.matcher(url);
                Matcher subSetMatcher = pattern.matcher(url);
                if (matcher.matches() || subSetMatcher.find()) {
                    return buildURL(sp.getKey(), sp.getValue(), url);
                }
            }
            log.info("Redirected to default environment");
            return formattedURL.toString().replace(formattedURL.getHost(), "www.google.com/search?").replace(":" + env.getProperty("server.port"), "").replace(formattedURL.getProtocol(), "https");
        } catch (Exception ex) {
            log.error("Error while building url", ex);
            return formattedURL.toString().replace(formattedURL.getHost(), "google.com").replace(":" + env.getProperty("server.port"), "").replace(formattedURL.getProtocol(), "https");
        }
    }

    /***
     *
     * @param queryParams
     * @return
     */
    public String getQueryParamsString(String queryParams) {
        if (!StringUtils.isEmpty(queryParams)) {
            return "?" + queryParams;
        }
        return "";
    }

    /***
     *
     * @param pattern
     * @param mapTo
     * @param uri
     * @return
     * @throws Exception
     */
    private String buildURL(String pattern, String mapTo, String uri) throws Exception {
        boolean isEmpty = !new URL(mapTo).getPath().isEmpty();
        String updatedURL = isEmpty
                ? new URL(mapTo).toString().replace(new URL(mapTo).getPath(), new URL(uri).getPath())
                : new URL(mapTo).toString() + new URL(uri).getPath();
        log.info("Redirected to environment  " + updatedURL);
        return updatedURL;
    }

    public Map<String, String> getUrlsMap() {
        return urlRedirectRuleMap;
    }

    public void setUrlsMap(Map<String, String> map) {
        urlRedirectRuleMap = map;
    }

    public void clearUrlsMap() {
        this.urlRedirectRuleMap.clear();
    }
}