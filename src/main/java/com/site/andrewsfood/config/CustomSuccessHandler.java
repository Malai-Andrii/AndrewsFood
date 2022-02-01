package com.site.andrewsfood.config;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            System.out.println("Can't redirect");
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }


    protected String determineTargetUrl(Authentication authentication) {
        List<String> roles = new LinkedList<String>();

        //Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority a : authentication.getAuthorities()) {
            roles.add(a.getAuthority());
        }

        if (roles.contains("ADMIN")) {
            return "/mainAdmin";
        } else if (roles.contains("USER")) {
            return "/mainUser";
        } else {
            return "/accessDenied";
        }
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

}