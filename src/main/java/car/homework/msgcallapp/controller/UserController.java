package car.homework.msgcallapp.controller;

import car.homework.msgcallapp.model.AppUser.UserStatus;
import car.homework.msgcallapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import car.homework.msgcallapp.model.AppUser;  // Mettez à jour ici


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
            return ResponseEntity.ok("Statut mis à jour avec succès");
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

    // Récupérer le statut d'un utilisateur spécifique
//    @GetMapping("/status/{username}")
//    public ResponseEntity<String> getUserStatus(@PathVariable String username) {
//        UserStatus status = userService.getUserStatus(username);
//        return ResponseEntity.ok(status.name());
//    }


}
