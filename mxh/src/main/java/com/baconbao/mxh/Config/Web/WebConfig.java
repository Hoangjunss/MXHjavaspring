package com.baconbao.mxh.Config.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable()).authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .formLogin(login -> login.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/", true)
                        .permitAll()).logout( logout -> logout
                        .logoutRequestMatcher(
                                        new AntPathRequestMatcher("/logout"))
                        .permitAll().deleteCookies("auth_code", "JSESSIONID")
                        .invalidateHttpSession(true));
                        return http.build();
    }
         @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth
                                .userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder());
        }

}
