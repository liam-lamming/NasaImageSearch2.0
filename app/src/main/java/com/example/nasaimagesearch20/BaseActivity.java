package com.example.nasaimagesearch20;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Note: No setContentView here. This will be handled by child activities.
    }

    // Method to set up the toolbar and make it reusable in all activities
    protected void setUpToolbar(int layoutResID) {
        setContentView(layoutResID);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource into the toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selections
        int id = item.getItemId();
        if (id == R.id.action_help) {
            showHelpDialog();
            return true;
        } else if (id == R.id.action_settings) {
            openSettings();
            return true;
        } else if (id == R.id.action_refresh) {
            refreshPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to show the help fragment as a dialog
    private void showHelpDialog() {
        // Create an instance of HelpFragment
        HelpFragment helpFragment = new HelpFragment();

        // Show the fragment as a dialog
        FragmentManager fragmentManager = getSupportFragmentManager();
        helpFragment.show(fragmentManager, "HelpFragment");
    }

    // Method to open settings (modify this to open your actual settings)
    private void openSettings() {
        // You can launch a settings activity or display a dialog, etc.
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Method to refresh the current activity/page
    private void refreshPage() {
        // Refresh the current activity
        finish();
        startActivity(getIntent());
    }
}
