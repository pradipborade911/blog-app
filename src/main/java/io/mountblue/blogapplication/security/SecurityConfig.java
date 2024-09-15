package io.mountblue.blogapplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    BlogUserDetailsService blogUserDetailsService;

    @Bean
    public SecurityFilterChain customFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers(HttpMethod.GET, "/").permitAll() //view all posts
                                .requestMatchers(HttpMethod.GET, "/{id:\\d+}").permitAll() //view specific post
                                .requestMatchers(HttpMethod.GET, "/search").permitAll() //search in posts
                                .requestMatchers(HttpMethod.GET, "/filter").permitAll() //filter posts
                                .requestMatchers(HttpMethod.POST, "/register").permitAll() //register new user
                                .requestMatchers(HttpMethod.GET, "/register").permitAll() //register new user
                                .requestMatchers(HttpMethod.POST, "/*/addComment").permitAll() //add comment to post

                                .requestMatchers("/newpost").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers("/*/edit").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers("/*/delete").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers("/*/comments/*/edit").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers("/*/comments/*/delete").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers("/comments/*/update").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers("/*/update").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers(HttpMethod.POST, "/**").hasAnyRole("ADMIN")

                                .anyRequest().authenticated())
                .formLogin(form ->
                        form
                                .loginPage("/signin")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(blogUserDetailsService).passwordEncoder(passwordEncoder());
    }
}