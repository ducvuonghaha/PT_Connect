package com.tanxe.supple_online.model;

public class ProductsInCart {

//    public String id;
    public String username;
    public String productname;
    public int price;
    public int quantity;
    public String image;

//    public String getId() {
//        return id;
//    }

//    public void setId(String id) {
//        this.id = id;
////    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Integer getPrice() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ProductsInCart() {
    }

    public  ProductsInCart( String image, int price, String productname, int quantity, String username) {
        this.username = username;
        this.productname = productname;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public ProductsInCart(int quantity) {
        this.quantity = quantity;
    }
}
