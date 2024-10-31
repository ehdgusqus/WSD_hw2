package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class ShopManager {
    private final Scanner scanner = new Scanner(System.in);
    private ArrayList<ShopInform> shopList = new ArrayList<>();
    private final ShopCRUD shopCRUD = new ShopCRUD();
    private final DBUtil dbUtil = new DBUtil();

    public void start() {
        dbUtil.createTable();
        loadFromDatabase();
        printMenu();
    }

    private void loadFromDatabase() {
        shopList = dbUtil.loadItems();
        System.out.println("Database loaded. Total items: " + shopList.size());
    }

    public void printMenu() {
        int menu;
        while (true) {
            System.out.print("1.List  2.List(price)  3.Search  4.Add  5.Modify  6.Delete  7.Save file 0.Exit ");
            menu = scanner.nextInt();
            scanner.nextLine();  // 입력 버퍼 클리어

            if (menu == 0) {
                System.out.println("시스템이 종료되었습니다.");
                break;
            }

            switch (menu) {
                case 1 -> shopCRUD.list(shopList);
                case 2 -> shopCRUD.listPrice(shopList);
                case 3 -> shopCRUD.search(shopList);
                case 4 -> addItem();
                case 5 -> modifyItem();
                case 6 -> deleteItem();
                case 7 -> saveToFile();
                default -> System.out.println("올바른 번호를 입력하세요");
            }
        }
    }

    private void addItem() {
        ShopInform item = shopCRUD.add();
        ShopInform dbItem = dbUtil.insertItem(item.getName(), item.getDescription(), item.getPrice(), item.getQuantity(), item.getCategory());
        shopList.add(dbItem);
        System.out.println("Item added.");
    }

    private void modifyItem() {
        System.out.print("수정할 제품의 아이디를 입력하세요: ");
        int id = scanner.nextInt();

        ShopInform item = findById(id);
        if (item != null) {
            System.out.println("Modify Shop");

            ShopInform updatedInfo = shopCRUD.printShop();

            item.setName(updatedInfo.getName());
            item.setDescription(updatedInfo.getDescription());
            item.setPrice(updatedInfo.getPrice());
            item.setQuantity(updatedInfo.getQuantity());
            item.setCategory(updatedInfo.getCategory());

            // DB에서도 업데이트 수행
            dbUtil.updateItem(item);
            System.out.println("Item updated with current timestamp.");
        } else {
            System.out.println("해당 아이디의 제품을 찾을 수 없습니다.");
        }
    }



    private void deleteItem() {
        System.out.print("삭제할 제품의 아이디를 입력하세요: ");
        int id = scanner.nextInt();
        boolean removed = shopCRUD.delete(shopList, id);
        if (removed) {
            dbUtil.deleteItem(id);
            System.out.println("Item deleted.");
        } else {
            System.out.println("DB에서 아이디를 찾을 수 없습니다.");
        }
    }

    private void saveToFile() {
        StringBuilder data = new StringBuilder();
        for (ShopInform item : shopList) {
            data.append(item.toString()).append("\n");
        }
        dbUtil.saveDataToFile(data.toString());
    }
    // id로 ArrayList에서 아이템을 찾는 메서드
    private ShopInform findById(int id) {
        for (ShopInform item : shopList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
