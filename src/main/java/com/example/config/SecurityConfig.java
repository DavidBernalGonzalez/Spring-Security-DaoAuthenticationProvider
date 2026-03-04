package com.example.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	// SecurityFilterChain → Construye y registra la cadena de filtros de seguridad.
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		return httpSecurity.build();
	}

  // AuthenticationManager → Obtiene el gestor de autenticación configurado por Spring.
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
		return authenticationConfiguration.getAuthenticationManager();
	}

  // AuthenticationProvider → Define el proveedor encargado de validar las credenciales del usuario.
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

  // PasswordEncoder → Se encarga de codificar y verificar las contraseñas del usuario.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	// UserDetailsService → Se encarga de cargar y proporcionar los datos del usuario durante el proceso de autenticación.
	@Bean
	public UserDetailsService userDetailsService() {
		List<UserDetails> userDetailsList = new ArrayList<>();
		
		UserDetails userDetails = User.withUsername("davidBernal")
										.password("1234").roles("ADMIN")
										.authorities("READ", "CREATE", "DELETE")
										.build();
		// Manera 1
		userDetailsList.add(userDetails);
		// Manera 2
		userDetailsList.add(User.withUsername("user")
				.password("1234").roles("USER")
				.authorities("READ")
				.build());
		
		return new InMemoryUserDetailsManager(userDetailsList);
	}
}
