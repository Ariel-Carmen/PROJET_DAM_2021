package com.near.applicationmobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.near.applicationmobile.dao.DataBaseHelper;
import com.near.applicationmobile.dao.DataBaseRoom;
import com.near.applicationmobile.dao.ProductDao;
import com.near.applicationmobile.dao.ProductRoomDao;
import com.near.applicationmobile.databinding.ActivityProductBinding;
import com.near.applicationmobile.entities.Product;
import com.near.applicationmobile.adapters.ProductAdapter;
import com.near.applicationmobile.webservices.ProductWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private ActivityProductBinding binding;

    private ProductAdapter productAdapter;
    private List<Product> products = new ArrayList<>();
    private ProductDao productDao;
    final static int MAIN_CALL = 120;
    final static int MAIN_CAL = 121;

    private ProductRoomDao productRoomDao;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        productDao = new ProductDao(this);
        productRoomDao = DataBaseRoom.getInstance(getApplicationContext()).productRoomDao();
        generateProducts();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                startActivityForResult(intent, MAIN_CALL);
            }
        });

        buildCustomAdapter();
        registerForContextMenu(binding.ourListView);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.add(Menu.NONE, 0, Menu.NONE, "Supprimer");
        menu.add(Menu.NONE, 1, Menu.NONE, "Modifier");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_menu_item:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
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
                            final ProductWebService productWebService = new ProductWebService();
                            @Override
                            public void run() {
                                productWebService.deleteProduct((Product)binding.ourListView.getAdapter().getItem(info.position));
                                productRoomDao.delete((Product)binding.ourListView.getAdapter().getItem(info.position));
                                runOnUiThread(()->{
                                    products.remove((Product)binding.ourListView.getAdapter().getItem(info.position));
                                    productAdapter.notifyDataSetChanged();
                                });
                            }
                        });
                        thread.start();
                    }
                }).setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setTitle("Confirmation de suppression");
                dialog.show();

                return true;
            case 1:
                AdapterView.AdapterContextMenuInfo nfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("PRODUIT", products.get(nfo.position));
                startActivityForResult(intent, MAIN_CAL);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void buildCustomAdapter() {
        productAdapter = new ProductAdapter(this, products);
        binding.ourListView.setAdapter(productAdapter);
        binding.ourListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                intent.putExtra("MY_PRODUCT", products.get(position));
                startActivityForResult(intent, MAIN_CALL );
            }
        });
    }

    private void buildSimpleAdapterData() {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (Product product :
                products) {
            Map<String, String> map = new HashMap<>();
            map.put("name", product.name);
            map.put("price", "XOF " + product.price);
            map.put("quantity",  product.quantityInStock + " disponible" +
                    (product.quantityInStock>1 ? "s" : ""));
            mapList.add(map);
        }
        binding.ourListView.setAdapter(new SimpleAdapter(this, mapList, R.layout.regular_product_item,
                new String[]{"name", "quantity", "price"}, new int[]{R.id.name, R.id.quantity_in_stock, R.id.price}));
    }
    private void generateProducts(){
//        products = productDao.findAll();
//        if (products.isEmpty()){
//            productDao.insert(new Product("PC HP", "HP NoteBook", 10000, 100, 10));
//            productDao.insert(new Product("Huawey P9", "HP NoteBook", 6000, 100, 10));
//            productDao.insert(new Product("Galaxy S21", "Samsung Galaxy S21", 800000, 100, 10));
//            productDao.insert(new Product("Galaxy Note 10", "Samsung Galaxy Note 10", 800000, 100, 10));
//            productDao.insert(new Product("Redmi S11", "Xiaomi Redmi S11", 300000, 100, 10));}
//        products = productDao.findAll();

        Thread thread = new Thread(new Runnable() {
            final List<Product> localProducts = new ArrayList<>();
            final ProductWebService productWebService = new ProductWebService();
            @Override
            public void run() {
                localProducts.addAll(productWebService.getProducts());
                localProducts.addAll(productRoomDao.findAll());
                if(localProducts.isEmpty()) {
                    productRoomDao.insert(new Product("Galaxy S21", "Samsung Galaxy S21", 800000, 100, 10));
                    productRoomDao.insert(new Product("Galaxy Note 10", "Samsung Galaxy Note 10", 800000, 100, 10));
                    productRoomDao.insert(new Product("Redmi S11", "Xiaomi Redmi S11", 300000, 100, 10));
                    productRoomDao.insert(new Product("Galaxy S21", "Samsung Galaxy S21", 800000, 100, 10));
                    localProducts.addAll(productRoomDao.findAll());
                }
                runOnUiThread(()->{
                    products.addAll(localProducts);
                });
            }
        });
        thread.start();
        buildCustomAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MAIN_CALL) {
            if(resultCode== Activity.RESULT_OK) {
                Log.e("TAG", "onActivityResult: " + data.getSerializableExtra("NEW_PRODUCT"));
                Product product = (Product) data.getSerializableExtra("NEW_PRODUCT");
//                productDao.insert(product);
//                products = productDao.findAll();
//                buildCustomAdapter();


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("save :: " + product);
                        ProductWebService productWebService = new ProductWebService();
                        Product save = productWebService.createProduct(product);
                        System.out.println("save :: " + save);

                        productRoomDao.insert(product);
//                        products.clear();
//                        products.addAll(productRoomDao.findAll());

                        runOnUiThread(()->{
                            productAdapter.notifyDataSetChanged();
                        });
                    }
                });
                thread.start();
                buildCustomAdapter();
            }

        }
        else if(requestCode==MAIN_CAL) {
            if(resultCode== Activity.RESULT_OK) {
                Log.e("TAG", "onActivityResult: " + data.getSerializableExtra("NEW_PRODUCT"));
                Product product = (Product) data.getSerializableExtra("NEW_PRODUCT");
//                productDao.insert(product);
//                products = productDao.findAll();
//                buildCustomAdapter();

                if (product!=null){
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            productRoomDao.update(product);
                            System.out.println("save :: " + product);
                            products.set(product.id, product);

                            //productRoomDao.update(product);
                            runOnUiThread(()->{
                                productAdapter.notifyDataSetChanged();
                                Log.d("eee", "produit remplace");
                            });
                        }
                    });
                    thread.start();
                    buildCustomAdapter();
                }
            }
        }
    }
}