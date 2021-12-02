package com.near.applicationmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.near.applicationmobile.ui.editproduct.EditProductFragment;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_product);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, EditProductFragment.newInstance())
                    .commitNow();
        }
    }
}