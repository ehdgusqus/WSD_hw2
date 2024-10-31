package org.example;

public class ShopInform {
    private int id;
    private String name;
    private String description;
    private int price;
    private int quantity;
    private String category;
    private String createDate;

    public ShopInform( String name, String description, int price, int quantity, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public ShopInform(int id, String name, String description, int price, int quantity, String category) {
    }

    public ShopInform(int id, String name, String description, int price, int quantity, String category, String createDate) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "id=" + id +
                " [" +
                "name=" + name +
                ", description='" + description  + '\'' +
                ", price=" + price +
                ", quantity_level=" + quantity +
                ", category='" + category + '\'' +
                ", createDate=" + createDate + '\'' +
                ']';
    }
}
