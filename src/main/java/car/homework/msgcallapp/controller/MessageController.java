package car.homework.msgcallapp.controller;

import car.homework.msgcallapp.model.Message;
import car.homework.msgcallapp.model.MessageRequest;
import car.homework.msgcallapp.model.AESEncryption;
import car.homework.msgcallapp.model.INMSGPacket;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import car.homework.msgcallapp.service.MessageService;
import car.homework.msgcallapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    @Autowired
    private UserService userService;

    // Assurez-vous que MessageService est passé par le constructeur
    public MessageController(MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageRequest messageRequest) {
        try {
            String encryptedMessage = AESEncryption.encrypt(messageRequest.getMessageContent());
            INMSGPacket packet = new INMSGPacket("1.0", 256, "session123", "0x03", "SUCCESS", encryptedMessage);
            if (messageRequest.getSenderId() == null || messageRequest.getRecipientId() == null || messageRequest.getMessageContent() == null) {
                return ResponseEntity.badRequest().body("Les données du message sont manquantes.");
            }
            messageService.sendMessage(messageRequest.getSenderId(), messageRequest.getRecipientId(), messageRequest.getMessageContent(), messageRequest.getGroupId());
            // Logique pour envoyer le paquet
            return ResponseEntity.ok("Message chiffré envoyé à " + messageRequest.getRecipientId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors du chiffrement du message.");
        }
    }

    @RequestMapping(value = "/get_messages", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions(HttpServletResponse response) {
        // Ajouter manuellement des en-têtes CORS pour tester
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        return ResponseEntity.ok().build();
    }

//    @GetMapping("/get_messages")
//    public ResponseEntity<List<Message>> getMessages(HttpServletResponse response) {
////        response.setHeader("Access-Control-Allow-Origin", "*");
////        return ResponseEntity.ok(messageService.getMessages());
//        List<Message> decryptedMessages = messageService.getDecryptedMessages();  // Récupérer les messages décryptés
//        return ResponseEntity.ok(decryptedMessages);
//    }

    // Récupérer les messages
    @GetMapping("/get_messages")
    public ResponseEntity<List<Message>> getMessages(@RequestHeader("Authorization") String authHeader) {
        String[] credentials = extractCredentials(authHeader);
        if (credentials == null) {
            return ResponseEntity.status(402).body(null);
        }

        String username = credentials[0];
        String password = credentials[1];

        if (!userService.authenticateUser(username, password)) {
            return ResponseEntity.status(401).body(null);
        }

        List<Message> messages = messageService.getDecryptedMessages();
        return ResponseEntity.ok(messages);
    }

    // Méthode pour extraire les identifiants de l'en-tête Authorization
    private String[] extractCredentials(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Décoder l'en-tête Basic Authorization
            String base64Credentials = authHeader.substring(6);
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            return credentials.split(":", 2);
        }
        return null;
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Message>> getMessagesForGroup(@RequestHeader("Authorization") String authHeader, @PathVariable String groupId) {
        String[] credentials = extractCredentials(authHeader);
        if (credentials == null) {
            return ResponseEntity.status(402).body(null);
        }

        String username = credentials[0];
        String password = credentials[1];

        if (!userService.authenticateUser(username, password)) {
            return ResponseEntity.status(401).body(null);
        }
        List<Message> messages = messageService.getMessagesForGroup(groupId);
        return ResponseEntity.ok(messages);
    }

//    @PostMapping("/send")
//    public ResponseEntity<String> sendMessage(@RequestBody MessageRequest messageRequest) {
//        messageService.sendMessage(messageRequest.getSenderId(), messageRequest.getRecipientId(), messageRequest.getMessageContent(), messageRequest.getGroupId());
//        return ResponseEntity.ok("Message envoyé au groupe " + messageRequest.getGroupId());
//    }

}