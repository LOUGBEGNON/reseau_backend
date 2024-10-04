package car.homework.msgcallapp.config;

import car.homework.msgcallapp.model.AppUser;
import car.homework.msgcallapp.security.CustomAuthenticationSuccessHandler;
import car.homework.msgcallapp.security.CustomLogoutSuccessHandler;
import car.homework.msgcallapp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(UserService userService, CustomAuthenticationSuccessHandler successHandler) {
        this.userService = userService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/messages/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(withDefaults())
                .formLogin(form -> form
                        .successHandler(successHandler) // Utilisation du gestionnaire de succès personnalisé
//                        .loginPage("/api/auth/login") // Page de connexion personnalisée (si nécessaire)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler(userService))
                        .permitAll());
        return http.build();
    }

    @Bean
    public CustomLogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler(userService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));  // Autoriser toutes les origines
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // Autoriser toutes les méthodes HTTP
        configuration.setAllowedHeaders(Arrays.asList("*"));  // Autoriser tous les en-têtes
        configuration.setAllowCredentials(true);  // Autoriser l'envoi de cookies ou d'informations d'authentification

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Appliquer la configuration CORS à toutes les routes

        return source;
    }


    @Bean
//    @Override
    public UserDetailsService userDetailsService() {
        // Implémenter votre propre gestion des utilisateurs ici
        return username -> {
            AppUser user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
            }
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        };
    }
}
