package com.theretrocenter.esp32_camandroidapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.theretrocenter.esp32_camandroidapp.utilities.Preferences;

public class UserGuideFragment  extends Fragment {
    private MainActivity mainActivity = new MainActivity();
    private Preferences preferences = Preferences.getInstance(mainActivity);
    private String sawUserGuide = preferences.getData("SawUserGuide");

    void skipUserGuide() {
        if (sawUserGuide.equals("saw")) {
            NavHostFragment.findNavController(UserGuideFragment.this)
                    .navigate(R.id.action_UserGuideFragment_to_ControlCarFragment);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        skipUserGuide();
        return inflater.inflate(R.layout.user_guide, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        view.findViewById(R.id.closeUserGuide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save preferred UI control in preferences
                preferences.saveData("SawUserGuide", "saw");

                // Return to control car fragment layout
                NavHostFragment.findNavController(UserGuideFragment.this)
                        .navigate(R.id.action_UserGuideFragment_to_ControlCarFragment);
            }
        });
    }
}
