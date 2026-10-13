package integra.momentifly.config;

import integra.momentifly.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // TODO i can just delete any account if i'm logged in via postman / likely other tools
    //  (assuming i know the user id, idk if this is something to fix or not,
    //  but i can use the GET /api/users and get the id of all users via postman, so..)
    //  Fix might be to add a role to user??

    // TODO UpdateUserRequest DTO does not contain password right now
    //   since passwords should never be returned in API responses
    //    but then how to update password if the user wants to?

    // TODO see more about SecurityConfig and if there's a better way to code this

    // filter: Which endpoints are public and which need login
    // What to send back when authentication fails
    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // registrations should work no matter what
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll() // swagger does not work otherwise
                        .anyRequest().authenticated()) // everything else needs valid credentials
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint()));

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain");
            boolean sentCredentials = request.getHeader(HttpHeaders.AUTHORIZATION) != null;
            response.getWriter().write(sentCredentials
                    ? "Invalid username or password"
                    : "Authentication required");
        };
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(u -> org.springframework.security.core.userdetails.User
                        .withUsername(u.getUsername())
                        .password(u.getPassword())
                        .roles("USER") // hardcoded, idk
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
