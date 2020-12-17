package com.tanxe.supple_online.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tanxe.supple_online.database.Database;
import com.tanxe.supple_online.model.ProductsInCart;

import java.util.ArrayList;
import java.util.List;

import static com.tanxe.supple_online.database.Constant.PRODUCT_CART_IMAGE;
import static com.tanxe.supple_online.database.Constant.PRODUCT_CART_NAME;
import static com.tanxe.supple_online.database.Constant.PRODUCT_CART_NUMBER;
import static com.tanxe.supple_online.database.Constant.PRODUCT_CART_PRICE;
import static com.tanxe.supple_online.database.Constant.PRODUCT_CART_TABLE;
import static com.tanxe.supple_online.database.Constant.PRODUCT_CART_USERNAME;


public class ProductInCartDAO {
    private Database database;

    public ProductInCartDAO(Context context) {
        this.database = new Database(context);
    }

    public long insertProductCart(ProductsInCart productCart) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_CART_NAME, productCart.productname);
        contentValues.put(PRODUCT_CART_USERNAME, productCart.username);
        contentValues.put(PRODUCT_CART_NUMBER, productCart.quantity);
        contentValues.put(PRODUCT_CART_IMAGE, productCart.image);
        contentValues.put(PRODUCT_CART_PRICE, productCart.price);

        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        long result = sqLiteDatabase.insert(PRODUCT_CART_TABLE, null, contentValues);
        sqLiteDatabase.close();
        return result;
    }

    public long updateProductCartAmount(ProductsInCart productCart, String tensanpham) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_CART_NUMBER, productCart.quantity);

        //xin quyen ghi vao bang
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();

        long result = sqLiteDatabase.update(PRODUCT_CART_TABLE, contentValues, PRODUCT_CART_NAME + "=?",
                new String[]{tensanpham});

        return result;
    }

    public long deleteProductCart(String productName) {
        long result = -1;
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();

        result = sqLiteDatabase.delete(PRODUCT_CART_TABLE, PRODUCT_CART_NAME + "=?",
                new String[]{productName});

        return result;
    }

    public List<ProductsInCart> getAllProductCart(String username) {
        List<ProductsInCart> productCartList = new ArrayList<>();
        String sSQL = "SELECT * FROM GioHang WHERE Username like '" + username + "'";
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sSQL, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                //di chuyen toi vi tri dau tien cua con tro
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String PRODUCT_CART_NAME_ = cursor.getString(cursor.getColumnIndex(PRODUCT_CART_NAME));
                    String PRODUCT_CART_USERNAME_ = cursor.getString(cursor.getColumnIndex(PRODUCT_CART_USERNAME));
                    int PRODUCT_CART_PRICE_ = cursor.getInt(cursor.getColumnIndex(PRODUCT_CART_PRICE));
                    int PRODUCT_CART_NUMBER_ = cursor.getInt(cursor.getColumnIndex(PRODUCT_CART_NUMBER));
                    String PRODUCT_CART_IMAGE_ = cursor.getString(cursor.getColumnIndex(PRODUCT_CART_IMAGE));

                    ProductsInCart productCart = new ProductsInCart( PRODUCT_CART_IMAGE_, PRODUCT_CART_PRICE_, PRODUCT_CART_NAME_, PRODUCT_CART_NUMBER_, PRODUCT_CART_USERNAME_);
                    //add user vao array users;
                    productCartList.add(productCart);
                    //di chuyen toi vi tri tiep theo
                    cursor.moveToNext();
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        }
        return productCartList;
    }


    public double getTongTien(String username) {
        double tongtien = 0;
        String QUERY = "SELECT SUM(GiaThanh*SoLuong) FROM GioHang WHERE Username = '" + username + "' ";
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    tongtien = cursor.getDouble(0);
                    cursor.moveToNext();
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        }
        return tongtien;
    }

    public void insertMyOrders(String username) {
        String QUERY = "INSERT INTO DonHangCuaToi(Username,TenSanPham,SoLuong,GiaThanh,HinhAnh)" +
                " SELECT Username,TenSanPham,SoLuong,GiaThanh,HinhAnh FROM GioHang" +
                " WHERE Username like '" + username + "'";
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        sqLiteDatabase.execSQL(QUERY);
    }

    public void deleteCart(String username) {
        String QUERY = "DELETE FROM GioHang WHERE Username like '" + username + "'";
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        sqLiteDatabase.execSQL(QUERY);
    }


    public int getNumberInCart(String username) {
        int soluong = 0;
        String QUERY = "SELECT COUNT(TenSanPham) FROM GioHang  WHERE Username = '" + username + "'";
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    soluong = cursor.getInt(0);
                    cursor.moveToNext();
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        }
        return soluong;
    }

    public String getProductName(String username) {
        String tensp = "";
        String QUERY = "SELECT TenSanPham FROM GioHang  WHERE Username = '" + username + "'";
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    tensp = cursor.getString(0);
                    cursor.moveToNext();
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        }
        return tensp;
    }

    public String getUsername(){
        String username = "";
        String sSQL = "SELECT Username FROM GioHang";
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sSQL, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    username = cursor.getString(0);
                    cursor.moveToNext();
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        }
        return username;
    }
}

