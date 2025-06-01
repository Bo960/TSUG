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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

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

        // date
        TextView dateView = findViewById(R.id.date);
        String date = createdPost.getDate().toString();
        dateView.setText("Erstellt/bearbeitet: " + date);

        // Title
        TextView titleView = findViewById(R.id.titleView);
        String title = createdPost.getTitle();
        titleView.setText(title);

        // Likes
        TextView likesView = findViewById(R.id.likesView);
        ImageView likeIcon = findViewById(R.id.likeIcon);
        likesView.setText(createdPost.getLikes() + "");

        // check if post is liked by current user
        if (currentUser.hasLiked(createdPost.getID())) {
            likeIcon.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            likeIcon.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        View.OnClickListener likeClickListener = v -> {
            if (currentUser.getID() != createdPost.getUser().getID()) {
                postLiked = !postLiked;
                User postOwner = createdPost.getUser();
                if (postLiked && !currentUser.hasLiked(createdPost.getID())) {
                    createdPost.like();
                    Container.updatePost(createdPost);
                    currentUser.addLikedPost(createdPost.getID());
                    // Like-Zähler erhöhen
                    postOwner.newLike();
                    Container.updateUser(postOwner);
                    likeIcon.setImageResource(R.drawable.baseline_favorite_24);
                } else if (!postLiked && currentUser.hasLiked(createdPost.getID())) {
                    createdPost.unlike();
                    Container.updatePost(createdPost);
                    currentUser.removeLikedPost(createdPost.getID());
                    // Like-Zähler niedriger
                    postOwner.lostLike();
                    Container.updateUser(postOwner);
                    likeIcon.setImageResource(R.drawable.baseline_favorite_border_24);
                }
                likesView.setText(String.valueOf(createdPost.getLikes()));
            } else {
                // Eigener Beitrag kann nicht geliked werden
                Toast.makeText(this, "Du kannst deinen eigenen Beitrag nicht liken!", Toast.LENGTH_SHORT).show();
            }
        };
        likeIcon.setOnClickListener(likeClickListener);
        likesView.setOnClickListener(likeClickListener);

        // show author or edit/delete buttons
        TextView authorView = findViewById(R.id.authorView);
        ImageView authorIcon = findViewById(R.id.authorIcon);
        String author = createdPost.getUser().getName();
        authorView.setText(author);

        FloatingActionButton editPostBtn = findViewById(R.id.editPostBtn);
        FloatingActionButton deletePostBtn = findViewById(R.id.deletePostBtn);

        if (currentUser.equals(createdPost.getUser())) {
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
            Intent intent = new Intent(PostActivity.this, CreateActivity.class);
            intent.putExtra("beitrag_id", createdPost.getID());
            intent.putExtra("is_edit", true);
            intent.putExtra("user", currentUser);
            startActivity(intent);
        });

        // delete post
        deletePostBtn.setOnClickListener(v -> {
            Toast.makeText(PostActivity.this, "Beitrag gelöscht", Toast.LENGTH_SHORT).show();
            Container.removePost(beitragID);
            Intent intent = new Intent(PostActivity.this, HomeActivity.class);
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
        Chip nachtzug = findViewById(R.id.nachtzug);
        Chip flexibel = findViewById(R.id.flexibel);
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

        // visibility of tags
        ArrayList<String> tags = createdPost.getTags();
        guenstig.setVisibility(View.GONE);
        nachtzug.setVisibility(View.GONE);
        flexibel.setVisibility(View.GONE);
        direkt.setVisibility(View.GONE);
        kurzeFahrt.setVisibility(View.GONE);
        langeFahrt.setVisibility(View.GONE);
        for (String tag : tags) {
            switch (tag) {
                case "Günstig":
                    guenstig.setVisibility(View.VISIBLE);
                    continue;
                case "Nachtzug":
                    nachtzug.setVisibility(View.VISIBLE);
                    continue;
                case "Flexibel":
                    flexibel.setVisibility(View.VISIBLE);
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

        // comments
        RecyclerView commentRecyclerView = findViewById(R.id.commentRecyclerView);
        CommentAdapter commentAdapter = new CommentAdapter(this, new ArrayList<>(createdPost.getCommentList()));
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);

        // Swipe to delete / edit
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Comment comment = commentAdapter.getCommentAt(position);

                if (!comment.getUser().equals(currentUser)) {
                    Toast.makeText(commentRecyclerView.getContext(),
                            "Nur eigene Kommentare können bearbeitet oder gelöscht werden", Toast.LENGTH_SHORT).show();
                    commentAdapter.notifyItemChanged(position);
                    return;
                }

                if (direction == ItemTouchHelper.LEFT) {
                    // delete comment
                    new AlertDialog.Builder(commentRecyclerView.getContext())
                            .setTitle("Kommentar löschen")
                            .setMessage("Willst du diesen Kommentar wirklich löschen?")
                            .setPositiveButton("Ja", (dialog, which) -> {
                                createdPost.getCommentList().remove(position);
                                commentAdapter.updateComments(new ArrayList<>(createdPost.getCommentList()));
                            })
                            .setNegativeButton("Abbrechen", (dialog, which) -> {
                                commentAdapter.notifyItemChanged(position);
                                dialog.dismiss();
                            })
                            .setCancelable(false)
                            .show();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // edit comment
                    EditText editText = new EditText(commentRecyclerView.getContext());
                    editText.setText(comment.getCommentText());
                    editText.setSelection(editText.getText().length());

                    new AlertDialog.Builder(commentRecyclerView.getContext())
                            .setTitle("Kommentar bearbeiten")
                            .setView(editText)
                            .setPositiveButton("Speichern", (dialog, which) -> {
                                String newText = editText.getText().toString().trim();
                                if (!newText.equals("")) {
                                    comment.setCommentText(newText);
                                    commentAdapter.updateComments(new ArrayList<>(createdPost.getCommentList()));
                                } else {
                                    Toast.makeText(commentRecyclerView.getContext(), "Kommentar darf nicht leer sein", Toast.LENGTH_SHORT).show();
                                    commentAdapter.notifyItemChanged(position);
                                }
                            })
                            .setNegativeButton("Abbrechen", (dialog, which) -> {
                                commentAdapter.notifyItemChanged(position);
                                dialog.dismiss();
                            })
                            .setCancelable(false)
                            .show();
                }
            }
        };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(commentRecyclerView);


        // input: new comment
        EditText commentInput = findViewById(R.id.commentInput);
        Button sendCommentBtn = findViewById(R.id.sendCommentBtn);
        sendCommentBtn.setOnClickListener(v -> {
            String text = commentInput.getText().toString().trim();
            if (!text.isEmpty()) {
                createdPost.addComment(new Comment(currentUser, text));
                currentUser.newAnswer(); // increase response count
                commentAdapter.updateComments((List<Comment>) createdPost.getCommentList().clone());
                commentInput.setText("");
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(commentInput.getWindowToken(), 0);
                }
            }
        });
        commentAdapter.updateComments(createdPost.getCommentList());

        // Beitrag als gesehen markieren
        if (currentUser != null && currentUser.getID() != createdPost.getUser().getID()) {
            currentUser.addSeenPost(beitragID);
        }

        // Beitrag als erstellt markieren
        if (currentUser != null && currentUser.getID() == createdPost.getUser().getID()) {
            currentUser.addCreatedPost(beitragID);
        }
    }
}