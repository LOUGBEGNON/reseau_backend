package car.homework.msgcallapp.security;

import car.homework.msgcallapp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UserService userService;

    public CustomLogoutSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            // Obtenez le nom d'utilisateur après la déconnexion
            String username = authentication.getName();

            // Mettez à jour le statut de l'utilisateur comme "OFFLINE"
            userService.setUserStatusOffline(username);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
