package ro.websocket.server.rest.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.websocket.server.commons.UserRole;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InMemoryUsers {

    private final Map<String, User> IN_MEMORY_USERS;

    public InMemoryUsers(PasswordEncoder passwordEncoder) {
        IN_MEMORY_USERS = Collections.unmodifiableMap(Map.of(
                "user1", new User("user1", passwordEncoder.encode("user1"), List.of(UserRole.USER::getRoleWithPrefix)),
                "user2", new User("user2", passwordEncoder.encode("user2"), List.of(UserRole.USER::getRoleWithPrefix)),
                "notifier", new User("notifier", passwordEncoder.encode("notifier"), List.of(UserRole.NOTIFIER::getRoleWithPrefix))
        ));
    }

    public List<User> getInMemoryUsers() {
        return IN_MEMORY_USERS.keySet().stream().map(IN_MEMORY_USERS::get).toList();
    }
}
