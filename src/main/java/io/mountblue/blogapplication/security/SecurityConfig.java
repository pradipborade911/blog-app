package io.mountblue.blogapplication.security;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    BlogUserDetailsService blogUserDetailsService;

//@Bean
//    public InMemoryUserDetailsManager userDetailsManagerTwo(){
//        UserDetails userPradeep = User.builder()
//                .username("pradipborade")
//                .password("{noop}Password!789")
//                .roles("GUEST")
//                .build();
//
//    UserDetails userSachin = User.builder()
//            .username("sachinsharma")
//            .password("{noop}Password!789")
//            .roles("AUTHOR")
//            .build();
//    return new InMemoryUserDetailsManager(userPradeep, userSachin);
//    }

    @Bean
    public SecurityFilterChain customFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers(HttpMethod.GET, "/*").permitAll()
                                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/*/addComment").permitAll()

                                .requestMatchers("/newpost").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers("/*/edit").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers("/*/delete").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers("/*/comments/*/edit").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers("/*/comments/*/delete").hasAnyRole("ADMIN", "AUTHOR")
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

    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(blogUserDetailsService).passwordEncoder(passwordEncoder());
    }
}