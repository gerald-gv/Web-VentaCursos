package com.EFSRTIII.ventacursos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public SecurityConfig() {
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers(
                                        "/",
                                        "/auth/login/**",
                                        "/auth/register",
                                        "/nosotros",
                                        "/programas",
                                        "/soporte",
                                        "/js/**"
                ).permitAll()

                                .requestMatchers("/admin/**")
                                .hasRole("ADMIN")

                                .requestMatchers("/usuario/**")
                                .hasAnyRole("ADMIN", "CLIENTE")

                .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("contrasenia")
                        .defaultSuccessUrl("/rutaQueDeberiaSerLaDelUsuario", true)
                        .failureUrl("/?error=true")
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/programas"))
                )

                .build();
    }
}
