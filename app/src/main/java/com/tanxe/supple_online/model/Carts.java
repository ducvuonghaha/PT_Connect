
package com.tanxe.supple_online.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Carts {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("Cart")
    @Expose
    private List<Cart> cart = null;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("Recipients")
    @Expose
    private String recipients;
    @SerializedName("ReceivingAddress")
    @Expose
    private String receivingAddress;
    @SerializedName("DateCart")
    @Expose
    private Date dateCart;
    @SerializedName("TotalPrice")
    @Expose
    private Double totalPrice;

    @SerializedName("Phone")
    @Expose
    private String phone;

    @SerializedName("Status")
    @Expose
    private String status;

    public String getPhone() {
        return phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    public Date getDateCart() {
        return dateCart;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDateCart(Date dateCart) {
        this.dateCart = dateCart;
    }

    public Carts(String username, List<Cart> cart, String phone, String recipients, String receivingAddress, Date dateCart, Double totalPrice, String status) {
        this.username = username;
        this.cart = cart;
        this.recipients = recipients;
        this.receivingAddress = receivingAddress;
        this.dateCart = dateCart;
        this.totalPrice = totalPrice;
        this.phone = phone;
        this.status = status;
    }

    public Carts() {
    }
}
