package ro.websocket.server.commons;

public enum UserRole {

    USER("USER"),
    NOTIFIER("NOTIFIER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getRoleWithPrefix() {
        return "ROLE_" + role;
    }
}
