package org.example;

public class Main {
    public static void main(String[] args) {
        DBUtil dbUtil = new DBUtil();

        // ShopManager 시작
        ShopManager manager = new ShopManager();
        manager.start();
    }
}
