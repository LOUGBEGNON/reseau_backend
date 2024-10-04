package car.homework.msgcallapp.service;

import car.homework.msgcallapp.model.AESEncryption;
import car.homework.msgcallapp.model.Message;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final List<Message> messages = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            // Générer une clé AES au démarrage de l'application
            AESEncryption.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la génération de la clé AES.");
        }
    }

    public void sendMessage(String sender, String recipient, String content, String groupId) {
        try {
            // Chiffrement du contenu du message avant de l'envoyer
            String encryptedContent = AESEncryption.encrypt(content);
            Message message = new Message(sender, recipient, encryptedContent, groupId);
            messages.add(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chiffrement du message.");
        }
    }

    public String receiveMessage(Message message) {
        try {
            // Déchiffrement du contenu du message
            return AESEncryption.decrypt(message.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du déchiffrement du message.");
        }
    }

    public List<Message> getDecryptedMessages() {
        List<Message> decryptedMessages = new ArrayList<>();
        for (Message message : messages) {
            try {
                // Déchiffrement du message avant de l'ajouter à la liste
                String decryptedContent = AESEncryption.decrypt(message.getContent());
                Message decryptedMessage = new Message(message.getSender(), message.getRecipient(), decryptedContent, message.getGroupId());
                decryptedMessages.add(decryptedMessage);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors du déchiffrement du message.");
            }
        }
        return decryptedMessages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    // Récupérer les messages d'un groupe spécifique
    public List<Message> getMessagesForGroup(String groupId) {
        return messages.stream()
                .filter(message -> message.getGroupId().equals(groupId))  // Filtrer par groupId
                .map(message -> {
                    try {
                        // Déchiffrement du message
                        String decryptedContent = AESEncryption.decrypt(message.getContent());
                        // Créer un nouveau message déchiffré
                        return new Message(message.getSender(), message.getRecipient(), decryptedContent, message.getGroupId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Erreur lors du déchiffrement du message.");
                    }
                })
                .collect(Collectors.toList());
    }
}