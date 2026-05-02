public abstract class Staff {
    private String staffId;
    private String fullName;
    private String role;

    public Staff(String staffId, String fullName, String role) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.role = role;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }
}
