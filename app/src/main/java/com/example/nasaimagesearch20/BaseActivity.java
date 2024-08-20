package com.example.nasaimagesearch20;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No setContentView here. This will be handled by child activities.
    }

    // Method to set up the toolbar and make it reusable in all activities
    protected void setUpToolbar(int layoutResID, boolean showBackButton) {
        setContentView(layoutResID);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Enable the back button in the toolbar if required
        if (showBackButton) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);  // Custom back icon
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource into the toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

//Removed for current functionalities
//        // Set up the search view in the toolbar
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();

        // Customizing the SearchView hint and behavior
//        searchView.setQueryHint(getString(R.string.search_hint)); // Fetching hint from strings.xml
//        searchView.setIconifiedByDefault(false); // Ensures the search bar is not collapsed
//        searchView.setSubmitButtonEnabled(true); // Enables a submit button within the search view
//
//        // Handle search query submission
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // Handle search query submission
//                searchImages(query);
//                searchView.clearFocus();  // Clear focus to collapse the keyboard
//                return true;
//            }

//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Handle search query text changes if needed
//                return false;
//            }
//        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selections
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Handle the back button press in the toolbar
            onBackPressed();
            return true;
        } else if (id == R.id.action_help) {
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
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Method to refresh the current activity/page
    private void refreshPage() {
        finish();
        startActivity(getIntent());

// Removed for later functionality
//    // Method to handle searching images based on a query
//    private void searchImages(String query) {
//        // Implement the search functionality
//        Intent intent = new Intent(this, SearchResultsActivity.class);
//        intent.putExtra("SEARCH_QUERY", query);
//        startActivity(intent);
    }
}
