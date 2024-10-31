package org.example;

public class ShopInform {
    private int id;
    private String name;
    private String description;
    private int price;
    private String create_date;
    private String quality;

    public ShopInform(int id, String name, String description, int price, String create_date, String quality) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.create_date = create_date;
        this.quality = quality;
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

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", create_date='" + create_date + '\'' +
                ", quality='" + quality + '\'' +
                '}';
    }
}
