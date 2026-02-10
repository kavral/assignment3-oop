package repository;

import model.Offer;
import org.springframework.stereotype.Repository;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OfferRepositoryImpl implements OfferRepository {

    @Override
    public void save(Offer offer) {
        String sql = "INSERT INTO offers (food_item_id, discount_percentage, description, start_date, end_date, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, offer.getFoodItemId());
            ps.setDouble(2, offer.getDiscountPercentage());
            ps.setString(3, offer.getDescription());
            ps.setDate(4, Date.valueOf(offer.getStartDate()));
            ps.setDate(5, Date.valueOf(offer.getEndDate()));
            ps.setBoolean(6, offer.isActive());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        offer.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Offer> findAll() {
        List<Offer> offers = new ArrayList<>();
        String sql = "SELECT * FROM offers ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                offers.add(mapResultSetToOffer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offers;
    }

    @Override
    public List<Offer> findByFoodItemId(int foodItemId) {
        List<Offer> offers = new ArrayList<>();
        String sql = "SELECT * FROM offers WHERE food_item_id = ? ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, foodItemId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                offers.add(mapResultSetToOffer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offers;
    }

    @Override
    public List<Offer> findActiveOffers() {
        List<Offer> offers = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String sql = "SELECT * FROM offers WHERE is_active = TRUE " +
                "AND start_date <= ? AND end_date >= ? ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(today));
            ps.setDate(2, Date.valueOf(today));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                offers.add(mapResultSetToOffer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offers;
    }

    @Override
    public Offer findById(int id) {
        String sql = "SELECT * FROM offers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToOffer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Offer offer) {
        String sql = "UPDATE offers SET food_item_id = ?, discount_percentage = ?, description = ?, " +
                "start_date = ?, end_date = ?, is_active = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, offer.getFoodItemId());
            ps.setDouble(2, offer.getDiscountPercentage());
            ps.setString(3, offer.getDescription());
            ps.setDate(4, Date.valueOf(offer.getStartDate()));
            ps.setDate(5, Date.valueOf(offer.getEndDate()));
            ps.setBoolean(6, offer.isActive());
            ps.setInt(7, offer.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM offers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deactivateById(int id) {
        String sql = "UPDATE offers SET is_active = FALSE WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Offer mapResultSetToOffer(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int foodItemId = rs.getInt("food_item_id");
        double discountPercentage = rs.getDouble("discount_percentage");
        String description = rs.getString("description");
        LocalDate startDate = rs.getDate("start_date").toLocalDate();
        LocalDate endDate = rs.getDate("end_date").toLocalDate();
        boolean isActive = rs.getBoolean("is_active");

        return new Offer(id, foodItemId, discountPercentage, description, startDate, endDate, isActive);
    }
}

