package com.example.its.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	/*
    @Bean
    public UserDetailsManager authenticateUserByDb(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserManager.setUsersByUsernameQuery("select email, password, true as enabled from users where email=?");
        // value in role must be ROLE_nameOfRle, the value in password must have a prefix like {noop}
        jdbcUserManager.setAuthoritiesByUsernameQuery("select email, 'ROLE_' || role as role from users where email=?");

        return jdbcUserManager;
    }
    */

    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, WebAuthFailedHandler authFailedHandler) throws Exception {
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> {
            login.loginPage("/login")
                 .loginProcessingUrl("/authenticateTheUser")
//                 .failureHandler(new WebAuthFailedHandler())
                 .defaultSuccessUrl("/memories")
                 .permitAll();
        });
        
        http.logout(logout -> {
        	logout.logoutRequestMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/logout"))
        	      //.logoutUrl("/logout")
        	      .logoutSuccessUrl("/login?logout")
        	      .invalidateHttpSession(true)
        	      .deleteCookies("web_service_its");
        });

        String[] permittedUrls = {"/","/css/**", "/webjars/**","/h2-console/**","/signup/**","/inquiries/**","/test/**","/error/**","/access/denied/**"};
        http.authorizeHttpRequests( auth -> {
            auth.requestMatchers(permittedUrls).permitAll()
                .requestMatchers("/memories/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.PUT, "/admin/users/update/*").authenticated()
//                .requestMatchers(HttpMethod.PUT, "/admin/users/update/**").authenticated()
                .requestMatchers("/admin/users/delete/**").hasRole("ADMIN")
                .requestMatchers("/admin/users/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated();
        })
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
        .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
        )
        .exceptionHandling(ex -> ex.accessDeniedPage("/access/denied"));

        
        http.logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
