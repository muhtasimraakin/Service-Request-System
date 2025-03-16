package com.example.srs.ui.history;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.srs.R;
import com.example.srs.RequestStatus; // Assuming you have a RequestStatusActivity
import com.example.srs.databinding.FragmentHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private String providerUid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get a reference to the "buttonContainer" LinearLayout using view binding
        LinearLayout buttonContainer = binding.buttonContainer;

        // Get the UID of the current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserUid = mAuth.getCurrentUser().getUid();

        // Get a reference to the "requests" collection
        CollectionReference requestsRef = FirebaseFirestore.getInstance().collection("requests");

        // Query documents where the customerUid matches the UID of the current user
        requestsRef.whereEqualTo("customerUid", currentUserUid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Loop through each document in the result
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the "providerUid" and "customerUid" fields
                    providerUid = document.getString("providerUid");

                    // Get the provider name using the provider UID
                    FirebaseFirestore.getInstance().collection("providers")
                            .document(providerUid)
                            .get()
                            .addOnSuccessListener(providerDocument -> {
                                String providerName = providerDocument.getString("name");
                                addButtonToLayout(providerName, document.getString("providerUid"), buttonContainer, document.getId());
                                // Pass request ID to addButtonToLayout
                            });
                }
            } else {
                // Handle error
            }
        });

        return root;
    }

    private void addButtonToLayout(String providerName, String providerUid, LinearLayout buttonContainer, String requestId) {
        if (isAdded()) {
            // Access context only if fragment is attached
            Button button = new Button(requireContext());

            // Get servicesOffered from provider document
            FirebaseFirestore.getInstance().collection("providers")
                    .document(providerUid)
                    .get()
                    .addOnSuccessListener(providerDocument -> {
                        String servicesOffered = providerDocument.getString("servicesOffered");
                        // Set button text with provider name and services offered
                        button.setText(providerName + "\nService: " + servicesOffered);

                        // Set text transformation method to none
                        button.setAllCaps(false);

                        // Set background drawable from XML
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.provider_card));
                        } else {
                            button.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.provider_card));
                        }

                        // Set margins
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        int marginBottomPx = 26; // Define your margin dimension in resources
                        layoutParams.setMargins(36, 0, 36, marginBottomPx);
                        button.setLayoutParams(layoutParams);

                        button.setOnClickListener(view -> {
                            // Get the status field of the request ID
                            FirebaseFirestore.getInstance().collection("requests")
                                    .document(requestId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        String requestStatus = documentSnapshot.getString("status");
                                        // Navigate to RequestStatusActivity when the button is clicked
                                        Intent intent = new Intent(requireContext(), RequestStatus.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("providerName", providerName);
                                        intent.putExtra("providerUid", providerUid); // Passing providerUid as extra
                                        intent.putExtra("requestStatus", requestStatus);
                                        startActivity(intent);
                                    });
                        });
                        buttonContainer.addView(button);
                    });
        }
    }







    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
