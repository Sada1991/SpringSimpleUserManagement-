package net.igap.config;


import net.igap.security.JwtAuthEntryPoint;
import net.igap.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ProjectSecurityConfig {

  private UserDetailsService userDetailsService;

  private JwtAuthEntryPoint authenticationEntryPoint;

  private JwtAuthFilter authenticationFilter;

  public ProjectSecurityConfig(UserDetailsService userDetailsService,
      JwtAuthEntryPoint authenticationEntryPoint,
      JwtAuthFilter authenticationFilter) {
    this.userDetailsService = userDetailsService;
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.authenticationFilter = authenticationFilter;
  }


  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeHttpRequests((authorize) ->
            authorize
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/provinces", "/api/v1/provinces/**", "/actuator/**",
                    "/actuator").hasRole("ADMIN")
                .anyRequest().authenticated()

        ).exceptionHandling(exception -> exception
            .authenticationEntryPoint(authenticationEntryPoint)
        ).sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
