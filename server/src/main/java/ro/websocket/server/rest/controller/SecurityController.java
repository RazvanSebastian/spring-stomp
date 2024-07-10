package ro.websocket.server.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/csrf-token")
    public ResponseEntity<String> getCsrfToken(CsrfToken token) {
        return ResponseEntity.ok(token.getToken());
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Void> isAuthenticated() {
        return ResponseEntity.noContent().build();
    }

}
