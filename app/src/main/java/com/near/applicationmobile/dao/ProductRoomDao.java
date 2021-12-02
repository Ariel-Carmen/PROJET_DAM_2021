package com.near.applicationmobile.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.near.applicationmobile.entities.Product;

import java.util.List;

@Dao
public interface ProductRoomDao {
    @Query("SELECT * FROM product")
    List<Product> findAll();

    @Query("SELECT * FROM product WHERE id IN (:userIds)")
    List<Product> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM product WHERE name LIKE :search AND " +
            "description LIKE :search")
    List<Product> findByName(String search);

    @Insert
    void insertAll(Product... products);

    @Query("UPDATE product SET name = :nom, description = :desc, price = :prix, quantityInStock = :stock, alertQuantity= :alert WHERE id = :id")
    void updateProduct(int id, String nom, String desc, double prix, double stock, double alert);

    @Update
    void update(Product product);

    @Insert
    void insert(Product product);

    @Delete
    void delete(Product product);


}
