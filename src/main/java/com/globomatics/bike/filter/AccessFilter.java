package com.globomatics.bike.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AccessFilter.class);
    JwtDecoder decoder;

    @Value("spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
    private String jwkAddress;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        decoder = new NimbusJwtDecoderJwkSupport("https://auth.elinext.com/.well-known/openid-configuration/jwks");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(httpRequest);

        String authHeaderValue = httpRequest.getHeader("Authorization");
        String accessTokenValue = authHeaderValue.substring(7);
        Jwt accessToken = decoder.decode(accessTokenValue);
        Map<String, Object> claims = accessToken.getClaims();
        if (claims.containsKey("http://schemas.microsoft.com/ws/2008/06/identity/claims/role")) {
            mutableRequest.putHeader("role", "admin");
        }
        String email = (String) claims.get("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress");
        mutableRequest.putHeader("email", email);
        log.info("-------------------------------------------------");
        log.info(accessToken.toString());
        log.info(accessToken.getClaims().toString());
        log.info("-------------------------------------------------");
        log.info(mutableRequest.getHeader("email"));
        chain.doFilter(mutableRequest, response);
    }

    @Override
    public void destroy() {

    }
}
