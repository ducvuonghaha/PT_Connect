
package com.tanxe.supple_online.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    public User(String id, List<String> specialized, String fullname, String age, String background, String workplace, String sex,String address) {
        this.id = id;
        this.specialized = specialized;
        this.fullname = fullname;
        this.age = age;
        this.background = background;
        this.workplace = workplace;
        this.sex = sex;
        this.address=address;
    }

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Specialized")
    @Expose
    private List<String> specialized = null;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("Fullname")
    @Expose
    private String fullname;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Age")
    @Expose
    private String age;
    @SerializedName("Background")
    @Expose
    private String background;
    @SerializedName("ImageProfile")
    @Expose
    private String imageProfile;
    @SerializedName("Rating")
    @Expose
    private String rating;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Workplace")
    @Expose
    private String workplace;

    @SerializedName("Sex")
    @Expose
    private String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getSpecialized() {
        return specialized;
    }

    public void setSpecialized(List<String> specialized) {
        this.specialized = specialized;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public User() {
    }
}
