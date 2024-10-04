package car.homework.msgcallapp.security;

import car.homework.msgcallapp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
// Mettez à jour les instructions d'importation dans votre fichier Java
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // Obtenez le nom d'utilisateur après l'authentification réussie
        String username = authentication.getName();

        // Mettez à jour le statut de l'utilisateur comme "CONNECTÉ"
        userService.setUserStatusOnline(username);

        // Redirection ou réponse après authentification réussie
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
