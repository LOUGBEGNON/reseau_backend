package car.homework.msgcallapp.service;

import car.homework.msgcallapp.model.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import car.homework.msgcallapp.model.AppUser.UserStatus;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private Map<String, AppUser> users = new HashMap<>(); // Utilisation de AppUser

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Méthode d'inscription
    public String registerUser(String username, String rawPassword) {
        if (users.containsKey(username)) {
            return "L'utilisateur existe déjà.";
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);
        AppUser user = new AppUser(username, encodedPassword);
        System.out.println("user");
        System.out.println(user);
        users.put(username, user);
        return "Utilisateur inscrit avec succès.";
    }

    // Récupérer tous les utilisateurs
    public List<AppUser> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // Rechercher un utilisateur par nom d'utilisateur
    public AppUser findByUsername(String username) {
        return users.get(username);
    }

    // Vérifier le mot de passe de l'utilisateur
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void updateUserStatus(String username, UserStatus status) {
        AppUser user = users.get(username);
        if (user != null) {
            user.setStatus(status);
        }
    }

    // Vérifier les identifiants de l'utilisateur
    public boolean authenticateUser(String username, String rawPassword) {
        System.out.println(username);
        System.out.println(rawPassword);
        AppUser user = users.get(username);
        if (user != null) {
            // Comparer le mot de passe en clair avec le mot de passe haché stocké
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }


    // Obtenir le statut de l'utilisateur
    public UserStatus getUserStatus(String username) {
        AppUser user = users.get(username);
        return user != null ? user.getStatus() : UserStatus.OFFLINE;
    }

    // Méthode pour marquer un utilisateur comme connecté
    public void setUserStatusOnline(String username) {
        AppUser user = users.get(username);
        if (user != null) {
            user.setStatus(UserStatus.CONNECTED);
        } else {
            System.out.println("Utilisateur non trouvé : " + username);
        }
    }

    // Méthode pour marquer un utilisateur comme déconnecté
    public void setUserStatusOffline(String username) {
        AppUser user = users.get(username);
        if (user != null) {
            user.setStatus(UserStatus.OFFLINE);
        }
    }

    // Récupérer tous les utilisateurs connectés
    public List<AppUser> getOnlineUsers() {
        List<AppUser> onlineUsers = new ArrayList<>();
        for (AppUser user : users.values()) {
            if (user.getStatus() == UserStatus.CONNECTED) {
                onlineUsers.add(user);
            }
        }
        return onlineUsers;
    }
}