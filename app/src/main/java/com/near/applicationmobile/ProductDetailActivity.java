package com.near.applicationmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.near.applicationmobile.dao.DataBaseRoom;
import com.near.applicationmobile.dao.ProductRoomDao;
import com.near.applicationmobile.databinding.ActivityProductDetailBinding;
import com.near.applicationmobile.databinding.DialogBinding;
import com.near.applicationmobile.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding productDetailBinding;
    private ProductRoomDao productRoomDao;
    private Product product;
    private DialogBinding dialogBinding;
    private EditText name, description, qStock, qAlert, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productDetailBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        dialogBinding = DialogBinding.inflate(getLayoutInflater());
        setContentView(productDetailBinding.getRoot());
        productRoomDao = DataBaseRoom.getInstance(getApplicationContext()).productRoomDao();

        product = (Product) getIntent().getSerializableExtra("MY_PRODUCT");
        showProduct(product);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.product_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Voulez-vous supprimer ce produit?")
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                productRoomDao.delete(product);
                                setResult(2);
                                finish();
                            }
                        });
                        thread.start();
                    }
                }).setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setTitle("Confirmation de suppression");
                dialog.show();
                return true;

            case R.id.edit:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Modifier objet");
                final View customLayout
                        = getLayoutInflater()
                        .inflate(
                                R.layout.dialog,
                                null);
                name = customLayout.findViewById(R.id.nm);
                description = customLayout.findViewById(R.id.dsc);
                price = customLayout.findViewById(R.id.pr);
                qStock = customLayout.findViewById(R.id.dp);
                qAlert = customLayout.findViewById(R.id.ap);
                name.setText(product.name);
                description.setText(product.description);
                price.setText(String.format("%s", product.price));
                qStock.setText(String.format("%s", product.quantityInStock));
                qAlert.setText(String.format("%s", product.alertQuantity));

                builder1.setView(customLayout)
                        .setCancelable(false)
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setPositiveButton("Terminé", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean des = emptyField(name);
                        boolean desc = emptyField(description);
                        boolean prx = emptyField(price);
                        boolean qtd = emptyField(qStock);
                        boolean qta = emptyField(qAlert);

                        if (des && desc && prx && qtd && qta){
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    productRoomDao.updateProduct(product.id,
                                            name.getText().toString(),
                                            description.getText().toString(),
                                            Double.parseDouble(price.getText().toString()),
                                            Double.parseDouble(qStock.getText().toString()),
                                            Double.parseDouble(qAlert.getText().toString())
                                    );
                                    setResult(2);
                                    finish();
                                }
                            });
                            thread.start();
                        }
                    }
                });
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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

    private void showProduct(Product product){
        productDetailBinding.name.setText(product.name);
        productDetailBinding.desc.setText(product.description);
        productDetailBinding.prix.setText("XOF "+product.price);
        productDetailBinding.qted.setText("En stock: " + product.quantityInStock);
        productDetailBinding.qtea.setText("Alerte à: " + product.alertQuantity);
    }
}

