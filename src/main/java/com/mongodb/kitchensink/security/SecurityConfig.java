package com.mongodb.kitchensink.security;


import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.service.CustomMemberDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private final CustomMemberDetailsService userDetailsService;

    public SecurityConfig(CustomMemberDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/test", "/registerUser", "/login") // Optionally disable CSRF for specific paths
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())).authorizeHttpRequests(auth->auth
                .requestMatchers("/test","/registerUser","/login").permitAll()
                        .requestMatchers("/user/**").hasAnyRole("user","admin","USER","ADMIN")
                .requestMatchers("/admin/**").hasRole("admin")
                        .anyRequest().authenticated())
                .formLogin(form-> form.successHandler(new SuccessHandler()).permitAll());



        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}


