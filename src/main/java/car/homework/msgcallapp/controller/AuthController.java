package car.homework.msgcallapp.controller;

import car.homework.msgcallapp.model.AppUser;  // Mettez à jour ici
import car.homework.msgcallapp.model.INMSGPacket;
import car.homework.msgcallapp.service.UserService;
import car.homework.msgcallapp.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final WebSocketService webSocketService;

    public AuthController(UserService userService, WebSocketService webSocketService) {
        this.userService = userService;
        this.webSocketService = webSocketService;
    }

    public boolean sendPacket(INMSGPacket packet) {
        String packetData = convertPacketToJson(packet);
        return webSocketService.send(packetData);
    }

    private String convertPacketToJson(INMSGPacket packet) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(packet);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Endpoint pour authentifier ou enregistrer un utilisateur
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Le nom d'utilisateur et le mot de passe sont obligatoires");
        }

        AppUser user = userService.findByUsername(username);
        if (user != null) {
            if (userService.checkPassword(password, user.getPassword())) {
                userService.setUserStatusOnline(username);
                INMSGPacket packet = new INMSGPacket("1.0", 256, UUID.randomUUID().toString(), "0x01", "SUCCESS", "Login réussi");

                boolean isSent = sendPacket(packet);
                System.out.println("isSent");
                System.out.println(isSent);
                if (isSent) {
                    return ResponseEntity.ok("Authentification réussie pour " + username);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'envoi du message de réussite");
                }
            } else {
                INMSGPacket packet = new INMSGPacket("1.0", 256, UUID.randomUUID().toString(), "0x01", "FAILURE", "Mot de passe incorrect");
                boolean isSent = sendPacket(packet);
                System.out.println("isSent");
                System.out.println(isSent);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect");
            }
        } else {
            String result = userService.registerUser(username, password);
            userService.setUserStatusOnline(username);
            INMSGPacket packet = new INMSGPacket("1.0", 256, UUID.randomUUID().toString(), "0x01", "SUCCESS", "Nouvel utilisateur enregistré");

            boolean isSent = sendPacket(packet);
            if (isSent) {
                return ResponseEntity.ok("Nouvel utilisateur enregistré et authentifié : " + username);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'envoi du message de nouvel utilisateur");
            }
        }
    }


    // Endpoint pour définir le statut de l'utilisateur
    @PostMapping("/status")
    public ResponseEntity<String> updateStatus(@RequestParam String username, @RequestParam String status) {
        userService.updateUserStatus(username, AppUser.UserStatus.valueOf(status));
        return ResponseEntity.ok("Statut mis à jour pour " + username);
    }

    // Nouvelle route pour obtenir la liste des utilisateurs
    @GetMapping("/status/all")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Endpoint pour récupérer le statut d'un utilisateur
    @GetMapping("/status/{username}")
    public ResponseEntity<String> getStatus(@PathVariable String username) {
        String status = String.valueOf(userService.getUserStatus(username));
        return ResponseEntity.ok("Le statut de " + username + " est : " + status);
    }
}