package de.marcusjanke.examples.spring.oauth2;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class JwtAuthenticationConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(expressionInterceptUrlRegistry -> expressionInterceptUrlRegistry.anyRequest().authenticated())
                .oauth2ResourceServer().jwt();
    }
}
