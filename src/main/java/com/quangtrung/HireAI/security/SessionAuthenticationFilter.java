package com.quangtrung.HireAI.security;

import com.quangtrung.HireAI.dto.SessionUserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;

    public SessionAuthenticationFilter(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("account");
            Boolean checkLogin = (Boolean) session.getAttribute("checkLogin");
            
            System.out.println("SessionAuthenticationFilter - sessionUser: " + sessionUser);
            System.out.println("SessionAuthenticationFilter - checkLogin: " + checkLogin);
            
            if (sessionUser != null && Boolean.TRUE.equals(checkLogin)) {
                // Nếu chưa có authentication trong SecurityContext, tạo mới từ session
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        System.out.println("Loading user from DB with email: " + sessionUser.getEmail());
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(sessionUser.getEmail());
                        
                        System.out.println("User authorities: " + userDetails.getAuthorities());
                        
                        UsernamePasswordAuthenticationToken authentication = 
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, 
                                        null, 
                                        userDetails.getAuthorities()
                                );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        System.out.println("Authentication set successfully for: " + sessionUser.getEmail());
                    } catch (Exception e) {
                        System.out.println("Error loading user: " + e.getMessage());
                        e.printStackTrace();
                        // Nếu không tìm thấy user hoặc có lỗi, không set authentication
                        // Vẫn cho phép request tiếp tục (có thể là session cũ/hết hạn)
                    }
                } else {
                    System.out.println("Authentication already exists");
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
