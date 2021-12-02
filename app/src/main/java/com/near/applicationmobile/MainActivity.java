package com.near.applicationmobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.near.applicationmobile.dao.DataBaseHelper;
import com.near.applicationmobile.dao.DataBaseRoom;
import com.near.applicationmobile.dao.ProductDao;
import com.near.applicationmobile.dao.ProductRoomDao;
import com.near.applicationmobile.databinding.DialogBinding;
import com.near.applicationmobile.entities.Product;
import com.near.applicationmobile.databinding.ActivityMainBinding;
import com.near.applicationmobile.webservices.ProductWebService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getCanonicalName();
    private TextInputEditText designationEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText priceEditText;
    private TextInputEditText qtyInStockEditText;
    private TextInputEditText alertQuantityEditText;
    private ProductRoomDao productRoomDao;
    private Product produit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productRoomDao = DataBaseRoom.getInstance(getApplicationContext()).productRoomDao();

        designationEditText = findViewById(R.id.designation);
        descriptionEditText = findViewById(R.id.description);
        priceEditText = findViewById(R.id.price);
        qtyInStockEditText = findViewById(R.id.qteD);
        alertQuantityEditText = findViewById(R.id.qteA);
        Intent data = this.getIntent();
        produit = (Product) data.getSerializableExtra("PRODUIT");


        if (produit!=null){
            designationEditText.setText(produit.name);
            descriptionEditText.setText(produit.description);
            priceEditText.setText(""+produit.price);
            qtyInStockEditText.setText(""+produit.quantityInStock);
            alertQuantityEditText.setText("" +produit.alertQuantity);
        }


        findViewById(R.id.myBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public void saveProduct(){
        Product product = new Product();
        boolean des = emptyField(descriptionEditText);
        boolean desc = emptyField(designationEditText);
        boolean prx = emptyField(qtyInStockEditText);
        boolean qtd = emptyField(priceEditText);
        boolean qta = emptyField(alertQuantityEditText);

        if (des && desc && prx && qtd && qta){
            product.name = designationEditText.getText().toString();
            product.description = descriptionEditText.getText().toString();
            product.price = Double.parseDouble(priceEditText.getText().toString());
            product.quantityInStock = Double.parseDouble(qtyInStockEditText.getText().toString());
            product.alertQuantity = Double.parseDouble(alertQuantityEditText.getText().toString());

            if (produit!=null){
                Thread thread = new Thread(new Runnable() {
                    final ProductWebService productWebService = new ProductWebService();
                    @Override
                    public void run() {
                        productWebService.updateProduct(produit);
                        productRoomDao.update(product);
                    }
                });
                thread.start();
            }

            Intent intent = getIntent();
            intent.putExtra("NEW_PRODUCT", product);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public boolean emptyField(EditText editText){
        boolean bool = false;
        if (editText.getText().toString().isEmpty()){
            editText.setError("Remplissez le champ");
        } else {
            bool = true;
        }
        return bool;
    }

}