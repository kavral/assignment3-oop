package repository;

import model.FoodItem;
import model.Meal;
import model.Drink;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemRepository {

    public int save(FoodItem item) {
        String sql =
                "INSERT INTO food_items (name, price, type) VALUES (?, ?, ?)";

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
                        System.out.println("Saved: " + item.getName() + " (ID: " + id + ")");
                        return id;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<FoodItem> findAll() {
        List<FoodItem> items = new ArrayList<>();
        String sql = "SELECT * FROM food_items";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String type = rs.getString("type");

                FoodItem item =
                        type.equals("Meal")
                                ? new Meal(id, name, price)
                                : new Drink(id, name, price);

                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

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

    public FoodItem findById(int id) {
        String sql = "SELECT * FROM food_items WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String type = rs.getString("type");

                return type.equals("Meal")
                        ? new Meal(id, name, price)
                        : new Drink(id, name, price);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}