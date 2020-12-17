
package com.tanxe.supple_online.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Quantity")
    @Expose
    private int quantity;
    @SerializedName("Price")
    @Expose
    private Integer price;
    @SerializedName("_id")
    @Expose
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Cart(String name, int quantity, Integer price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Cart() {
    }
}
