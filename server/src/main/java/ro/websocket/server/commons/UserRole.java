package ro.websocket.server.commons;

import lombok.Getter;

@Getter
public enum UserRole {

    USER("USER"),
    NOTIFIER("NOTIFIER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRoleWithPrefix() {
        return "ROLE_" + role;
    }
}
