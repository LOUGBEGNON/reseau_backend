package car.homework.msgcallapp.service;

import org.springframework.web.socket.TextMessage;
import car.homework.msgcallapp.model.AESEncryption;
import car.homework.msgcallapp.model.INMSGPacket;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.ArrayList;
import java.util.List;

public class MessagingWebSocketHandler extends TextWebSocketHandler {
    private List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            // Chiffrement du message avant de l'envoyer
            String encryptedMessage = AESEncryption.encrypt(message.getPayload());
            INMSGPacket packet = new INMSGPacket("1.0", 256, "session123", "0x03", "SUCCESS", encryptedMessage);

            for (WebSocketSession webSocketSession : sessions) {
                webSocketSession.sendMessage(new TextMessage("Message chiffré envoyé : " + packet.getData()));
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage("Erreur lors du chiffrement du message."));
        }
    }
}