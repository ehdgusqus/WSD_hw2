package org.example;

import java.util.ArrayList;

public interface ICRUD {
    public ShopInform add();
    public ShopInform modify(ArrayList<ShopInform> shopList, int id);
    public boolean delete(ArrayList<ShopInform> shopList, int id);
    public void list(ArrayList<ShopInform> shopList);
    public void listPrice(ArrayList<ShopInform> shopList);
    public void search(ArrayList<ShopInform> shopList);
}
