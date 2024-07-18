package ro.websocket.server.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ro.websocket.server.commons.UserDetails;

@Controller
public class SecurityController {

    @GetMapping("/csrf-token")
    public ResponseEntity<String> getCsrfToken(CsrfToken token) {
        return ResponseEntity.ok(token.getToken());
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<UserDetails> isAuthenticated() {
        return ResponseEntity.ok(new UserDetails(SecurityContextHolder.getContext().getAuthentication()));
    }

}
