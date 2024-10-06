package car.homework.msgcallapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new car.homework.msgcallapp.service.MessagingWebSocketHandler(), "/messaging");
//                .setAllowedOrigins("*");
    }

    // Définition du gestionnaire WebSocket
    public class WebSocketHandler extends TextWebSocketHandler {

        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            System.out.println("Message received: " + message.getPayload());
            session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
        }
    }
}