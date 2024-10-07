package car.homework.msgcallapp.controller;

import car.homework.msgcallapp.model.AppUser.UserStatus;
import car.homework.msgcallapp.model.INMSGPacket;
import car.homework.msgcallapp.service.UserService;
import car.homework.msgcallapp.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import car.homework.msgcallapp.model.AppUser;  // Mettez à jour ici


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final WebSocketService webSocketService;

    public UserController(UserService userService, WebSocketService webSocketService) {
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

    // Route pour l'inscription des utilisateurs
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");
        String result = userService.registerUser(username, password);
        return ResponseEntity.ok(result);
    }

    // API pour obtenir tous les utilisateurs
    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Récupérer tous les utilisateurs et leurs statuts
    @GetMapping("/status/{username}")
    public ResponseEntity<Map<String, String>> getAllUserStatuses(@PathVariable String username, @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        Map<String, String> statuses = userService.getAllUsers().stream()
                .collect(Collectors.toMap(
                        AppUser::getUsername,
                        user -> user.getStatus().name()
                ));
        return ResponseEntity.ok(statuses);
    }

    // Mettre à jour le statut de l'utilisateur
    @PostMapping("/status/{username}")
    public ResponseEntity<String> updateUserStatus(@PathVariable String username, @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        try {
            UserStatus userStatus = UserStatus.valueOf(status.toUpperCase());
            userService.updateUserStatus(username, userStatus);
            INMSGPacket packet = new INMSGPacket(
                    "1.0",
                    256,
                    UUID.randomUUID().toString(),
                    "0x07",
                    userStatus.name(),
                    "Status update successful"
            );
            boolean isSent = sendPacket(packet);

            if (userStatus == UserStatus.CONNECTED) {
                INMSGPacket packetConnected = new INMSGPacket(
                        "1.0",
                        256,
                        UUID.randomUUID().toString(),
                        "0x10",
                        userStatus.name(),
                        "Status update"
                );
                boolean isSentpacketConnected = sendPacket(packetConnected);
            }

            if (userStatus == UserStatus.AWAY) {
                INMSGPacket packetAway = new INMSGPacket(
                        "1.0",
                        256,
                        UUID.randomUUID().toString(),
                        "0x11",
                        userStatus.name(),
                        "Status update successful"
                );
                boolean isSentAway = sendPacket(packetAway);
            }

            if (userStatus == UserStatus.BUSY) {
                INMSGPacket packetBusy = new INMSGPacket(
                        "1.0",
                        256,
                        UUID.randomUUID().toString(),
                        "0x12",
                        userStatus.name(),
                        "Status update successful"
                );
                boolean isSentBusy = sendPacket(packetBusy);
            }

            if (userStatus == UserStatus.OFFLINE) {
                INMSGPacket packetOffline = new INMSGPacket(
                        "1.0",
                        256,
                        UUID.randomUUID().toString(),
                        "0x13",
                        userStatus.name(),
                        "Status update successful"
                );
                boolean isSentOffline = sendPacket(packetOffline);
            }
            if (isSent) {
                return ResponseEntity.ok("Statut mis à jour avec succès et notifié.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Statut mis à jour mais échec de la notification.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Statut invalide");
        }
    }

    // API pour récupérer tous les utilisateurs connectés
    @GetMapping("/connected")
    public ResponseEntity<List<AppUser>> getConnectedUsers() {
        List<AppUser> connectedUsers = userService.getOnlineUsers();
        return ResponseEntity.ok(connectedUsers);
    }

    // API pour définir un utilisateur comme connecté
    @PostMapping("/set_status_online/{username}")
    public ResponseEntity<String> setUserStatusOnline(@PathVariable String username) {
        userService.setUserStatusOnline(username);
        return ResponseEntity.ok("Utilisateur " + username + " est maintenant en ligne.");
    }

    // API pour définir un utilisateur comme déconnecté
    @PostMapping("/set_status_offline/{username}")
    public ResponseEntity<String> setUserStatusOffline(@PathVariable String username) {
        userService.setUserStatusOffline(username);
        return ResponseEntity.ok("Utilisateur " + username + " est maintenant hors ligne.");
    }
}
