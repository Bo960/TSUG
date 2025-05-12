package at.ac.univie.hci.tsug;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Set;

import at.ac.univie.hci.tsug.activities.AccountActivity;
import at.ac.univie.hci.tsug.activities.CreateActivity;
import at.ac.univie.hci.tsug.activities.SearchActivity;
import at.ac.univie.hci.tsug.activities.SettingsActivity;
import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.container.PostContainer;
import at.ac.univie.hci.tsug.container.UserContainer;
import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.User;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    SearchView searchView;
    public String simpleSearchTerm;
    String activityName = "TSUG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //BOTTOM NAVIGATION:
        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_neuer_beitrag:
                        //Beitrag erstellen Seite
                        intent = new Intent(MainActivity.this, CreateActivity.class);
                        startActivity(intent);
                        //Von Position-Links nach Position-Rechts
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        return true;

                    case R.id.nav_account:
                        //Account settings Seite
                        intent = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(intent);
                        //Von Position-Links nach Position-Rechts
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        return true;
                }
                return false;
            }
        });

        //SIMPLE SEARCH BAR:
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                simpleSearchTerm = newText;
                return true;
            }
        });

        Button searchButton = findViewById(R.id.searchStart);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO SHOW RESULTS
            }
        });

        //TOP NAVIGATION:
        ImageButton backNav = findViewById(R.id.nav_back);
        backNav.setOnClickListener(v -> finish());

        ImageButton setNav = findViewById(R.id.nav_einstellungen);
        setNav.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
        });

        //SUCHFILTER:
        Button searchFilter = findViewById(R.id.searchFilter);
        searchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
            }
        });

        //TEXT
        TextView testText = findViewById(R.id.nav_text_testing);
        testText.setText(activityName);

        //TODO TESTING CONTAINER to delete later:
        TextView username = findViewById(R.id.test_name);
        TextView user_id = findViewById(R.id.test_user_ID);
        TextView titel = findViewById(R.id.test_titel);
        TextView post_id = findViewById(R.id.test_post_ID);
        TextView post_des = findViewById(R.id.test_des);

        Set<Post> allPosts = Container.getAllPosts();
        Post post = allPosts.iterator().next();

        username.setText(post.getUser().getName());
        user_id.setText(String.valueOf(post.getUser().getID()));

        titel.setText(post.getTitel());
        post_id.setText(String.valueOf(post.getID()));
        post_des.setText(post.getDes());


    }
    public String getSimpleSearchTerm() {
        return simpleSearchTerm;
    }
}

