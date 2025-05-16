package at.ac.univie.hci.tsug.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.PostAdapter;
import at.ac.univie.hci.tsug.elements.RecyclerviewInterface;
import at.ac.univie.hci.tsug.elements.SearchHelper;
import at.ac.univie.hci.tsug.elements.User;

public class HomeActivity extends AppCompatActivity implements RecyclerviewInterface {


    private SearchView searchView;
    public static String simpleSearchTerm;
    private String activityName = "TSUG";
    private User currentUser;
    public ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Generate RecyclerView:
        posts = Container.getListOfPosts();
        showPosts();

        //Recieveing User from Login or Register:
        currentUser = getIntent().getParcelableExtra("user");

        //BOTTOM NAVIGATION:
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_neuer_beitrag:
                        //Beitrag erstellen Seite
                        intent = new Intent(HomeActivity.this, CreateActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        //Von Position-Links nach Position-Rechts
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        return true;

                    case R.id.nav_account:
                        //Account settings Seite
                        intent = new Intent(HomeActivity.this, AccountActivity.class);
                        intent.putExtra("user", currentUser);
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

        //SIMPLE SEARCH:
        Button searchButton = findViewById(R.id.searchStart);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posts = SearchHelper.simpleSearchReturn(simpleSearchTerm);
                showPosts();
            }
        });

        //TOP NAVIGATION:
        ImageButton backNav = findViewById(R.id.nav_back);
        backNav.setOnClickListener(v -> finish());

        ImageButton setNav = findViewById(R.id.nav_einstellungen);
        setNav.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
        });

        //SUCHFILTER:
        Button searchFilter = findViewById(R.id.searchFilter);
        searchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
            }
        });

        //TEXT
        TextView testText = findViewById(R.id.nav_text_testing);
        testText.setText(activityName);
    }
    public static String getSimpleSearchTerm() {
        return simpleSearchTerm;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(HomeActivity.this, PostActivity.class);

        intent.putExtra("beitrag_id", posts.get(position).getID());

        intent.putExtra("user", currentUser);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    //Generating the List for the Recyclerview:

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void showPosts()
    {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

        recyclerView.setAdapter(new PostAdapter(HomeActivity.this, getApplicationContext(), posts));

    }
}

