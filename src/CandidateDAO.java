import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO = Data Access Object. This class is the ONLY place that talks to the database.
// Everything else in the app works with Candidate objects, not SQL.
public class CandidateDAO {

    public void addCandidate(Candidate c) {
        String sql = "INSERT INTO candidates (name, email, skills, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getSkills());
            stmt.setString(4, c.getStatus().name());
            stmt.executeUpdate();

            System.out.println("Candidate added successfully.");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: A candidate with that email already exists.");
        } catch (SQLException e) {
            System.out.println("Database error while adding candidate: " + e.getMessage());
        }
    }

    public List<Candidate> getAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "SELECT * FROM candidates ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                candidates.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Database error while fetching candidates: " + e.getMessage());
        }
        return candidates;
    }

    public List<Candidate> searchBySkill(String skill) {
        List<Candidate> results = new ArrayList<>();
        String sql = "SELECT * FROM candidates WHERE skills LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + skill + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Database error while searching: " + e.getMessage());
        }
        return results;
    }

    public boolean updateStatus(String email, Status newStatus) {
        String sql = "UPDATE candidates SET status = ? WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus.name());
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database error while updating status: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteByEmail(String email) {
        String sql = "DELETE FROM candidates WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database error while deleting: " + e.getMessage());
            return false;
        }
    }

    // Helper: converts one row of a ResultSet into a Candidate object.
    // Pulled out into its own method because getAllCandidates() and searchBySkill()
    // both need it — avoids repeating the same 5 lines twice.
    private Candidate mapRow(ResultSet rs) throws SQLException {
        return new Candidate(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("skills"),
                Status.valueOf(rs.getString("status"))
        );
    }
}