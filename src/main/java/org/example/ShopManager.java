package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class ShopManager {
    private Scanner scanner = new Scanner(System.in);
    private ArrayList<ShopInform> shopList = new ArrayList<>(); // 모든 데이터 저장
    private ShopCRUD shopCRUD = new ShopCRUD();
    private DBUtil dbUtil = new DBUtil();

    public void start() {
        dbUtil.createTable(); // 프로그램 시작 시 테이블 생성
        loadFromDatabase(); // 데이터베이스에서 기존 데이터 로드
        printMenu(); // 메뉴 출력 시작
    }

    // 데이터베이스에서 데이터를 불러와 shopList에 추가하는 메서드
    private void loadFromDatabase() {
        dbUtil.createTable(); // DBUtil에서 데이터를 불러오는 메서드 구현
        System.out.println("Database loaded. Total items: " + shopList.size());
    }

    // 메뉴 출력 및 사용자 입력 처리 메서드
    public void printMenu() {
        int menu;

        while (true) {
            System.out.print("1.List  2.List(price)  3.Search  4.Add  5.Modify  6.Delete  7.Save file 0.Exit ");
            menu = scanner.nextInt();

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

    // 아이템 추가
    private void addItem() {
        ShopInform item = shopCRUD.add();
        shopList.add(item); // 메모리에 데이터 추가
        dbUtil.insertItem(item.getName(), item.getDescription(), item.getPrice(), item.getQuantity(), item.getCategory()); // 데이터베이스에 추가
        System.out.println("Item added.");
    }

    // 아이템 수정
    private void modifyItem() {
        System.out.print("수정할 제품의 아이디를 입력하세요: ");
        int id = scanner.nextInt();
        ShopInform item = shopCRUD.modify(shopList, id);
        if (item != null) {
            dbUtil.updateItem(item); // 데이터베이스에 수정사항 반영 (DBUtil에 updateItem 메서드 필요)
            System.out.println("Item updated.");
        } else {
            System.out.println("아이디를 찾을 수 없습니다.");
        }
    }

    // 아이템 삭제
    private void deleteItem() {
        System.out.print("삭제할 제품의 아이디를 입력하세요: ");
        int id = scanner.nextInt();
        boolean removed = shopCRUD.delete(shopList, id);
        if (removed) {
            dbUtil.deleteItem(id); // 데이터베이스에서 항목 삭제 (DBUtil에 deleteItem 메서드 필요)
            System.out.println("Item deleted.");
        } else {
            System.out.println("아이디를 찾을 수 없습니다.");
        }
    }

    // 현재 데이터를 파일로 저장
    private void saveToFile() {
        StringBuilder data = new StringBuilder();
        for (ShopInform item : shopList) {
            data.append(item.toString()).append("\n"); // 데이터를 문자열로 변환
        }
        dbUtil.saveDataToFile(data.toString()); // 파일에 저장
    }
}
