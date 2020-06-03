public class Product {
    private int id;
    private String title;
    private int price;

    Product() {
        id = -1;
        title = "";
        price = -1;
    }

    void setID(int id) {
        this.id = id;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setPrice(int price) {
        this.price = price;
    }

    int getId() {return id;}
    String getTitle() {return title;}
    int getPrice() {return price;}

}
