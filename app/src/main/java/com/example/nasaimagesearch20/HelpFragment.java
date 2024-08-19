package com.example.nasaimagesearch20;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class HelpFragment extends DialogFragment {

    private static final String ARG_HELP_TEXT = "help_text";

    public static HelpFragment newInstance(String helpText) {
        HelpFragment fragment = new HelpFragment();

        // Pass the help text as an argument to the fragment
        Bundle args = new Bundle();
        args.putString(ARG_HELP_TEXT, helpText);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment_help layout
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        // Get the help text passed via arguments and set it to the TextView
        TextView helpTextView = view.findViewById(R.id.help_text_view);
        if (getArguments() != null) {
            String helpText = getArguments().getString(ARG_HELP_TEXT, "No help available.");
            helpTextView.setText(helpText);
        }

        return view;
    }
}
