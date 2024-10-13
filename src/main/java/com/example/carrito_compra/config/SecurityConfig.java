package com.example.carrito_compra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Solo admin puede acceder a /admin/*
                        .requestMatchers("/h2-console/**").permitAll() // Permitir acceso a la consola H2 sin restricciones
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // Permitir acceso a recursos estáticos (CSS, JS, imágenes)
                        .requestMatchers("/products/add").authenticated() // Proteger la ruta para añadir productos
                        .requestMatchers("/products/save").authenticated()
                        .requestMatchers("/products/save").hasRole("ADMIN")
                        .requestMatchers("/cart/add/**").permitAll()
                        .anyRequest().permitAll()  // Todas las demás rutas son accesibles sin autenticación
                )
                .csrf().disable()  // Desactivar protección CSRF temporalmente para pruebas
                .formLogin().disable()  // Desactivar login
                .httpBasic().disable()  // Desactivar autenticación básica
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/products/add", true)  // Redirigir a /products después de iniciar sesión exitosamente
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")  // Desactivar CSRF para la consola H2
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin()  // Permitir iframes desde la misma fuente
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        var admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        var user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

}
