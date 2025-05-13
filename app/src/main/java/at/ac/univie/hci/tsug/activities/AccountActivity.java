package at.ac.univie.hci.tsug.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import at.ac.univie.hci.tsug.MainActivity;
import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.elements.User;

public class AccountActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    String activityName = "Konto";
    private User currentUser;

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
        usernameText.setText(currentUser.getName());

        TextView userIdText = findViewById(R.id.user_id);
        userIdText.setText(String.format("@%s", currentUser.getID()));

        TextView emailText = findViewById(R.id.email);
        emailText.setText(currentUser.getEmail());

        TextView rankingText = findViewById(R.id.ranking);
        rankingText.setText(currentUser.getRank());

        TextView questionsCountText = findViewById(R.id.questions_count);
        questionsCountText.setText(String.valueOf(currentUser.getQuestions()));

        TextView answersCountText = findViewById(R.id.answers_count);
        answersCountText.setText(String.valueOf(currentUser.getAnswers()));

        TextView likesCountText = findViewById(R.id.likes_count);
        likesCountText.setText(String.valueOf(currentUser.getLikes()));

        ((ImageView) findViewById(R.id.profile_image)).setImageResource(R.drawable.account);

        //TODO braucht man ein currentUser in UserContainer !!
        //currentUser = UserContainer.getCurrentUser();

        TextView likes = findViewById(R.id.likes);
        likes.setOnClickListener(v ->{
            Intent i= new Intent(this, LikedPostsActivity.class);
            startActivity(i);
            //Von Position-Links nach Position-Rechts
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        });
        TextView seen = findViewById(R.id.seen);
        seen.setOnClickListener(v ->{
            Intent i= new Intent(this, SeenPostsActivity.class);
            startActivity(i);
            //Von Position-Links nach Position-Rechts
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        });
        TextView created=findViewById(R.id.created);
        created.setOnClickListener(v ->{
            Intent i= new Intent(this, CreatedPostsActivity.class);
            startActivity(i);
            //Von Position-Links nach Position-Rechts
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        });

    }
}