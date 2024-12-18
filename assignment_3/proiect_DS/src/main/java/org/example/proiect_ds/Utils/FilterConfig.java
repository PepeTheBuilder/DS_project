package org.example.proiect_ds.Utils;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtValidationFilter> jwtFilter() {
        FilterRegistrationBean<JwtValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtValidationFilter());
        registrationBean.addUrlPatterns("/api/users/*"); // Apply to all endpoints under /api/
        return registrationBean;
    }
}
