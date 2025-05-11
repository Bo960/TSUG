package at.ac.univie.hci.tsug;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class AccountActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    String activityName = "Konto";
    // für Testen
    String userId = "123456";
    String userName = "Max Mustermann";
    String email = "max@gmail.com";
    int questionsCount = 10;
    int answersCount = 12;
    int likesCount = 8;
    int points= questionsCount*2+answersCount*3+likesCount;
    int ranking = (int) Math.floor(points/50.0)+1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        //Homescreen
                        intent = new Intent(AccountActivity.this, MainActivity.class);
                        startActivity(intent);
                        //Von Position-Rechts nach Position-Links
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        return true;

                    case R.id.nav_neuer_beitrag:
                        //Beitrag erstellen Seite
                        intent = new Intent(AccountActivity.this, CreateActivity.class);
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
            Intent intent = new Intent(AccountActivity.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
        });

        TextView usernameText = findViewById(R.id.username);
        usernameText.setText(userName);

        TextView userIdText = findViewById(R.id.user_id);
        userIdText.setText(String.format("@%s", userId));

        TextView emailText = findViewById(R.id.email);
        emailText.setText(email);

        TextView rankingText = findViewById(R.id.ranking);
        rankingText.setText(String.valueOf("#"+ ranking));

        TextView questionsCountText = findViewById(R.id.questions_count);
        questionsCountText.setText(String.valueOf(questionsCount));

        TextView answersCountText = findViewById(R.id.answers_count);
        answersCountText.setText(String.valueOf(answersCount));

        TextView likesCountText = findViewById(R.id.likes_count);
        likesCountText.setText(String.valueOf(likesCount));

        ((ImageView) findViewById(R.id.profile_image)).setImageResource(R.drawable.account);

        TextView likes = findViewById(R.id.likes);
        likes.setOnClickListener(v ->{
            Intent i= new Intent(this, LikedPostsActivity.class);
            startActivity(i);
        });
        TextView seen = findViewById(R.id.seen);
        seen.setOnClickListener(v ->{
            Intent i= new Intent(this, SeenPostsActivity.class);
            startActivity(i);
        });
        TextView created=findViewById(R.id.created);
        created.setOnClickListener(v ->{
            Intent i= new Intent(this, CreatedPostsActivity.class);
            startActivity(i);
        });

    }
}