package repository;

import model.Drink;
import model.FoodItem;
import model.Meal;
import org.springframework.stereotype.Repository;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class FoodItemRepositoryImpl implements FoodItemRepository {

    @Override
    public FoodItem save(FoodItem item) {
        String sql = "INSERT INTO food_items (name, price, type) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setString(3, item.getClass().getSimpleName());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        item.setId(id);
                        return item;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FoodItem> findAll() {
        List<FoodItem> items = new ArrayList<>();
        String sql = "SELECT * FROM food_items";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public List<FoodItem> findAllSortedByName() {
        List<FoodItem> items = findAll();
        items.sort(Comparator.comparing(FoodItem::getName, String.CASE_INSENSITIVE_ORDER));
        return items;
    }

    @Override
    public void updatePrice(String name, double newPrice) {
        String sql = "UPDATE food_items SET price = ? WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newPrice);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByName(String name) {
        String sql = "DELETE FROM food_items WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM food_items WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FoodItem findById(int id) {
        String sql = "SELECT * FROM food_items WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FoodItem mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        String type = rs.getString("type");

        return "Meal".equals(type)
                ? new Meal(id, name, price)
                : new Drink(id, name, price);
    }
}

