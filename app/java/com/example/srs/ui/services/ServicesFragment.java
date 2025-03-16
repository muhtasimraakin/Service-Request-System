package com.example.srs.ui.services;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import com.example.srs.R;
import android.widget.Button;
import android.content.Intent;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.srs.SearchResults;
import com.example.srs.databinding.FragmentServicesBinding;

public class ServicesFragment extends Fragment {

    private FragmentServicesBinding binding;
    private GridLayout buttonGridLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        buttonGridLayout = view.findViewById(R.id.buttonGridLayout);

        String[] servicesArray = getResources().getStringArray(R.array.service_provider_array);
        int[] iconResIds = {R.drawable.ic_appliances, R.drawable.ic_plumbing, R.drawable.ic_electrical, R.drawable.ic_home_cleaning,
                R.drawable.ic_tutoring, R.drawable.ic_packaging_moving, R.drawable.ic_computer_repair,
                R.drawable.ic_painting, R.drawable.ic_pest_control, R.drawable.ic_car_cleaning};

        for (int i = 0; i < servicesArray.length; i++) {
            addButtonToGridLayout(servicesArray[i], iconResIds[i]);
        }

        return view;
    }

    private void addButtonToGridLayout(String serviceName, int iconResId) {
        Button button = new Button(requireContext());
        button.setLayoutParams(new GridLayout.LayoutParams());
        button.setBackgroundResource(R.drawable.services_btn);

        // Set the icon for the button and resize it
        @SuppressLint("UseCompatLoadingForDrawables") Drawable icon = getResources().getDrawable(iconResId);
        int iconSize = getResources().getDimensionPixelSize(R.dimen.icon_size); // Define 'icon_size' in your dimens.xml
        icon.setBounds(0, 0, iconSize, iconSize); // Set the bounds of the icon
        button.setCompoundDrawables(null, icon, null, null); // Set the icon on the left side of the button

        // Set the text below the icon
        button.setGravity(Gravity.CENTER);
        button.setText(serviceName);
        button.setCompoundDrawablePadding(10);
        button.setTextSize(10);


        button.setOnClickListener(v -> {
            // Handle button click here
            Intent intent = new Intent(getActivity(), SearchResults.class);
            intent.putExtra("serviceSelected", serviceName);
            startActivity(intent);
        });
        buttonGridLayout.addView(button);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}