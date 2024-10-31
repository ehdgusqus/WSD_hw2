package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class ShopCRUD implements ICURD {
    Scanner scanner = new Scanner(System.in);

    @Override
    public ShopInform add() {
        System.out.println("Add Shop");
        return printShop(); // ShopInform 객체 반환
    }

    @Override
    public void modify(ArrayList<ShopInform> shopList, int id) {
        System.out.println("Modify Shop");
        ShopInform item = findById(shopList, id);
        if (item != null) {
            int index = shopList.indexOf(item);
            shopList.set(index, printShop());
            System.out.println("수정 완료");
        } else {
            System.out.println("해당 아이디의 제품을 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(ArrayList<ShopInform> shopList, int id) {
        ShopInform item = findById(shopList, id);
        if (item != null) {
            shopList.remove(item);
            System.out.println("삭제 완료");
        } else {
            System.out.println("해당 아이디의 제품을 찾을 수 없습니다.");
        }
    }

    @Override
    public void list(ArrayList<ShopInform> shopList) {
        for (ShopInform item : shopList) {
            System.out.println(item);
        }
    }

    @Override
    public void listPrice(ArrayList<ShopInform> shopList) {
        // 가격 정렬된 리스트를 출력하는 로직
    }

    @Override
    public void search(ArrayList<ShopInform> shopList) {
        System.out.print("검색할 상품명: ");
        String name = scanner.next();
        for (ShopInform item : shopList) {
            if (item.getName().equalsIgnoreCase(name)) {
                System.out.println(item);
            }
        }
    }

    // 아이디로 제품 찾기
    private ShopInform findById(ArrayList<ShopInform> shopList, int id) {
        for (ShopInform item : shopList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    // ShopInform 객체 생성 메서드
    private ShopInform printShop() {
        System.out.print("ID: ");
        int id = scanner.nextInt();

        System.out.print("상품명: ");
        String name = scanner.next();

        System.out.print("상세정보: ");
        String description = scanner.next();

        System.out.print("가격: ");
        int price = scanner.nextInt();

        System.out.print("날짜: ");
        String date = scanner.next();

        System.out.print("품질: ");
        String quantity = scanner.next();

        return new ShopInform(id, name, description, price, date, quantity);
    }
}
