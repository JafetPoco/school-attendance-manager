package com.IEASmart.sistemaAsistencias.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Proveer un valor por defecto para evitar fallo si la propiedad no está definida
    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return (request, response, authentication) -> {
            // Evitar NPE y redirigir a raíz si no hay frontendUrl configurado
            String target = (frontendUrl == null || frontendUrl.isBlank()) ? "/" : frontendUrl.replaceAll("/$", "") + "/dashboard";
            response.sendRedirect(target);
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontendUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PATCH",  "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Set-Cookie"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"No autenticado\"}");
                        })
                )
                .authorizeHttpRequests(requests -> requests
                        // permitir explícitamente la raíz y recursos estáticos
                        .requestMatchers(HttpMethod.GET, "/", "/index", "/api/justifications/public/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/justifications/public/**").permitAll()
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
