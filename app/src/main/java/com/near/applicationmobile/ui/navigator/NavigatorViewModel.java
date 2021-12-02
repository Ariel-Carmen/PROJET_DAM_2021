package com.near.applicationmobile.ui.navigator;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.near.applicationmobile.dao.DataBaseRoom;
import com.near.applicationmobile.dao.ProductDao;
import com.near.applicationmobile.dao.ProductRoomDao;
import com.near.applicationmobile.entities.Product;

import java.util.List;

public class NavigatorViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private ProductDao productDao;
    private ProductRoomDao productRoomDao;
    public MutableLiveData<List<Product>> mutableLiveData = new MutableLiveData<>();

    public NavigatorViewModel(Application application) {
        super();
        this.productDao = new ProductDao(application);
        this.productRoomDao = DataBaseRoom.getInstance(application).productRoomDao();
    }

    public void loadProduct() {
        Log.d("TAG", "loadProduct: ");
        new Thread(() -> {
            mutableLiveData.postValue(productRoomDao.findAll());
        }).start();
    }
}