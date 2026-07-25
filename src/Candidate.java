public class Candidate {
    private int id;
    private String name;
    private String email;
    private String skills;
    private Status status;

    // Constructor used when adding a NEW candidate (no id yet, DB will assign it)
    public Candidate(String name, String email, String skills, Status status) {
        this.name = name;
        this.email = email;
        this.skills = skills;
        this.status = status;
    }

    // Constructor used when reading a candidate back FROM the database (id already exists)
    public Candidate(int id, String name, String email, String skills, Status status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.skills = skills;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getSkills() { return skills; }
    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[%d] %-15s | %-25s | %-30s | %s",
                id, name, email, skills, status);
    }
}