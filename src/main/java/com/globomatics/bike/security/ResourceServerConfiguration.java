package com.globomatics.bike.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Bean("resourceServerRequestMatcher")
    public RequestMatcher resources() {
        return new RequestHeaderRequestMatcher("Authorization");
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .requestMatcher(resources()).authorizeRequests()
                .anyRequest().authenticated();
    }

}
