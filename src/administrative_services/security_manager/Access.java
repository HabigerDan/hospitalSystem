package administrative_services.security_manager;

import java.time.LocalDateTime;

public class Access {
    private String accessId;
    private int employeeId;
    private String resourceName;
    private AccessType type;
    private AccessLevel level;
    private AccessStatus status;
    private LocalDateTime grantedDate;
    private LocalDateTime revokedDate;
    private String grantedBy;

    public enum AccessType {
        SYSTEM,
        APPLICATION,
        BUILDING,
        ROOM,
        DATABASE,
        NETWORK
    }

    public enum AccessLevel {
        READ_ONLY,
        READ_WRITE,
        ADMIN,
        FULL_ACCESS
    }

    public enum AccessStatus {
        ACTIVE,
        PENDING,
        REVOKED,
        EXPIRED,
        ERROR
    }

    public Access(String accessId, int employeeId, String resourceName,
            AccessType type, AccessLevel level, String grantedBy) {
        this.accessId = accessId;
        this.employeeId = employeeId;
        this.resourceName = resourceName;
        this.type = type;
        this.level = level;
        this.status = AccessStatus.PENDING;
        this.grantedDate = LocalDateTime.now();
        this.grantedBy = grantedBy;
    }

    public void activate() {
        this.status = AccessStatus.ACTIVE;
    }

    public void revoke() {
        this.status = AccessStatus.REVOKED;
        this.revokedDate = LocalDateTime.now();
    }

    public boolean isActive() {
        return status == AccessStatus.ACTIVE;
    }

    public boolean isPending() {
        return status == AccessStatus.PENDING;
    }

    // Getters
    public String getAccessId() {
        return accessId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public AccessType getType() {
        return type;
    }

    public AccessLevel getLevel() {
        return level;
    }

    public AccessStatus getStatus() {
        return status;
    }

    public LocalDateTime getGrantedDate() {
        return grantedDate;
    }

    public LocalDateTime getRevokedDate() {
        return revokedDate;
    }

    public String getGrantedBy() {
        return grantedBy;
    }

    // Setters
    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setType(AccessType type) {
        this.type = type;
    }

    public void setLevel(AccessLevel level) {
        this.level = level;
    }

    public void setStatus(AccessStatus status) {
        this.status = status;
    }

    public void setGrantedDate(LocalDateTime grantedDate) {
        this.grantedDate = grantedDate;
    }

    public void setRevokedDate(LocalDateTime revokedDate) {
        this.revokedDate = revokedDate;
    }

    public void setGrantedBy(String grantedBy) {
        this.grantedBy = grantedBy;
    }
}