package com.library.librarymanager.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class Utf8WebConfig {

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        return new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true, true);
    }

    @Bean
    public FilterRegistrationBean<Filter> utf8ContentTypeFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new Utf8ContentTypeFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    private static final class Utf8ContentTypeFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            if (response instanceof HttpServletResponse httpResponse) {
                chain.doFilter(request, new Utf8ResponseWrapper(httpResponse));
                return;
            }
            chain.doFilter(request, response);
        }
    }

    private static final class Utf8ResponseWrapper extends HttpServletResponseWrapper {
        private Utf8ResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void setContentType(String type) {
            super.setContentType(withUtf8Charset(type));
        }

        @Override
        public void setHeader(String name, String value) {
            if ("Content-Type".equalsIgnoreCase(name)) {
                super.setHeader(name, withUtf8Charset(value));
                return;
            }
            super.setHeader(name, value);
        }

        @Override
        public void addHeader(String name, String value) {
            if ("Content-Type".equalsIgnoreCase(name)) {
                super.addHeader(name, withUtf8Charset(value));
                return;
            }
            super.addHeader(name, value);
        }

        private static String withUtf8Charset(String contentType) {
            if (contentType == null || contentType.toLowerCase(Locale.ROOT).contains("charset=")) {
                return contentType;
            }

            String lower = contentType.toLowerCase(Locale.ROOT);
            if (lower.startsWith("text/")
                    || lower.contains("javascript")
                    || lower.contains("json")
                    || lower.contains("xml")) {
                return contentType + ";charset=UTF-8";
            }
            return contentType;
        }
    }
}
