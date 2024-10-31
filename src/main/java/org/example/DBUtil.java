package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DBUtil {
    private static final String URL = "jdbc:sqlite:mydata.db";

    // 데이터베이스 연결 생성 메서드
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
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
    public ShopInform insertItem(String name, String description, int price, int quantity, String category) {
        String sql = "INSERT INTO shopping_list (name, description, price, quantity, category) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;
        String createDate = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, price);
            pstmt.setInt(4, quantity);
            pstmt.setString(5, category);
            pstmt.executeUpdate();

            // 생성된 ID와 create_date 가져오기
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                }
            }

            String selectDateSQL = "SELECT create_date FROM shopping_list WHERE id = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectDateSQL)) {
                selectStmt.setInt(1, generatedId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        createDate = rs.getString("create_date");
                    }
                }
            }
            System.out.println("Item inserted successfully with ID: " + generatedId);
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
        return new ShopInform(generatedId, name, description, price, quantity, category, createDate);
    }

    // 데이터 수정 메서드
    public void updateItem(ShopInform item) {
        String sql = "UPDATE shopping_list SET name = ?, description = ?, price = ?, quantity = ?, category = ?, create_date = DATETIME('now', 'localtime') WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setInt(3, item.getPrice());
            pstmt.setInt(4, item.getQuantity());
            pstmt.setString(5, item.getCategory());
            pstmt.setInt(6, item.getId());
            pstmt.executeUpdate();
            System.out.println("Item updated successfully with new timestamp.");
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
            System.out.println("Item deleted.");
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    // 최신 create_date 값을 가져오는 메서드
    public String getLatestCreateDate() {
        String createDate = "";
        String sql = "SELECT create_date FROM shopping_list ORDER BY id DESC LIMIT 1";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
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

    // 테이블 초기화 메서드
    public void resetTable() {
        String dropTableSQL = "DROP TABLE IF EXISTS shopping_list";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS shopping_list (" +
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
            stmt.execute(dropTableSQL);
            System.out.println("Table 'shopping_list' dropped.");

            stmt.execute(createTableSQL);
            System.out.println("Table 'shopping_list' created.");
        } catch (SQLException e) {
            System.out.println("Error resetting table: " + e.getMessage());
        }
    }

    // 아이디로 항목 로드하는 메서드
    public ShopInform loadItemById(int id) {
        String sql = "SELECT * FROM shopping_list WHERE id = ?";
        ShopInform item = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    item = new ShopInform(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("price"),
                            rs.getInt("quantity"),
                            rs.getString("category"),
                            rs.getString("create_date")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Load by ID failed: " + e.getMessage());
        }
        return item;
    }

    // 전체 데이터를 로드하는 메서드
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

    // 가격 기준으로 정렬된 데이터 로드 메서드
    public ArrayList<ShopInform> loadItemsSortedByPrice() {
        ArrayList<ShopInform> items = new ArrayList<>();
        String sql = "SELECT * FROM shopping_list ORDER BY price ASC";
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
            System.out.println("Load by price failed: " + e.getMessage());
        }
        return items;
    }
}
