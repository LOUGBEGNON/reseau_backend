package car.homework.msgcallapp.service;

import car.homework.msgcallapp.model.AppUser;  // Mettez à jour ici
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import car.homework.msgcallapp.model.AppUser.UserStatus;
//import car.homework.msgcallapp.repository.AppUserRepository;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private Map<String, AppUser> users = new HashMap<>(); // Utilisation de AppUser
//    private final AppUserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        // Ajouter des utilisateurs pour le test
//        users.put("user1", new AppUser("user1", passwordEncoder.encode("password"), "Online"));
//        users.put("user2", new AppUser("user2", passwordEncoder.encode("password"), "Offline"));
//        users.put("user3", new AppUser("user3", passwordEncoder.encode("password"), "Online"));
//        users.put("user4", new AppUser("user4", passwordEncoder.encode("password"), "Offline"));
//        users.put("user5", new AppUser("user5", passwordEncoder.encode("password"), "Online"));
//        users.put("user6", new AppUser("user6", passwordEncoder.encode("password"), "Offline"));
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
        users.put(username, user); // Enregistrer l'utilisateur dans la mémoire
//        users.put(username, new AppUser(username, encodedPassword));
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
        System.out.println("je suis");
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

//    public String getUserStatus(String username) {
//        AppUser user = users.get(username);
//        return user != null ? user.getStatus() : "Unknown";
//    }
}