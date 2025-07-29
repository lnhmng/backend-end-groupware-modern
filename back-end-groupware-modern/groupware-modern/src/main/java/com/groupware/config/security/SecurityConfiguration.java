package com.groupware.config.security;

import com.groupware.entity.user.permission.Permission;
import com.groupware.entity.user.role.Role;
import com.groupware.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final MenuRepository menuRepository;

    String[] ignoredList = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/templates/**",
            "/WEB-INF/jsp/**",
            "/ws-data/**",
            "/login**",
            "/sign-in",
            "/assets/**",
            "/api/v1/common/**"
    };
    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setSuffix(".html");
        bean.setContentType("text/html;charset=UTF-8");
        return bean;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<Map<String, String>> authorities = menuRepository.rolePermission();

        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        ignoredList
                )
                .permitAll();

        for (Map<String, String> menu : authorities) {
            if (menu.get("method").equals("GET")) {
                http.authorizeHttpRequests()
                        .requestMatchers(GET, menu.get("apiLink")).hasAnyAuthority(menu.get("rolePermission"));
            } else if (menu.get("method").equals("POST")) {
                http.authorizeHttpRequests()
                        .requestMatchers(POST, menu.get("apiLink")).hasAnyAuthority(menu.get("rolePermission"));
            } else if (menu.get("method").equals("PUT")) {
                http.authorizeHttpRequests()
                        .requestMatchers(PUT, menu.get("apiLink")).hasAnyAuthority(menu.get("rolePermission"));
            } else if (menu.get("method").equals("DELETE")) {
                http.authorizeHttpRequests()
                        .requestMatchers(DELETE, menu.get("apiLink")).hasAnyAuthority(menu.get("rolePermission"));
            } else if (menu.get("method").equals("PATCH")) {
                http.authorizeHttpRequests()
                        .requestMatchers(PATCH, menu.get("apiLink") + "/**").hasAnyAuthority(menu.get("rolePermission"));
            }
        }

        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/management/**").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())
                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_READ.name(), Permission.MANAGER_READ.name())
                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_CREATE.name(), Permission.MANAGER_CREATE.name())
                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_UPDATE.name(), Permission.MANAGER_UPDATE.name())
                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_DELETE.name(), Permission.MANAGER_DELETE.name())

                .anyRequest()
                .authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                .and().exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
        ;


        http.formLogin().loginPage("/login")
                .failureUrl("/login?error= wrong username or password")
                .defaultSuccessUrl("/web/home", true);
        return http.build();
    }

    @Bean
    public HttpFirewall getHttpFirewall() {
        return new DefaultHttpFirewall();
    }

}
