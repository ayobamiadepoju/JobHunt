package com.example.JobPortal.config;

import com.example.JobPortal.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    private final String[] publicUrl = {
            "/",
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/js/**",
            "/summernote/**",
            "/fonts/**",
            "/favicon.ico",
            "/error"
    };


    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService());
//                .authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth->{
            auth.requestMatchers(publicUrl).permitAll();
            auth.anyRequest().authenticated();
        });

        http.formLogin(form -> form.loginPage("/login").permitAll()
                .successHandler(customAuthenticationSuccessHandler));
        http.logout(logout->{
            logout.logoutUrl("/logout/");
            logout.logoutSuccessUrl("/");
        }).cors(Customizer.withDefaults())
                .csrf(csrf ->csrf.disable());

        return http.build();
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        authenticationProvider.setUserDetailsService(customUserDetailsService);
//        return authenticationProvider;
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Return your injected custom implementation. I injected userDetailsService into the SecurityFilterChain,
        //and it was a game changer. DaoAuthentication provider was just so unnecessary. Lmao.
        return customUserDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}