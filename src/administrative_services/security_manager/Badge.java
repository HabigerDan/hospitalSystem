package administrative_services.security_manager;

import java.time.LocalDateTime;

public class Badge {
    private String badgeId;
    private int employeeId;
    private Role role;
    private BadgeStatus status;
    private LocalDateTime issuedDate;
    private LocalDateTime revokedDate;
    private String issuedBy;

    public enum BadgeStatus {
        ACTIVE,
        INACTIVE,
        REVOKED,
        LOST,
        PENDING
    }

    public Badge(String badgeId, int employeeId, Role role, String issuedBy) {
        this.badgeId = badgeId;
        this.employeeId = employeeId;
        this.role = role;
        this.status = BadgeStatus.PENDING;
        this.issuedDate = LocalDateTime.now();
        this.issuedBy = issuedBy;
    }

    public void activate() {
        this.status = BadgeStatus.ACTIVE;
    }

    public void revoke() {
        this.status = BadgeStatus.REVOKED;
        this.revokedDate = LocalDateTime.now();
    }

    public void markLost() {
        this.status = BadgeStatus.LOST;
    }

    public boolean isActive() {
        return status == BadgeStatus.ACTIVE;
    }

    // Getters
    public String getBadgeId() {
        return badgeId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public Role getRole() {
        return role;
    }

    public BadgeStatus getStatus() {
        return status;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public LocalDateTime getRevokedDate() {
        return revokedDate;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    // Setters
    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setStatus(BadgeStatus status) {
        this.status = status;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public void setRevokedDate(LocalDateTime revokedDate) {
        this.revokedDate = revokedDate;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }
}