package administrative_services.security_manager;

import administrative_services.onboarding_manager.Employee;
import administrative_services.onboarding_manager.OnboardingService;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AccessManager {
    private OnboardingService onboardingService;
    private HashMap<String, Badge> badges;
    private HashMap<Integer, List<Access>> accessMap;
    private AuditLog auditLog;

    private static final String DATA_DIR = "hospitalSystem/src/administrative_services/data/";
    private static final String BADGES_FILE = DATA_DIR + "badges.csv";
    private static final String ACCESS_FILE = DATA_DIR + "access.csv";

    public AccessManager(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
        this.badges = new HashMap<>();
        this.accessMap = new HashMap<>();
        this.auditLog = new AuditLog();

        File dir = new File(DATA_DIR);
        if (!dir.exists())
            dir.mkdirs();

        loadBadges();
        loadAccess();
    }

    public Employee getEmployee(int id) {
        for (Employee e : onboardingService.listEmployees()) {
            if (e.getId() == id)
                return e;
        }
        return null;
    }

    public Badge getEmployeeBadge(int employeeId) {
        for (Badge b : badges.values()) {
            if (b.getEmployeeId() == employeeId && b.isActive())
                return b;
        }
        return null;
    }

    public Badge assignBadge(int employeeId, Role role, String issuedBy) throws ValidationException {
        Employee emp = getEmployee(employeeId);
        if (emp == null) {
            throw new ValidationException("Employee not found");
        }

        if (getEmployeeBadge(employeeId) != null) {
            throw new ValidationException("Already has a badge");
        }

        String badgeId = "BDG-" + UUID.randomUUID().toString().substring(0, 8);
        Badge badge = new Badge(badgeId, employeeId, role, issuedBy);
        badge.activate();
        badges.put(badgeId, badge);

        grantRoleAccess(employeeId, role, issuedBy);
        save();
        auditLog.logBadgeChange(employeeId, badgeId, "ISSUED - " + role.getDisplayName());
        return badge;
    }

    public void changeRole(int employeeId, Role newRole, String changedBy) throws ValidationException {
        if (getEmployee(employeeId) == null) {
            throw new ValidationException("Employee not found");
        }

        Badge badge = getEmployeeBadge(employeeId);
        if (badge == null) {
            throw new ValidationException("No active badge");
        }

        badge.setRole(newRole);
        revokeAllAccess(employeeId);
        grantRoleAccess(employeeId, newRole, changedBy);
        save();
        auditLog.logInfo("Role changed", "Employee " + employeeId + " -> " + newRole);
    }

    private void grantRoleAccess(int employeeId, Role role, String grantedBy) {
        List<Access> list = accessMap.get(employeeId);
        if (list == null) {
            list = new ArrayList<>();
            accessMap.put(employeeId, list);
        }

        for (String resource : role.getDefaultAccess()) {
            String accessId = "ACC-" + UUID.randomUUID().toString().substring(0, 8);
            Access access = new Access(accessId, employeeId, resource,
                    Access.AccessType.SYSTEM, Access.AccessLevel.READ_WRITE, grantedBy);
            access.activate();
            list.add(access);
        }
    }

    private void revokeAllAccess(int employeeId) {
        List<Access> list = accessMap.get(employeeId);
        if (list == null)
            return;

        for (Access a : list) {
            if (a.isActive())
                a.revoke();
        }
    }

    public void processLastDay(int employeeId, String processedBy, boolean itemsMissing) {
        // revoke badge
        for (Badge b : badges.values()) {
            if (b.getEmployeeId() == employeeId && b.isActive()) {
                b.revoke();
                auditLog.logBadgeChange(employeeId, b.getBadgeId(), "REVOKED");
            }
        }

        // revoke access
        revokeAllAccess(employeeId);

        // mark terminated
        Employee emp = getEmployee(employeeId);
        if (emp != null) {
            emp.setStatus("TERMINATED");
            onboardingService.save();
        }

        save();
        auditLog.logInfo("Terminated", "Employee " + employeeId);
    }

    // load/save
    private void loadBadges() {
        File file = new File(BADGES_FILE);
        if (!file.exists())
            return;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = r.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] p = line.split(",");
                if (p.length >= 5) {
                    Badge b = new Badge(p[0], Integer.parseInt(p[1].trim()),
                            Role.valueOf(p[2].trim()), p[4]);
                    b.setStatus(Badge.BadgeStatus.valueOf(p[3]));
                    if (p.length > 5 && !p[5].isEmpty()) {
                        b.setIssuedDate(LocalDateTime.parse(p[5]));
                    }
                    if (p.length > 6 && !p[6].isEmpty()) {
                        b.setRevokedDate(LocalDateTime.parse(p[6]));
                    }
                    badges.put(p[0], b);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading badges");
        }
    }

    private void loadAccess() {
        File file = new File(ACCESS_FILE);
        if (!file.exists())
            return;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = r.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] p = line.split(",");
                if (p.length >= 6) {
                    int empId = Integer.parseInt(p[1].trim());
                    Access a = new Access(p[0], empId, p[2],
                            Access.AccessType.valueOf(p[3]),
                            Access.AccessLevel.valueOf(p[4]),
                            p.length > 7 ? p[7] : "System");
                    a.setStatus(Access.AccessStatus.valueOf(p[5]));

                    List<Access> list = accessMap.get(empId);
                    if (list == null) {
                        list = new ArrayList<>();
                        accessMap.put(empId, list);
                    }
                    list.add(a);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading access");
        }
    }

    private void save() {
        saveBadges();
        saveAccess();
        auditLog.save();
    }

    private void saveBadges() {
        try (PrintWriter w = new PrintWriter(new FileWriter(BADGES_FILE))) {
            w.println("BadgeID,EmployeeID,Role,Status,IssuedBy,IssuedDate,RevokedDate");
            for (Badge b : badges.values()) {
                w.println(b.getBadgeId() + "," + b.getEmployeeId() + "," +
                        b.getRole().name() + "," + b.getStatus() + "," + b.getIssuedBy() + "," +
                        b.getIssuedDate() + "," +
                        (b.getRevokedDate() != null ? b.getRevokedDate() : ""));
            }
        } catch (IOException e) {
            System.err.println("Error saving badges");
        }
    }

    private void saveAccess() {
        try (PrintWriter w = new PrintWriter(new FileWriter(ACCESS_FILE))) {
            w.println("AccessID,EmployeeID,Resource,Type,Level,Status,GrantedDate,GrantedBy,RevokedDate");
            for (List<Access> list : accessMap.values()) {
                for (Access a : list) {
                    w.println(a.getAccessId() + "," + a.getEmployeeId() + "," +
                            a.getResourceName() + "," + a.getType() + "," + a.getLevel() + "," +
                            a.getStatus() + "," + a.getGrantedDate() + "," + a.getGrantedBy() + "," +
                            (a.getRevokedDate() != null ? a.getRevokedDate() : ""));
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving access");
        }
    }

    // getters
    public HashMap<String, Badge> getBadges() {
        return badges;
    }

    public HashMap<Integer, List<Access>> getAccessMap() {
        return accessMap;
    }

    public AuditLog getAuditLog() {
        return auditLog;
    }

    public static class ValidationException extends Exception {
        public ValidationException(String msg) {
            super(msg);
        }
    }
}