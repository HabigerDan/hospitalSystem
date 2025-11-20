package waste_disposal_manager;

public class wasteType {
    
    private String typeName;
    private String category;
    
    public wasteType(String typeName, String category) {
        this.category = category;
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}