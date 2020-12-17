package com.tanxe.supple_online.database;

public class Constant {

    public static final String PRODUCT_CART_TABLE = "GioHang";
    public static final String PRODUCT_CART_USERNAME = "Username";
    public static final String PRODUCT_CART_NAME = "TenSanPham";
    public static final String PRODUCT_CART_NUMBER = "SoLuong";
    public static final String PRODUCT_CART_PRICE = "GiaThanh";
    public static final String PRODUCT_CART_IMAGE = "HinhAnh";


    public static final String CREATE_PRODUCT_CART_TABLE = "CREATE TABLE " + PRODUCT_CART_TABLE + "(" +
            "" + PRODUCT_CART_NAME + " NVARCHAR(100) PRIMARY KEY," +
            "" + PRODUCT_CART_USERNAME + " NVARCHAR(100)," +
            "" + PRODUCT_CART_PRICE + " INT," +
            "" + PRODUCT_CART_IMAGE + " NVARCHAR(100)," +
            "" + PRODUCT_CART_NUMBER + " INT" +
            ")";




    //USER
//    public static final String USER_TABLE = "NguoiDung";
//
//    //khai bao ten cot trong bang nguoiDung
//    public static final String USER_NAME = "Username";
//    public static final String USER_PASSWORD = "Password";
//    public static final String USER_PHONE = "Phone";
//    public static final String USER_FULL_NAME = "HoTen";
//    public static final String USER_ADDRESS = "Diachi";
//
//    //cau lenh tao bang nguoi dung
//
//    //CREATE TABLE nguoiDung (userName NVARCHAR(50) , Password NVARCHAR(50) , Phone NCHAR(10) , hoTen NVARCHAR(50));
//
//    public static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + "(" +
//            "" + USER_NAME + " NVARCHAR(50) PRIMARY KEY NOT NULL ," +
//            "" + USER_PASSWORD + " NVARCHAR(50)," +
//            "" + USER_ADDRESS + " NVARCHAR(200)," +
//            "" + USER_PHONE + " NCHAR(10)," +
//            "" + USER_FULL_NAME + " NVARCHAR(50)" +
//            ")";



}
