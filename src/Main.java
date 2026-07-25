import java.util.List;
import java.util.Scanner;

public class Main {
    private static final CandidateDAO dao = new CandidateDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addCandidate();
                case "2" -> listAllCandidates();
                case "3" -> searchBySkill();
                case "4" -> updateStatus();
                case "5" -> deleteCandidate();
                case "6" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option, try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== CANDIDATE TRACKER =====");
        System.out.println("1. Add candidate");
        System.out.println("2. List all candidates");
        System.out.println("3. Search by skill");
        System.out.println("4. Update candidate status");
        System.out.println("5. Delete candidate");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static void addCandidate() {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format. Candidate not added.");
            return;
        }
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty. Candidate not added.");
            return;
        }

        System.out.print("Skills (comma separated): ");
        String skills = scanner.nextLine().trim();

        Candidate c = new Candidate(name, email, skills, Status.NEW);
        dao.addCandidate(c);
    }

    private static void listAllCandidates() {
        List<Candidate> candidates = dao.getAllCandidates();
        if (candidates.isEmpty()) {
            System.out.println("No candidates found.");
            return;
        }
        candidates.forEach(System.out::println);
    }

    private static void searchBySkill() {
        System.out.print("Enter skill to search for: ");
        String skill = scanner.nextLine().trim();

        List<Candidate> results = dao.searchBySkill(skill);
        if (results.isEmpty()) {
            System.out.println("No candidates found with that skill.");
            return;
        }
        results.forEach(System.out::println);
    }

    private static void updateStatus() {
        System.out.print("Candidate email: ");
        String email = scanner.nextLine().trim();

        System.out.print("New status (NEW, SCREENING, INTERVIEWING, HIRED, REJECTED): ");
        String statusInput = scanner.nextLine().trim().toUpperCase();

        try {
            Status newStatus = Status.valueOf(statusInput);
            boolean updated = dao.updateStatus(email, newStatus);
            System.out.println(updated ? "Status updated." : "No candidate found with that email.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status value.");
        }
    }

    private static void deleteCandidate() {
        System.out.print("Candidate email to delete: ");
        String email = scanner.nextLine().trim();

        boolean deleted = dao.deleteByEmail(email);
        System.out.println(deleted ? "Candidate deleted." : "No candidate found with that email.");
    }

    // Simple email format check — not perfect, but good enough to catch obvious typos
    private static boolean isValidEmail(String email) {
        return email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    }
}