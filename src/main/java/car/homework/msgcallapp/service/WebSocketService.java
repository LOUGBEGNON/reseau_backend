package car.homework.msgcallapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class WebSocketService {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    // Ajouter une session à la liste
    public void registerOpenConnection(WebSocketSession session) {
        sessions.addIfAbsent(session);
    }

    // Retirer une session de la liste
    public void registerClosedConnection(WebSocketSession session) {
        sessions.remove(session);
    }

    // Envoyer des données à toutes les sessions actives
    public boolean sendToAllConnectedSessions(String data) {
        boolean allMessagesSent = true;
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(data));
                } catch (IOException e) {
                    System.err.println("Error sending WebSocket message: " + e.getMessage());
                    allMessagesSent = false;
                }
            }
        }
        return allMessagesSent;
    }

    // Simulez l'envoi du message à travers WebSocket
    public boolean send(String data) {
        System.out.println("Sending data via WebSocket: " + data);
        return true;
    }
}

