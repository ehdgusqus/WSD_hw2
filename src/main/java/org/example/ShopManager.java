package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class ShopManager {
    Scanner scanner = new Scanner(System.in);
    ArrayList<ShopInform> shopList = new ArrayList<>();
    ShopCRUD shopCRUD = new ShopCRUD();

    public void start() {
        print();
    }
    // 메뉴 출력 메서드
    public void print(){
        int menu;

        while(true){
            System.out.println("1.List  2.List(price)  3.Search  4.Add  5.Modify  6.Delete  7.Save file 0.Exit");
            menu = scanner.nextInt();

            if(menu == 0){
                System.out.println("시스템이 종료되었습니다.");
                break;  // 반복문 종료
            }

            switch(menu){
                case 1: {
                    shopCRUD.list(shopList);
                    break;
                }
                case 2: {
                    shopCRUD.listPrice(shopList);
                    break;
                }
                case 3: {
                    shopCRUD.search(shopList);
                    break;
                }
                case 4: {
                    shopList.add(shopCRUD.add());
                    break;
                }
                case 5: {
                    System.out.print("수정할 제품의 아이디를 입력하세요: ");
                    int id = scanner.nextInt();
                    shopCRUD.modify(shopList, id);
                    break;
                }
                case 6: {
                    System.out.print("삭제할 제품의 아이디를 입력하세요: ");
                    int id = scanner.nextInt();
                    shopCRUD.delete(shopList, id);
                    break;
                }
                default:{
                    System.out.println("올바른 번호를 입력하세요");
                    break;
                }
            }
        }
    }

}
