package com.example.srs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class TipsBlogsFragment extends Fragment {
    private FirebaseFirestore db;
    private Context context; // Store the context here

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tips_blogs, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Store the context
        context = getContext();

        // Reference to the parent layout where you want to add the cards
        LinearLayout parentLayout = root.findViewById(R.id.tipsParentLayout);

        // Read data from Firestore
        db.collection("tipsBlogs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (isAdded() && context != null) { // Check if fragment is attached and context is not null
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String title = document.getString("title");
                                    String description = document.getString("description");

                                    // Create a new CardView
                                    CardView cardView = new CardView(context); // Use the stored context here
                                    cardView.setLayoutParams(new CardView.LayoutParams(
                                            CardView.LayoutParams.MATCH_PARENT,
                                            CardView.LayoutParams.WRAP_CONTENT));
                                    cardView.setRadius(15); // Set card corner radius
                                    cardView.setCardElevation(0); // Set card elevation
                                    cardView.setUseCompatPadding(true); // Add padding for card content
                                    cardView.setContentPadding(20, 20, 20, 20); // Set content padding
                                    cardView.setCardBackgroundColor(getResources().getColor(R.color.light_bg));

                                    // Create a new LinearLayout for the card content
                                    LinearLayout linearLayout = new LinearLayout(context); // Use the stored context here
                                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                                    // Create and add TextViews for title and description to the LinearLayout
                                    TextView titleTextView = new TextView(context); // Use the stored context here
                                    titleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    titleTextView.setText(title);
                                    titleTextView.setTextSize(18);
                                    titleTextView.setPadding(15,15,15,15);
                                    linearLayout.addView(titleTextView);

                                    TextView descriptionTextView = new TextView(context); // Use the stored context here
                                    descriptionTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    descriptionTextView.setText(description);
                                    linearLayout.addView(descriptionTextView);
                                    descriptionTextView.setPadding(15,15,15,15);


                                    // Add the LinearLayout to the CardView
                                    cardView.addView(linearLayout);

                                    // Add the CardView to the parent layout
                                    parentLayout.addView(cardView);
                                }
                            } else {
                                TextView errorTextView = new TextView(context); // Use the stored context here
                                errorTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                errorTextView.setText("Error fetching data");
                                parentLayout.addView(errorTextView);
                            }
                        }
                    }
                });

        return root;
    }
}

