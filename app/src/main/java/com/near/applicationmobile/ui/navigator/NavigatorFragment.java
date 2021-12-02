package com.near.applicationmobile.ui.navigator;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.near.applicationmobile.R;
import com.near.applicationmobile.adapters.ProductAdapter;
import com.near.applicationmobile.databinding.NavigatorFragmentBinding;
import com.near.applicationmobile.entities.Product;

import java.util.ArrayList;
import java.util.List;


public class NavigatorFragment extends Fragment {

    private NavigatorViewModel mViewModel;
    private final List<Product> products = new ArrayList<>();
    private ProductAdapter productAdapter;
    final static int MAIN_CALL = 120;
    private NavigatorFragmentBinding binding;


    public static NavigatorFragment newInstance() {
        return new NavigatorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NavigatorFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NavigatorFragment.this)
                        .navigate(R.id.create_navigatorFragment_to_editProductFragment);
            }
        });
        buildCustomAdapter();
    }

    private void buildCustomAdapter() {
        productAdapter = new ProductAdapter(getContext(), products);
        binding.ourListView.setAdapter(productAdapter);
        binding.ourListView.setOnItemClickListener((adapterView, view, position, id) -> {

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(products.isEmpty())
            mViewModel.loadProduct();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NavigatorViewModel.class);
        mViewModel.mutableLiveData.observe(getViewLifecycleOwner(), (productsList) -> {
            products.addAll(productsList);
            productAdapter.notifyDataSetChanged();
        });
        // TODO: Use the ViewModel
    }

}