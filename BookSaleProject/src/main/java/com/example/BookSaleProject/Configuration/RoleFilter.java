package com.example.BookSaleProject.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class RoleFilter implements Filter {

    private static final List<String> EXCLUDED_PATHS = Arrays.asList("/user/", "/book/", "/client/",
            "/css/", "/images/", "/js/");

    private static final List<String> USER_PATHS = Arrays.asList("/cart/", "/bill/");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String path = httpServletRequest.getServletPath();
        System.out.println(path);
        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith) || path.equals("/")) {
            chain.doFilter(request, response);
            return;
        }
        String userRole = (String) httpServletRequest.getSession().getAttribute("roleUser");
        if (userRole == null) {
            httpServletResponse.sendRedirect("/");
            return;
        } else if ("ADMIN".equals(userRole)) {
            chain.doFilter(request, response);
            return;
        } else if ("USER".equals(userRole)) {
            if (USER_PATHS.stream().anyMatch(path::startsWith)) {
                chain.doFilter(request, response);
                return;
            } else {
                httpServletResponse.sendRedirect("/user/error");
                return;
            }
        }
    }

}
