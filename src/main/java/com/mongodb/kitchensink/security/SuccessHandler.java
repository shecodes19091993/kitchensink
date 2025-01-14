package com.mongodb.kitchensink.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


@Override
    public void onAuthenticationSuccess(HttpServletRequest request , HttpServletResponse response, Authentication
                                    authication) throws ServletException, IOException {

    boolean isAdmin = authication.getAuthorities().stream().anyMatch(granted->granted.getAuthority().equals("ROLE_admin"));
             if(isAdmin)
             {
                 setDefaultTargetUrl("/admin/register?id="+authication.getName().split("-")[1]);
             }
             else
             {
                 setDefaultTargetUrl("/user/profile?id="+authication.getName().split("-")[1]);
             }
                super.onAuthenticationSuccess(request,response,authication);
    }
}
