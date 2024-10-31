package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBUtil {
    private static final String URL = "jdbc:sqlite:mydata.db"; // SQLite 파일 경로 설정

    // 데이터베이스 연결 생성 메서드
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite established.");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    // 테이블 생성 메서드
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS shopping_list (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "price REAL," +
                "quantity INTEGER," +
                "category TEXT," +
                "create_date TEXT DEFAULT (DATETIME('now', 'localtime'))" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'shopping_list' created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // 데이터 삽입 메서드
    public void insertItem(String name, String description, int price, int quantity, String category) {
        String sql = "INSERT INTO shopping_list (name, description, price, quantity, category) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, price);
            pstmt.setInt(4, quantity);
            pstmt.setString(5, category);
            pstmt.executeUpdate();
            System.out.println("Item inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    // 데이터 수정 메서드
    public void updateItem(ShopInform item) {
        String sql = "UPDATE shopping_list SET name = ?, description = ?, price = ?, quantity = ?, category = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setInt(3, item.getPrice());
            pstmt.setInt(4, item.getQuantity());
            pstmt.setString(5, item.getCategory());
            pstmt.setInt(6, item.getId());
            pstmt.executeUpdate();
            System.out.println("Item updated successfully.");
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    // 데이터 삭제 메서드
    public void deleteItem(int id) {
        String sql = "DELETE FROM shopping_list WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Item deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    // 최신 create_date 값을 가져오는 메서드
    public String getLatestCreateDate() {
        String createDate = "";
        String sql = "SELECT create_date FROM shopping_list ORDER BY id DESC LIMIT 1"; // 가장 최신 데이터

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                // 파일 이름 형식에 맞게 create_date 포맷 변경
                createDate = rs.getString("create_date").replace(":", "").replace("-", "").replace(" ", "_");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving date: " + e.getMessage());
        }
        return createDate;
    }

    // 데이터를 파일로 저장하는 메서드
    public void saveDataToFile(String data) {
        String timestamp = getLatestCreateDate();
        String fileName = "data_" + timestamp + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(data);
            System.out.println("Data saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("File save error: " + e.getMessage());
        }
    }

    // 데이터 로드 메서드 (ShopManager에서 호출하여 shopList에 추가할 때 사용)
    public ArrayList<ShopInform> loadItems() {
        ArrayList<ShopInform> items = new ArrayList<>();
        String sql = "SELECT * FROM shopping_list";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ShopInform item = new ShopInform(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price"),
                        rs.getInt("quantity"),
                        rs.getString("category"),
                        rs.getString("create_date")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Load failed: " + e.getMessage());
        }
        return items;
    }
}
