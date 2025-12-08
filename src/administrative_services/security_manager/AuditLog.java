package administrative_services.security_manager;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLog {
    private List<LogEntry> entries;
    private static final String DATA_DIR = "src/administrative_services/data/";
    private static final String AUDIT_LOG_CSV = DATA_DIR + "audit_log.csv";

    public enum LogLevel {
        INFO, WARNING, ERROR
    }

    public static class LogEntry {
        private LocalDateTime timestamp;
        private LogLevel level;
        private String action;
        private String details;

        public LogEntry(LogLevel level, String action, String details) {
            this.timestamp = LocalDateTime.now();
            this.level = level;
            this.action = action;
            this.details = details;
        }

        public LogEntry(LocalDateTime timestamp, LogLevel level, String action, String details) {
            this.timestamp = timestamp;
            this.level = level;
            this.action = action;
            this.details = details;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public LogLevel getLevel() {
            return level;
        }

        public String getAction() {
            return action;
        }

        public String getDetails() {
            return details;
        }
    }

    public AuditLog() {
        this.entries = new ArrayList<>();
        ensureDataDirectory();
        load();
    }

    private void ensureDataDirectory() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists())
            dataDir.mkdirs();
    }

    public void logInfo(String action, String details) {
        entries.add(new LogEntry(LogLevel.INFO, action, details));
    }

    public void logWarning(String action, String details) {
        entries.add(new LogEntry(LogLevel.WARNING, action, details));
    }

    public void logError(String action, String details) {
        entries.add(new LogEntry(LogLevel.ERROR, action, details));
    }

    public void logAccessChange(int employeeId, String resource, String action) {
        logInfo("Access Change", "Employee " + employeeId + ": " + resource + " - " + action);
    }

    public void logBadgeChange(int employeeId, String badgeId, String action) {
        logInfo("Badge Change", "Employee " + employeeId + ": " + badgeId + " - " + action);
    }

    public List<LogEntry> getEntries() {
        return entries;
    }

    public List<LogEntry> getRecentEntries(int count) {
        int start = Math.max(0, entries.size() - count);
        return new ArrayList<>(entries.subList(start, entries.size()));
    }

    private void load() {
        File file = new File(AUDIT_LOG_CSV);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] p = line.split(",", 4);
                if (p.length >= 4) {
                    entries.add(new LogEntry(LocalDateTime.parse(p[0]), LogLevel.valueOf(p[1]), p[2], p[3]));
                }
            }
        } catch (Exception e) {
        }
    }

    public void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_LOG_CSV))) {
            writer.println("Timestamp,Level,Action,Details");
            for (LogEntry e : entries) {
                writer.println(e.getTimestamp() + "," + e.getLevel() + "," + e.getAction() + "," + e.getDetails());
            }
        } catch (IOException e) {
            System.out.println("Error saving audit log: " + e.getMessage());
        }
    }
}