package administrative_services.onboarding_manager;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public final class CsvSnapshot {

    private CsvSnapshot() {
    }

    // LOAD
    public static void loadAll(OnboardingService svc, String dataDir) {
        try {
            Path base = Paths.get(dataDir);
            if (!Files.exists(base)) {
                return;
            }

            svc.clearAll();

            // employees.csv id,name,email,startDate
            for (String[] r : read(base.resolve("employees.csv"))) {
                svc.loadEmployee(new Employee(
                        Integer.parseInt(r[0].trim()), r[1], r[2], LocalDate.parse(r[3])));
            }

            // checklists.csv id,employeeId
            for (String[] r : read(base.resolve("checklists.csv"))) {
                svc.loadChecklist(new Checklist(UUID.fromString(r[0]), Integer.parseInt(r[1].trim())));
            }

            // checklist_items.csv id,checklistId,code,dueDate,completedOn
            for (String[] r : read(base.resolve("checklist_items.csv"))) {
                String codeStr = r[2];
                ItemCode code;
                try {
                    code = ItemCode.valueOf(codeStr);
                } catch (Exception ignore) {
                    // skip rows with unknown codes
                    continue;
                }

                LocalDate due = r[3].isEmpty() ? null : LocalDate.parse(r[3]);
                LocalDate done = r.length > 4 && !r[4].isEmpty() ? LocalDate.parse(r[4]) : null;

                ChecklistItem it = new ChecklistItem(
                        UUID.fromString(r[0]),
                        UUID.fromString(r[1]),
                        code,
                        due);
                if (done != null) {
                    it.markDone(done);
                }
                svc.loadChecklistItem(it);
            }

            // orientations.csv id,employeeId,dateTime,location,placeholder
            for (String[] r : read(base.resolve("orientations.csv"))) {
                svc.loadOrientation(new Orientation(
                        UUID.fromString(r[0]),
                        Integer.parseInt(r[1].trim()),
                        LocalDateTime.parse(r[2]),
                        r[3],
                        Boolean.parseBoolean(r[4])));
            }

        } catch (Exception ex) {
            throw new RuntimeException("Load failed: " + ex.getMessage(), ex);
        }
    }

    // SAVE
    public static void saveAll(OnboardingService svc, String dataDir) {
        try {
            Path base = Paths.get(dataDir);
            Files.createDirectories(base);

            // employees.csv
            List<String> empLines = new ArrayList<>();
            for (Employee e : svc.listEmployees()) {
                empLines.add(e.getId() + "," + e.getName() + "," + e.getEmail() + "," +
                        e.getStartDate() + "," + e.getStatus());
            }
            write(base.resolve("employees.csv"), "id,name,email,startDate", empLines);

            // checklists + items
            List<String> clLines = new ArrayList<>();
            List<String> itemLines = new ArrayList<>();
            for (Employee e : svc.listEmployees()) {
                var cl = svc.getChecklistForEmployee(e.getId());
                clLines.add(cl.getId() + "," + e.getId());
                for (ChecklistItem it : svc.listItems(e.getId())) {
                    itemLines.add(it.getId() + "," + cl.getId() + "," + it.getCode().name() + ","
                            + (it.getDueDate() == null ? "" : it.getDueDate())
                            + "," + (it.getCompletedOn() == null ? "" : it.getCompletedOn()));
                }
            }
            write(base.resolve("checklists.csv"), "id,employeeId", clLines);
            write(base.resolve("checklist_items.csv"), "id,checklistId,code,dueDate,completedOn", itemLines);

            // orientations.csv
            List<String> oriLines = new ArrayList<>();
            for (Employee e : svc.listEmployees()) {
                for (Orientation o : svc.listOrientations(e.getId())) {
                    oriLines.add(o.getId() + "," + e.getId() + "," + o.getDateTime() + "," + esc(o.getLocation()) + ","
                            + o.isPlaceholder());
                }
            }
            write(base.resolve("orientations.csv"), "id,employeeId,dateTime,location,placeholder", oriLines);

            System.out.println("Saved snapshot to " + base.toAbsolutePath());
        } catch (Exception ex) {
            throw new RuntimeException("Save failed: " + ex.getMessage(), ex);
        }
    }

    // helpers
    private static List<String[]> read(Path file) throws Exception {
        if (!Files.exists(file)) {
            return List.of();
        }
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<String[]> out = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String s = lines.get(i).trim();
            if (s.isEmpty()) {
                continue;
            }
            out.add(s.split(",", -1));
        }
        return out;
    }

    private static void write(Path file, String header, List<String> lines) throws Exception {
        List<String> out = new ArrayList<>(1 + lines.size());
        out.add(header);
        out.addAll(lines);
        Files.write(file, out, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace(",", " ");
    }
}
