package at.ac.univie.hci.tsug.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.PostAdapter;
import at.ac.univie.hci.tsug.elements.RecyclerviewInterface;
import at.ac.univie.hci.tsug.elements.User;

public class SearchResultsActivity extends AppCompatActivity implements RecyclerviewInterface {
    private RecyclerView rvResults;
    private ArrayList<Post> results;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        //User aus Intent holen
        currentUser = getIntent().getParcelableExtra("user");

        //Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        intent = new Intent(SearchResultsActivity.this, MainActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
                        return true;
                    case R.id.nav_neuer_beitrag:
                        intent = new Intent(SearchResultsActivity.this, CreateActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
                        return true;
                    case R.id.nav_account:
                        intent = new Intent(SearchResultsActivity.this, AccountActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
                        return true;
                }
                return false;
            }
        });

        // Top Navigation
        ImageButton backNav = findViewById(R.id.nav_back);
        backNav.setOnClickListener(v -> finish());

        ImageButton setNav = findViewById(R.id.nav_einstellungen);
        setNav.setOnClickListener(v -> {
            Intent intent = new Intent(SearchResultsActivity.this, SettingsActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
        });

        //RecyclerView einrichten
        rvResults = findViewById(R.id.rv_results);
        rvResults.setLayoutManager(new LinearLayoutManager(this));

        //Ergebnisse aus Intent lesen
        results = getIntent().getParcelableArrayListExtra("results");
        if (results == null) {
            results = new ArrayList<>();
        }

        //Adapter mit this als Listener übergeben
        PostAdapter adapter = new PostAdapter(this, this, results);
        rvResults.setAdapter(adapter);
    }

    //Wird aufgerufen, wenn ein Post im RecyclerView angeklickt wird -> Posts anklickbar gemacht - Woooooaaaahhhh
    @Override
    public void onPostClick(int position) {
        Post clicked = results.get(position);
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("beitrag_id", clicked.getID());
        intent.putExtra("user", currentUser);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
    }
}
