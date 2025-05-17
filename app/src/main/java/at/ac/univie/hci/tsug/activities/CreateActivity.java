package at.ac.univie.hci.tsug.activities;

import static at.ac.univie.hci.tsug.container.Container.getUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.elements.Comment;
import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.elements.User;

public class CreateActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    String activityName = "Erstellen";
    Button buttonSelectTags;
    String[] tags = {"Günstig", "Preiswert", "Nachtzug", "Sparangebot", "Flexibel", "Gruppentarif", "Direkt", "Kurze Fahrt", "Lange Fahrt"};
    boolean[] selectedTags = new boolean[tags.length];
    ArrayList<String> selectedTagList = new ArrayList<>();
    String selectedFrageTipp = "";
    private User currentUser;
    private ArrayList<Comment> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        bottomNav = findViewById(R.id.bottom_navigation);

        //Recieveing User from Home:
        currentUser = getIntent().getParcelableExtra("user");

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        //Homescreen
                        intent = new Intent(CreateActivity.this, HomeActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        //Von Position-Rechts nach Position-Links
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        return true;

                    case R.id.nav_account:
                        //Account settings Seite
                        intent = new Intent(CreateActivity.this, AccountActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        //Von Position-Links nach Position-Rechts
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
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
            Intent intent = new Intent(CreateActivity.this, SettingsActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
        });

        AtomicReference<Boolean> isEdit = new AtomicReference<>(getIntent().getBooleanExtra("is_edit", false));

        // tag frage / tipp
        Spinner tagSpinner = findViewById(R.id.tagFrageOderTipp);
        TextView spinnerErrorText = findViewById(R.id.spinnerErrorText);
        String[] tagsArray = {"Frage", "Tipp"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tagsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(adapter);

        // if editing: selected type & other fields
        if (isEdit.get()) {
            int beitragID = getIntent().getIntExtra("beitrag_id", 1);
            Post editPost = Container.getPost(beitragID);
            commentList = editPost.getCommentList();

            int tagIndex = Arrays.asList(tagsArray).indexOf(editPost.getFrage());
            tagSpinner.setSelection(tagIndex);

            selectedTagList = editPost.getTags();

            EditText input_start = findViewById(R.id.inputStart);
            EditText input_end = findViewById(R.id.inputEnd);
            EditText input_region = findViewById(R.id.inputRegion);
            EditText input_title = findViewById(R.id.inputTitle);
            EditText input_description = findViewById(R.id.inputDescription);

            if (editPost.getRegion().isEmpty()) {
                input_start.setText(editPost.getRoute().first);
                input_end.setText(editPost.getRoute().second);
            } else {
                input_region.setText(editPost.getRegion());
            }
            input_title.setText(editPost.getTitle());
            input_description.setText(editPost.getDes());

        }

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedFrageTipp = parentView.getItemAtPosition(position).toString();
                spinnerErrorText.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedFrageTipp = null;
                spinnerErrorText.setText("Bitte eine Kategorie auswählen.");
                spinnerErrorText.setVisibility(View.VISIBLE);
            }
        });

        // button categories
        buttonSelectTags = findViewById(R.id.buttonSelectTags);
        // if editing: selected items
        if (isEdit.get()) {
            for (int i = 0; i < tags.length; i++) {
                selectedTags[i] = selectedTagList.contains(tags[i]);
            }
        }
        buttonSelectTags.setOnClickListener(view -> {
            // if editing: to remember selected items
            for (int i = 0; i < tags.length; i++) {
                selectedTags[i] = selectedTagList.contains(tags[i]);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
            builder.setTitle("Tags auswählen");
            builder.setMultiChoiceItems(tags, selectedTags, (dialog, which, isChecked) -> selectedTags[which] = isChecked);
            builder.setPositiveButton("OK", (dialog, which) -> {
                selectedTagList.clear();
                for (int i = 0; i < tags.length; i++) {
                    if (selectedTags[i]) selectedTagList.add(tags[i]);
                }
            });
            builder.show();
        });

        // publish & go to post if correct
        Button publishButton = findViewById(R.id.publishBtn);
        publishButton.setOnClickListener(v -> {
            EditText input_start = findViewById(R.id.inputStart);
            EditText input_end = findViewById(R.id.inputEnd);
            EditText input_region = findViewById(R.id.inputRegion);
            EditText input_title = findViewById(R.id.inputTitle);
            EditText input_description = findViewById(R.id.inputDescription);

            String title = input_title.getText().toString();
            String startText = input_start.getText().toString();
            String endText = input_end.getText().toString();
            String regionText = input_region.getText().toString();
            String description = input_description.getText().toString();

            Boolean isFrage = true;
            if (selectedFrageTipp == "Tipp")
                isFrage = false;

            boolean valid = true;

            input_start.setError(null);
            input_end.setError(null);
            input_region.setError(null);
            input_title.setError(null);

            // Validation
            if ((startText.isEmpty() || endText.isEmpty()) && regionText.isEmpty()) {
                valid = false;
                if (startText.isEmpty())
                    input_start.setError("Bitte entweder Start & Ziel angeben oder Region");
                if (endText.isEmpty())
                    input_end.setError("Bitte entweder Start & Ziel angeben oder Region");
                input_region.setError("Bitte entweder Start & Ziel angeben oder Region");
            }

            if ((!startText.isEmpty() || !endText.isEmpty()) && !regionText.isEmpty()) {
                valid = false;
                if (!startText.isEmpty())
                    input_start.setError("Bitte entweder Start & Ziel angeben oder Region");
                if (!endText.isEmpty())
                    input_end.setError("Bitte entweder Start & Ziel angeben oder Region");
                input_region.setError("Bitte enweder Start & Ziel angeben oder Region");
            }

            if (title.isEmpty()) {
                valid = false;
                input_title.setError("Bitte Titel angeben");
            }

            if (valid) {
                // if only editing
                if (isEdit.get()) {
                    int beitragID = getIntent().getIntExtra("beitrag_id", 1);
                    Post editPost = Container.getPost(beitragID);

                    editPost.setTitle(title);
                    if (regionText.isEmpty()) {
                        editPost.setRoute(new Pair<>(startText, endText));
                    } else {
                        editPost.setRegion(regionText);
                    }
                    editPost.setDes(description);
                    // editPost.setCommentList(commentList);
                    editPost.setTags(selectedTagList);
                    editPost.setFrage(isFrage);

                    Container.updatePost(editPost);
                    isEdit.set(false);

                    Intent intent = new Intent(CreateActivity.this, PostActivity.class);
                    intent.putExtra("beitrag_id", editPost.getID());
                    intent.putExtra("user", currentUser);
                    startActivity(intent);
                } else {
                    Post createdPost;
                    if (regionText.isEmpty()) {
                        createdPost = new Post(title,
                                0,
                                currentUser,
                                isFrage,
                                selectedTagList,
                                new Pair<>(startText, endText),
                                description, commentList);
                    } else {
                        createdPost = new Post(title,
                                0,
                                currentUser,
                                isFrage,
                                selectedTagList,
                                regionText,
                                description, commentList);
                    }
                    Container.addPost(createdPost);

                    // Fragezähler erhöhen
                    if (createdPost.getFrage() == "Frage") {
                        currentUser.newQuestion();
                    }
                    Intent intent = new Intent(CreateActivity.this, PostActivity.class);
                    intent.putExtra("beitrag_id", createdPost.getID());
                    intent.putExtra("user", currentUser);
                    startActivity(intent);
                }
            }
        });
    }
}