package at.ac.univie.hci.tsug.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.elements.User;

public class CreatedPostsActivity extends AppCompatActivity {
    private ArrayList<HistoryPost> createdPostsList;
    private ListView createdPostsListView;
    private HistoryPostAdapter postAdapter;
    private String activityName = "Erstellte Beiträge";
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_posts);

        //Recieveing User from Home:
        currentUser = getIntent().getParcelableExtra("user");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        //Homescreen
                        intent = new Intent(CreatedPostsActivity.this, HomeActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        //Von Position-Rechts nach Position-Links
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        return true;

                    case R.id.nav_neuer_beitrag:
                        //Beitrag erstellen Seite
                        intent = new Intent(CreatedPostsActivity.this, CreateActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        //Von Position-Rechts nach Position-Links
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        return true;

                    case R.id.nav_account:
                        //Account settings Seite
                        intent = new Intent(CreatedPostsActivity.this, AccountActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        //Von Position-Rechts nach Position-Links
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        return true;
                }
                return false;
            }
        });

        //TOP NAVIGATION:
        ImageButton backNav = findViewById(R.id.nav_back);
        backNav.setOnClickListener(v -> finish());

        ImageButton setNav = findViewById(R.id.nav_einstellungen);
        setNav.setOnClickListener(v -> {
            Intent intent = new Intent(CreatedPostsActivity.this, SettingsActivity.class);

            startActivity(intent);
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
        });

        //TEXT
        TextView testText = findViewById(R.id.nav_text_testing);
        testText.setText(activityName);

        createdPostsListView = findViewById(R.id.created_questions_list);
        createdPostsList= new ArrayList<>();
        //TODO currentUser auch in usercontainer
        /*User currentUser = Container.getCurrentUser();

        for (int postId : currentUser.getCreatedPosts()) {
            Post post = Container.get(postId);
            if (post != null) {
                createdPostsList.add(new HistoryPost(post.getTitel(), post.getDes(), post.getUser()));
            }
        }*/
        postAdapter= new HistoryPostAdapter(this, createdPostsList);
        createdPostsListView.setAdapter(postAdapter);
    }
}
