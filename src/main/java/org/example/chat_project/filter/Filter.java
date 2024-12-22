package org.example.chat_project.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {
    List<String> openPages = List.of("/login","/register");
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!openPages.contains(request.getRequestURI())){
            Object user = request.getSession().getAttribute("currentUser");
            if (user == null){
                response.sendRedirect("/login");
                return;
            }

        }
        filterChain.doFilter(request,response);

    }
}
