package repository;

import model.FoodItem;
import model.Meal;
import model.Drink;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemRepository {

    public void save(FoodItem item) {
        String sql =
                "INSERT INTO food_items (name, price, type) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setString(3, item.getClass().getSimpleName());

            ps.executeUpdate();

            System.out.println("Saved: " + item.getName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<FoodItem> findAll() {
        List<FoodItem> items = new ArrayList<>();
        String sql = "SELECT * FROM food_items";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String type = rs.getString("type");

                FoodItem item =
                        type.equals("Meal")
                                ? new Meal(0, name, price)
                                : new Drink(0, name, price);

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
}