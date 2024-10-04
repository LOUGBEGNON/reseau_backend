package car.homework.msgcallapp.controller;

import car.homework.msgcallapp.model.AppUser;  // Mettez à jour ici
import car.homework.msgcallapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint pour authentifier ou enregistrer un utilisateur
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        // Vérification des valeurs envoyées
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Le nom d'utilisateur et le mot de passe sont obligatoires");
        }

        // Vérifier si l'utilisateur existe déjà
        AppUser user = userService.findByUsername(username);
        if (user != null) {
            // Si l'utilisateur existe, vérifier le mot de passe
            if (userService.checkPassword(password, user.getPassword())) {
                userService.setUserStatusOnline(username);
                return ResponseEntity.ok("Authentification réussie pour " + username);
            } else {
                return ResponseEntity.status(401).body("Mot de passe incorrect");
            }
        } else {
            // Si l'utilisateur n'existe pas, l'enregistrer
            String result = userService.registerUser(username, password);
            userService.setUserStatusOnline(username);
            return ResponseEntity.ok("Nouvel utilisateur enregistré et authentifié : " + username);
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

//    // Endpoint pour authentifier l'utilisateur
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
//        System.out.println("ResponseEntity");
//        System.out.println(username);
//        AppUser user = userService.findByUsername(username);  // Utiliser AppUser ici
//        if (user != null && userService.checkPassword(password, user.getPassword())) {
//            return ResponseEntity.ok("Authentification réussie pour " + username);
//        }
//        return ResponseEntity.status(401).body("Échec de l'authentification");
//    }


}