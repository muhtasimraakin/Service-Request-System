package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.srs.databinding.ActivityCustomerNavBinding;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerNav extends AppCompatActivity {

    private ActivityCustomerNavBinding binding;
    private EditText searchEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_services, R.id.navigation_history, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_customer_nav);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Initialize search UI elements
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        // Set click listener for the search button
        searchButton.setOnClickListener(v -> {
            String providerName = searchEditText.getText().toString().trim();
            if (!providerName.isEmpty()) {
                // Start ProviderInfo activity with the provider name as an extra
                Intent intent = new Intent(CustomerNav.this, ProviderInfo.class);
                intent.putExtra("providerName", providerName);
                startActivity(intent);
            } else {
                // Show a toast if the search query is empty
                Toast.makeText(CustomerNav.this, "Please enter a provider name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
