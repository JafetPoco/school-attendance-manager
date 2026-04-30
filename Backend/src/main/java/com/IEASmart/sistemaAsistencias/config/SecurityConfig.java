package com.IEASmart.sistemaAsistencias.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.sendRedirect("http://localhost:5173/dashboard");
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PATCH",  "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        // permitir explícitamente la raíz y recursos estáticos
                        .requestMatchers(HttpMethod.GET, "/", "/index", "/api/justifications/public/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/**", "/api/justifications/public/**").permitAll()
                        .requestMatchers("/favicon.ico", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        // permitir endpoints de OAuth2 y callbacks
                        .requestMatchers("/oauth2/**", "/oauth2/authorization/**", "/login", "/login/oauth2/**", "/login/oauth2/code/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                // deshabilitamos el formLogin: sólo OAuth2
                .formLogin(form -> form.disable())
                // activamos oauth2Login y usamos un success handler que redirige al front
                .oauth2Login(oauth2 -> oauth2.successHandler(authenticationSuccessHandler()))
                // cerrar sesión redirigiendo al front
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // Solo responde 200 OK
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));
        return http.build();
    }
}
