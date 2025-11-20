package administrative_services.onboarding_manager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class OnboardingService {

    // in-memory
    private int nextEmployeeId = 1;

    private final Map<Integer, Employee> employees = new LinkedHashMap<>();
    private final Map<Integer, Checklist> checklistsByEmp = new HashMap<>();
    private final Map<UUID, List<ChecklistItem>> itemsByChecklist = new HashMap<>();
    private final Map<Integer, List<Orientation>> orientationsByEmp = new HashMap<>();

    // ONE data directory - used everywhere
    private static final String DATA_DIR = "hospitalSystem/src/administrative_services/data/";

    // Constructor - load existing data on startup
    public OnboardingService() {
        try {
            CsvSnapshot.loadAll(this, DATA_DIR);
            System.out.println("Loaded " + employees.size() + " employees");
        } catch (Exception e) {
            System.out.println("Starting fresh - no existing data found");
        }
    }

    // create employee + checklist + default items
    public Employee createEmployee(String name, String email, LocalDate startDate) {
        int id = nextEmployeeId++;
        Employee e = new Employee(id, name, email, startDate);
        employees.put(id, e);

        Checklist cl = new Checklist(UUID.randomUUID(), id);
        checklistsByEmp.put(id, cl);
        itemsByChecklist.put(cl.getId(), new ArrayList<>());
        addDefaultItems(cl, startDate);

        System.out.println("Created employee id=" + id);
        save(); // Save using CsvSnapshot
        return e;
    }

    private void addDefaultItems(Checklist cl, LocalDate startDate) {
        addItem(cl, ItemCode.I9, startDate);
        addItem(cl, ItemCode.W4, startDate);
        addItem(cl, ItemCode.DIRECT_DEPOSIT, startDate.plusDays(3));
        addItem(cl, ItemCode.TRAINING, startDate.plusWeeks(2));
        addItem(cl, ItemCode.ORIENTATION, startDate);
    }

    private void addItem(Checklist cl, ItemCode code, LocalDate due) {
        ChecklistItem it = new ChecklistItem(UUID.randomUUID(), cl.getId(), code, due);
        itemsByChecklist.get(cl.getId()).add(it);
    }

    // queries
    public List<Employee> listEmployees() {
        return new ArrayList<>(employees.values());
    }

    public Employee getEmployee(int employeeId) {
        return employees.get(employeeId);
    }

    public Checklist getChecklistForEmployee(int employeeId) {
        Checklist cl = checklistsByEmp.get(employeeId);
        if (cl == null) {
            throw new NoSuchElementException("No checklist for employee " + employeeId);
        }
        return cl;
    }

    public List<ChecklistItem> listItems(int employeeId) {
        Checklist cl = getChecklistForEmployee(employeeId);
        return new ArrayList<>(itemsByChecklist.getOrDefault(cl.getId(), Collections.emptyList()));
    }

    public List<Orientation> listOrientations(int employeeId) {
        return new ArrayList<>(orientationsByEmp.getOrDefault(employeeId, Collections.emptyList()));
    }

    // actions
    public void markItemDone(int employeeId, ItemCode code, LocalDate date) {
        Checklist cl = getChecklistForEmployee(employeeId);
        ChecklistItem it = findItem(cl.getId(), code);
        it.markDone(date);
        save();
    }

    public Orientation scheduleOrientation(int employeeId, LocalDateTime when, String location, boolean placeholder) {
        Orientation o = new Orientation(UUID.randomUUID(), employeeId, when, location, placeholder);
        orientationsByEmp.computeIfAbsent(employeeId, k -> new ArrayList<>()).add(o);
        if (!placeholder) {
            markItemDone(employeeId, ItemCode.ORIENTATION, when.toLocalDate());
        } else {
            save();
        }
        return o;
    }

    public boolean isReady(int employeeId) {
        Employee e = employees.get(employeeId);
        if (e == null) {
            throw new NoSuchElementException("Employee not found: " + employeeId);
        }
        Checklist cl = getChecklistForEmployee(employeeId);
        boolean allDone = itemsByChecklist.getOrDefault(cl.getId(), Collections.emptyList())
                .stream().allMatch(ChecklistItem::isDone);
        return allDone && !LocalDate.now().isBefore(e.getStartDate());
    }

    private ChecklistItem findItem(UUID checklistId, ItemCode code) {
        for (ChecklistItem it : itemsByChecklist.getOrDefault(checklistId, Collections.emptyList())) {
            if (it.getCode() == code) {
                return it;
            }
        }
        throw new NoSuchElementException("Checklist item not found: " + code);
    }

    // loaders (used by CsvSnapshot)
    public void clearAll() {
        nextEmployeeId = 1;
        employees.clear();
        checklistsByEmp.clear();
        itemsByChecklist.clear();
        orientationsByEmp.clear();
    }

    public void loadEmployee(Employee e) {
        employees.put(e.getId(), e);
        if (e.getId() >= nextEmployeeId) {
            nextEmployeeId = e.getId() + 1;
        }
    }

    public void loadChecklist(Checklist cl) {
        checklistsByEmp.put(cl.getEmployeeId(), cl);
        itemsByChecklist.putIfAbsent(cl.getId(), new ArrayList<>());
    }

    public void loadChecklistItem(ChecklistItem it) {
        itemsByChecklist.computeIfAbsent(it.getChecklistId(), k -> new ArrayList<>()).add(it);
    }

    public void loadOrientation(Orientation o) {
        orientationsByEmp.computeIfAbsent(o.getEmployeeId(), k -> new ArrayList<>()).add(o);
    }

    // ONE save method - uses CsvSnapshot only
    public void save() {
        try {
            CsvSnapshot.saveAll(this, DATA_DIR);
        } catch (Exception e) {
            System.err.println("Warning: Could not save: " + e.getMessage());
        }
    }

    // Reload from disk
    public void reload() {
        try {
            CsvSnapshot.loadAll(this, DATA_DIR);
        } catch (Exception e) {
            System.err.println("Warning: Could not reload: " + e.getMessage());
        }
    }

    // Get the data directory (for other managers to use same location)
    public static String getDataDir() {
        return DATA_DIR;
    }
}