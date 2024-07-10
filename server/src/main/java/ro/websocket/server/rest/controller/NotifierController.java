package ro.websocket.server.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.websocket.server.model.Payload;

@Controller
@RequestMapping("/notifier")
@AllArgsConstructor
public class NotifierController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/hello-all")
    public ResponseEntity<String> sendHelloToEverybody(@RequestBody Payload payload) {
        simpMessagingTemplate.convertAndSend("/all/hello-all", payload.getMessage());
        return ResponseEntity.ok("Send");
    }

    @PostMapping("/hello-user")
    public ResponseEntity<String> sendHelloToUser(@RequestBody Payload payload) {
        simpMessagingTemplate.convertAndSendToUser(payload.getUserId(), "hello", payload.getMessage());
        return ResponseEntity.ok("Send");
    }

}
