package ro.websocket.server.rest.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ro.websocket.server.commons.UserRole;
import ro.websocket.server.rest.security.auth.BasicAuthProvider;
import ro.websocket.server.rest.security.auth.BasicAuthenticationFilter;
import ro.websocket.server.rest.security.jwt.JwtAuthProvider;
import ro.websocket.server.rest.security.jwt.JwtAuthenticationFilter;
import ro.websocket.server.rest.security.jwt.JwtService;
import ro.websocket.server.rest.security.util.SkipRequestMatcher;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class WebApiSecurityConfig {

    public static final String LOGIN_URL = "/login";
    public static final String WS_URL = "/ws/**";
    public static final String CSRF_URL = "/csrf-token/**";

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http,
                                              JwtService jwtService,
                                              AuthenticationManager authenticationManager) throws Exception {
        return http
                .csrf(
                        csrf -> csrf
                                .csrfTokenRepository(csrfTokenRepository())
                                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                                // Since login API uses token-based authentication (JWT), CSRF protection is not necessary.
                                .ignoringRequestMatchers(new AntPathRequestMatcher(LOGIN_URL, POST.name()))
                )
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(POST, LOGIN_URL).permitAll()
                        // WS handshake authentication and subscription authorization will be handled by the Websocket security configurations
                        .requestMatchers(WS_URL).permitAll()
                        .requestMatchers(GET, "/is-authenticated").authenticated()
                        .requestMatchers("/notifier/**").hasRole(UserRole.NOTIFIER.getRole())
                        .requestMatchers(GET, CSRF_URL).authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(basicAuthenticationFilter(jwtService, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       @Qualifier("basicAuthProvider") AuthenticationProvider usernameAndPasswordAuthenticationProvider,
                                                       @Qualifier("jwtAuthProvider") AuthenticationProvider jwtProvider
    ) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.authenticationProvider(usernameAndPasswordAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(jwtProvider);
        authenticationManagerBuilder.parentAuthenticationManager(null);

        return authenticationManagerBuilder.build();
    }

    @Bean("basicAuthProvider")
    public AuthenticationProvider basicAuthProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new BasicAuthProvider(userDetailsService, passwordEncoder);
    }

    @Bean("jwtAuthProvider")
    public AuthenticationProvider jwtAuthProvider(JwtService jwtService, UserDetailsService userDetailsService) {
        return new JwtAuthProvider(jwtService, userDetailsService);
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        User user1 = new User("user1", passwordEncoder.encode("user1"), List.of(UserRole.USER::getRoleWithPrefix));
        User user2 = new User("user2", passwordEncoder.encode("user2"), List.of(UserRole.USER::getRoleWithPrefix));
        User user3 = new User("notifier", passwordEncoder.encode("notifier"), List.of(UserRole.NOTIFIER::getRoleWithPrefix));
        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private BasicAuthenticationFilter basicAuthenticationFilter(JwtService jwtService, AuthenticationManager authenticationManager) {
        return new BasicAuthenticationFilter(LOGIN_URL, authenticationManager, jwtService);
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(new SkipRequestMatcher(Set.of(LOGIN_URL, WS_URL)), authenticationManager);
    }

    private CookieCsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookiePath("/");
        return csrfTokenRepository;
    }
}