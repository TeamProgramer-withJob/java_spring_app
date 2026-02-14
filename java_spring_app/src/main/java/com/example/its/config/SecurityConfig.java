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
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import com.example.its.WebAuthFailedHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsManager authenticateUserByDb(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserManager.setUsersByUsernameQuery("select username, password, true as enabled from users where username=?");
        // value in role must be ROLE_nameOfRle, the value in password must have a prefix like {noop}
        jdbcUserManager.setAuthoritiesByUsernameQuery("select username, 'ROLE_ADMIN' as role from users where username=?");

        return jdbcUserManager;
    }

    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, WebAuthFailedHandler authFailedHandler) throws Exception {
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> {
            login.loginPage("/login")
                 .loginProcessingUrl("/authenticateTheUser")
//                 .failureHandler(new WebAuthFailedHandler())
                 .defaultSuccessUrl("/issues?lang=ja")
                 .permitAll();
        });
        
        http.logout(logout -> {
        	logout.logoutRequestMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/logout"))
        	      //.logoutUrl("/logout")
        	      .logoutSuccessUrl("/login?logout")
        	      .invalidateHttpSession(true)
        	      .deleteCookies("web_service_its");
        });

        String[] permittedUrls = {"/css/**", "/webjars/**","/h2-console/**","/signup/**","/test/**","/error/**","/access/denied/**"};
        http.authorizeHttpRequests( auth -> {
            auth.requestMatchers(permittedUrls).permitAll()
                .requestMatchers("/issues/**").hasAnyRole("ADMIN", "USER")
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

/*
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // H2 Console configuration
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
*/