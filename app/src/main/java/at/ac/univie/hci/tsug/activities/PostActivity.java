package at.ac.univie.hci.tsug.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import at.ac.univie.hci.tsug.activities.CreateActivity;
import at.ac.univie.hci.tsug.MainActivity;
import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.elements.Comment;
import at.ac.univie.hci.tsug.elements.CommentAdapter;
import at.ac.univie.hci.tsug.elements.User;

public class PostActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    String activityName = "Beitrag";
    public boolean postLiked = false;
    private int likes = 55; // TODO hardcoded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bottom Navigation
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        // Homescreen
                        intent = new Intent(PostActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_neuer_beitrag:
                        // Beitrag erstellen Seite
                        intent = new Intent(PostActivity.this, CreateActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_account:
                        //Account settings Seite
                        intent = new Intent(PostActivity.this, AccountActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        //TESTING TEXT
        TextView testText = findViewById(R.id.nav_text_testing);
        testText.setText(activityName);

        // TOP NAVIGATION:
        ImageButton backNav = findViewById(R.id.nav_back);
        backNav.setOnClickListener(v -> finish());

        ImageButton setNav = findViewById(R.id.nav_einstellungen);
        setNav.setOnClickListener(v -> {
            Intent intent = new Intent(PostActivity.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // Title
        TextView titleView = findViewById(R.id.titleView);
        String title = "TITLE"; // TODO hardcoded
        titleView.setText(title);

        // Likes
        TextView likesView = findViewById(R.id.likesView);
        likesView.setText(likes + "");
        ImageView likeIcon = findViewById(R.id.likeIcon);

        View.OnClickListener likeClickListener = v -> {
            postLiked = !postLiked;
            if (postLiked) {
                likes++;
                likeIcon.setImageResource(R.drawable.baseline_favorite_24); // Gefülltes Herz
            } else {
                likes--;
                likeIcon.setImageResource(R.drawable.baseline_favorite_border_24); // Leeres Herz
            }
            likesView.setText(String.valueOf(likes));
        };
        likeIcon.setOnClickListener(likeClickListener);
        likesView.setOnClickListener(likeClickListener); // click on number

        // show author or edit
        TextView authorView = findViewById(R.id.authorView);
        ImageView authorIcon = findViewById(R.id.authorIcon);

        FloatingActionButton editPostBtn = findViewById(R.id.editPostBtn);

        if (authorView.toString().isEmpty()) { // TODO hardcoded
            editPostBtn.setVisibility(View.VISIBLE);
            authorView.setVisibility(View.GONE);
            authorIcon.setVisibility(View.GONE);
        } else {
            editPostBtn.setVisibility(View.GONE);
            authorView.setVisibility(View.VISIBLE);
            authorIcon.setVisibility(View.VISIBLE);
        }

        // edit post
        editPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostActivity.this, "Bearbeiten geklickt", Toast.LENGTH_SHORT).show();

                // Beispiel: Starte eine Bearbeiten-Aktivität
                // Intent intent = new Intent(PostActivity.this, EditPostActivity.class);
                // startActivity(intent);
            }
        });

        TextView startPlace = findViewById(R.id.startPlace);
        TextView endPlace = findViewById(R.id.endPlace);
        TextView region = findViewById(R.id.region);
        ImageView routeIcon = findViewById(R.id.routeIcon);
        ImageView regionIcon = findViewById(R.id.regionIcon);

        // TODO hardcoded
        String start = "START";
        String end = "END";
        String regionTxt = "";

        // check if exact route is given or region
        if (regionTxt == "") {
            // show start & icon & end
            startPlace.setVisibility(View.VISIBLE);
            routeIcon.setVisibility(View.VISIBLE);
            endPlace.setVisibility(View.VISIBLE);
            // don't show region & icon
            regionIcon.setVisibility(View.GONE);
            region.setVisibility(View.GONE);
        } else {
            regionIcon.setVisibility(View.VISIBLE);
            region.setVisibility(View.VISIBLE);

            startPlace.setVisibility(View.GONE);
            routeIcon.setVisibility(View.GONE);
            endPlace.setVisibility(View.GONE);
        }

        if (regionTxt != "") {
            region.setText(regionTxt);
        } else {
            startPlace.setText(start);
            endPlace.setText(end);
        }

        // TODO tags

        // Comments
        ListView commentListView = findViewById(R.id.commentListView);
        List<Comment> commentArrayList = new ArrayList<>();
        CommentAdapter commentAdapter = new CommentAdapter(this, commentArrayList);
        commentListView.setAdapter(commentAdapter);
        // TODO kommentare speichern?

        // input: new comment
        EditText commentInput = findViewById(R.id.commentInput);
        Button sendCommentBtn = findViewById(R.id.sendCommentBtn);
        sendCommentBtn.setOnClickListener(v -> {
            String text = commentInput.getText().toString().trim();
            if (!text.isEmpty()) {
                Comment newComment = new Comment(new User("John", "john@gmail.com", "123456789"), text); // TODO hardcoded author
                commentArrayList.add(0, newComment);
                commentAdapter.notifyDataSetChanged();
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(commentInput.getWindowToken(), 0);
                }
            }
        });

        commentAdapter.updateComments(commentArrayList);
    }
}