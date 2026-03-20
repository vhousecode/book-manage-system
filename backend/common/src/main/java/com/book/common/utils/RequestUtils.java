package com.book.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Request Utility Class
 */
public class RequestUtils {

    private RequestUtils() {
    }

    /**
     * Get current HTTP request
     *
     * @return HttpServletRequest or null
     */
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * Get request header value
     *
     * @param headerName header name
     * @return header value
     */
    public static String getHeader(String headerName) {
        HttpServletRequest request = getCurrentRequest();
        return request != null ? request.getHeader(headerName) : null;
    }

    /**
     * Get client IP address
     *
     * @return IP address
     */
    public static String getClientIp() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // For multiple proxies, get the first IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * Get request URL
     *
     * @return request URL
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getCurrentRequest();
        return request != null ? request.getRequestURL().toString() : null;
    }

    /**
     * Get request URI
     *
     * @return request URI
     */
    public static String getRequestUri() {
        HttpServletRequest request = getCurrentRequest();
        return request != null ? request.getRequestURI() : null;
    }
}
