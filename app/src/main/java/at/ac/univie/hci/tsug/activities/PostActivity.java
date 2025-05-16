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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.elements.Comment;
import at.ac.univie.hci.tsug.elements.CommentAdapter;
import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.User;

public class PostActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    String activityName = "Beitrag";
    public boolean postLiked = false;
    private User currentUser;

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

        //Recieving the ID
        int beitragID = getIntent().getIntExtra("beitrag_id", 0);
        Post createdPost = Container.getPost(beitragID);

        //Recieveing User from Home:
        currentUser = getIntent().getParcelableExtra("user");

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
                        intent = new Intent(PostActivity.this, HomeActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_neuer_beitrag:
                        // Beitrag erstellen Seite
                        intent = new Intent(PostActivity.this, CreateActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_account:
                        //Account settings Seite
                        intent = new Intent(PostActivity.this, AccountActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        //TEXT
        TextView testText = findViewById(R.id.nav_text_testing);
        testText.setText(activityName);

        // TOP NAVIGATION:
        ImageButton backNav = findViewById(R.id.nav_back);
        backNav.setOnClickListener(v -> finish());

        ImageButton setNav = findViewById(R.id.nav_einstellungen);
        setNav.setOnClickListener(v -> {
            Intent intent = new Intent(PostActivity.this, SettingsActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // Title
        TextView titleView = findViewById(R.id.titleView);
        String title = createdPost.getTitle();
        titleView.setText(title);

        // Likes
        TextView likesView = findViewById(R.id.likesView);
        ImageView likeIcon = findViewById(R.id.likeIcon);
        likesView.setText(createdPost.getLikes() + "");

        View.OnClickListener likeClickListener = v -> {
            // TODO prohibit save your own post
            postLiked = !postLiked;
            if (postLiked) {
                createdPost.addLike(currentUser);
                likeIcon.setImageResource(R.drawable.baseline_favorite_24);

                //Wenn geliked wird sollte der User der Post zu gen Favoriten hinzugefügt werden
                //currentUser.addLikedPost(createdPost.getID());

            } else {
                createdPost.unlike();
                likeIcon.setImageResource(R.drawable.baseline_favorite_border_24);

                //Wenn der Likeentfernt wird sollte er auch bei Favoriten entfernt werden.
                //currentUser.removeLikedPost(createdPost.getID());
            }
            likesView.setText(String.valueOf(createdPost.getLikes()));
        };
        likeIcon.setOnClickListener(likeClickListener);
        likesView.setOnClickListener(likeClickListener);

        // show author or edit
        TextView authorView = findViewById(R.id.authorView);
        ImageView authorIcon = findViewById(R.id.authorIcon);
        String author = createdPost.getUser().getName();
        authorView.setText(author);

        FloatingActionButton editPostBtn = findViewById(R.id.editPostBtn);
        FloatingActionButton deletePostBtn = findViewById(R.id.deletePostBtn);

        if (author.isEmpty()) { // TODO hardcoded
            editPostBtn.setVisibility(View.VISIBLE);
            deletePostBtn.setVisibility(View.VISIBLE);
            authorView.setVisibility(View.GONE);
            authorIcon.setVisibility(View.GONE);
        } else {
            editPostBtn.setVisibility(View.GONE);
            deletePostBtn.setVisibility(View.GONE);
            authorView.setVisibility(View.VISIBLE);
            authorIcon.setVisibility(View.VISIBLE);
        }

        // edit post
        editPostBtn.setOnClickListener(v -> {
            Toast.makeText(PostActivity.this, "Bearbeiten", Toast.LENGTH_SHORT).show();

            // TODO
            Intent intent = new Intent(PostActivity.this, CreateActivity.class);
            intent.putExtra("beitrag_id", createdPost.getID());
            intent.putExtra("is_edit", true);
            startActivity(intent);
        });

        // delete post
        deletePostBtn.setOnClickListener(v -> {
            Toast.makeText(PostActivity.this, "Beitrag gelöscht", Toast.LENGTH_SHORT).show();
            Container.removePost(beitragID);

            Intent intent = new Intent(PostActivity.this, MainActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
        });

        TextView startPlace = findViewById(R.id.startPlace);
        TextView endPlace = findViewById(R.id.endPlace);
        TextView region = findViewById(R.id.region);
        TextView descriptionView = findViewById(R.id.description);
        ImageView routeIcon = findViewById(R.id.routeIcon);
        ImageView regionIcon = findViewById(R.id.regionIcon);


        String regionTxt = createdPost.getRegion();
        String description = createdPost.getDes();


        // check if exact route is given or region
        if (regionTxt == "") {
            String start = createdPost.getRoute().first;
            String end = createdPost.getRoute().second;
            startPlace.setText(start);
            endPlace.setText(end);
            // show start & icon & end
            startPlace.setVisibility(View.VISIBLE);
            routeIcon.setVisibility(View.VISIBLE);
            endPlace.setVisibility(View.VISIBLE);
            // don't show region & icon
            regionIcon.setVisibility(View.GONE);
            region.setVisibility(View.GONE);
        } else {
            region.setText(regionTxt);
            regionIcon.setVisibility(View.VISIBLE);
            region.setVisibility(View.VISIBLE);

            startPlace.setVisibility(View.GONE);
            routeIcon.setVisibility(View.GONE);
            endPlace.setVisibility(View.GONE);
        }

        Chip frage = findViewById(R.id.frage);
        Chip tipp = findViewById(R.id.tipp);
        Chip guenstig = findViewById(R.id.guenstig);
        Chip preiswert = findViewById(R.id.preiswert);
        Chip nachtzug = findViewById(R.id.nachtzug);
        Chip sparangebot = findViewById(R.id.sparangebot);
        Chip flexibel = findViewById(R.id.flexibel);
        Chip gruppe = findViewById(R.id.gruppentarif);
        Chip direkt = findViewById(R.id.direkt);
        Chip kurzeFahrt = findViewById(R.id.kurzeFahrt);
        Chip langeFahrt = findViewById(R.id.langeFahrt);

        if (createdPost.getFrage() == "Frage") {
            frage.setVisibility(View.VISIBLE);
            tipp.setVisibility(View.GONE);
        } else {
            frage.setVisibility(View.GONE);
            tipp.setVisibility(View.VISIBLE);
        }
        // {"Günstig", "Preiswert", "Nachtzug", "Sparangebot", "Flexibel", "Gruppentarif", "Direkt", "Kurze Fahrt", "Lange Fahrt"}
        ArrayList<String> tags = createdPost.getTags();
        guenstig.setVisibility(View.GONE);
        preiswert.setVisibility(View.GONE);
        nachtzug.setVisibility(View.GONE);
        sparangebot.setVisibility(View.GONE);
        flexibel.setVisibility(View.GONE);
        gruppe.setVisibility(View.GONE);
        direkt.setVisibility(View.GONE);
        kurzeFahrt.setVisibility(View.GONE);
        langeFahrt.setVisibility(View.GONE);
        for (String tag : tags) {
            switch (tag) {
                case "Günstig":
                    guenstig.setVisibility(View.VISIBLE);
                    continue;
                case "Preiswert":
                    preiswert.setVisibility(View.VISIBLE);
                    continue;
                case "Nachtzug":
                    nachtzug.setVisibility(View.VISIBLE);
                    continue;
                case "Sparangebot":
                    sparangebot.setVisibility(View.VISIBLE);
                    continue;
                case "Flexibel":
                    flexibel.setVisibility(View.VISIBLE);
                    continue;
                case "Gruppentarif":
                    gruppe.setVisibility(View.VISIBLE);
                    continue;
                case "Direkt":
                    direkt.setVisibility(View.VISIBLE);
                    continue;
                case "Kurze Fahrt":
                    kurzeFahrt.setVisibility(View.VISIBLE);
                    continue;
                case "Lange Fahrt":
                    langeFahrt.setVisibility(View.VISIBLE);
            }
        }

        // description visibility
        if (description.isEmpty()) {
            // show start & icon & end
            descriptionView.setVisibility(View.GONE);
        } else {
            descriptionView.setText(description);
        }

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
                commentInput.setText("");
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(commentInput.getWindowToken(), 0);
                }
            }
        });

        commentAdapter.updateComments(commentArrayList);

        //Wenn das geladen wird bedeutet es der user sieht den beitrag. daher sollte es zu geshene beiträge hinzugefügt werden
        //currentUser.addSeenPost(postID);

        //Wenn die User übereinstimmen sollte sein Beitrag hinzugefügt werden zu seinen created Posts:
        //if(currentUser.equals(Container.getUser(postID)))
            //currentUser.addCreatedPost(postID);
    }
}