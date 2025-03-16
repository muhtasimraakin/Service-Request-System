package com.example.srs.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.srs.MainActivity;
import com.example.srs.R;
import com.example.srs.databinding.FragmentProfileBinding;
import com.example.srs.databinding.FragmentProfileBinding;

import com.example.srs.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DocumentReference docRef;
    Button button;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            DocumentReference docRef = db.collection("customers").document(uid);

            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String phone = documentSnapshot.getString("phone");
                    String address = documentSnapshot.getString("address");

                    TextView nameTextView = root.findViewById(R.id.profile_name);
                    TextView phoneTextView = root.findViewById(R.id.profile_phone);
                    TextView addressTextView = root.findViewById(R.id.profile_address);

                    nameTextView.setText(name);
                    phoneTextView.setText(phone);
                    addressTextView.setText(address);
                } else {
                    Log.d("ProfileFragment", "Document does not exist!");
                }
            }).addOnFailureListener(e -> {
                Log.e("ProfileFragment", "Error getting document", e);
            });
        } else {
            Log.d("ProfileFragment", "No user logged in");
        }

        button = root.findViewById(R.id.logout_customer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}