package com.theretrocenter.esp32_camandroidapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class CreditsFragment extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.credits, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.creditsBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to credits view
                NavHostFragment.findNavController(CreditsFragment.this)
                        .navigate(R.id.action_CreditsFragment_to_ConfigurationFragment);
            }
        });

    }
}
