package com.near.applicationmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.near.applicationmobile.ui.navigator.NavigatorFragment;

public class NavigatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigator_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager() // composant qui permet de manipuler les fragments au sein d'une activity
                    .beginTransaction() // permet de gerer les transaction entre fragment
                    .replace(R.id.container, NavigatorFragment.newInstance())
                    .commitNow();

           // NavController navController = Navigation.findNavController(this, R.id.navigator_graph);
//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }
}