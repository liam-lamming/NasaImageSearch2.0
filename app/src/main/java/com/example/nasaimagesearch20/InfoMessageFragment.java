package com.example.nasaimagesearch20;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.fragment.app.DialogFragment;

public class InfoMessageFragment extends DialogFragment {

    private CheckBox doNotShowCheckbox;
    private Button closeButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_message, container, false);

        doNotShowCheckbox = view.findViewById(R.id.do_not_show_checkbox);
        closeButton = view.findViewById(R.id.close_button);

        closeButton.setOnClickListener(v -> {
            if (doNotShowCheckbox.isChecked()) {
                // Save user preference to not show this message again
                saveDoNotShowPreference();
            }
            dismiss(); // Close the fragment
        });

        return view;
    }

    private void saveDoNotShowPreference() {
        SharedPreferences preferences = getActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("show_info_message", false);
        editor.apply();
    }
}
