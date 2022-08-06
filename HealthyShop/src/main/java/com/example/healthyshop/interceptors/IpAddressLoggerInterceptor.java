package com.example.healthyshop.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;

public class IpAddressLoggerInterceptor implements HandlerInterceptor {
    private Logger logger;

    public IpAddressLoggerInterceptor(){
        this.logger = LoggerFactory.getLogger(IpAddressLoggerInterceptor.class.getSimpleName());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isBlank()){
            ipAddress = request.getRemoteAddr();
        }
        logger.info("Current IP address: " + ipAddress);
        return true;
    }
}
