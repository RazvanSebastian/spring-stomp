package ro.websocket.server.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetails {
    private String username;
    private Collection<String> roles;

    public UserDetails(Authentication authentication) {
        this.username = authentication.getName();
        this.roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
