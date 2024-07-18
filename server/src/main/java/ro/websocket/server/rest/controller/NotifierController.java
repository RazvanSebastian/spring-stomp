package ro.websocket.server.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.websocket.server.model.Notification;
import ro.websocket.server.rest.security.InMemoryUsers;

import java.util.List;

@Controller
@RequestMapping("/notifier")
@AllArgsConstructor
public class NotifierController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final InMemoryUsers inMemoryUsers;

    @PostMapping("/all-notification")
    public ResponseEntity<String> sendHelloToEverybody(@RequestBody Notification notification) {
        simpMessagingTemplate.convertAndSend("/all/notification", notification.getMessage());
        return ResponseEntity.ok("Send");
    }

    @PostMapping("/user-notification")
    public ResponseEntity<String> sendHelloToUser(@RequestBody Notification notification) {
        simpMessagingTemplate.convertAndSendToUser(notification.getUserId(), "notification", notification.getMessage());
        return ResponseEntity.ok("Send");
    }

    @GetMapping("/users")
    public ResponseEntity<List<String>> getUsers() {
        return ResponseEntity.ok(inMemoryUsers.getInMemoryUsers().stream().map(User::getUsername).toList());
    }

}
