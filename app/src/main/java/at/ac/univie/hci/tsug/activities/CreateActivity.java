package at.ac.univie.hci.tsug;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import at.ac.univie.hci.tsug.MainActivity;
import at.ac.univie.hci.tsug.R;

public class CreateActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    String activityName = "Erstellen";
    Button buttonSelectTags;
    String[] tags = {"Günstig", "Preiswert", "Nachtzug", "Sparangebot", "Flexibel", "Gruppentarif", "Direkt", "Kurze Fahrt", "Lange Fahrt"};
    boolean[] selectedTags = new boolean[tags.length];
    List<String> selectedTagList = new ArrayList<>();
    private String selectedFrageTipp;

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



        //Martin's Code für Bottom Navigation START

        bottomNav = findViewById(R.id.bottom_navigation);

        //bottomNav.setOnApplyWindowInsetsListener(null);
        //bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        //Homescreen
                        intent = new Intent(CreateActivity.this, MainActivity.class);
                        startActivity(intent);
                        //Von Position-Rechts nach Position-Links
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        return true;

                    case R.id.nav_account:
                        //Account settings Seite
                        intent = new Intent(CreateActivity.this, AccountActivity.class);
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
            startActivity(intent);
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
        });

        EditText start = findViewById(R.id.inputStart);
        EditText end = findViewById(R.id.inputEnd);
        EditText region = findViewById(R.id.inputRegion);
        String startText = start.getText().toString().trim();
        String endText = end.getText().toString().trim();
        String regionText = region.getText().toString().trim();

        if (((startText.isEmpty() || endText.isEmpty()) && regionText.isEmpty()) || ((!startText.isEmpty() || !endText.isEmpty()) && !regionText.isEmpty())) {
            start.setError("Enweder Start und Ziel ODER Region erforderlich"); // TODO
            end.setError("Enweder Start und Ziel ODER Region erforderlich");
            region.setError("Enweder Start und Ziel ODER Region erforderlich");
        }

        // tag frage / tipp
        Spinner tagSpinner = findViewById(R.id.tagFrageOderTipp);
        TextView spinnerErrorText = findViewById(R.id.spinnerErrorText);
        String[] tagsArray = {"Frage", "Tipp"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tagsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Dropdown
        tagSpinner.setAdapter(adapter);
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
        buttonSelectTags.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
            builder.setTitle("Tags auswählen");
            builder.setMultiChoiceItems(tags, selectedTags, (dialog, which, isChecked) -> {
                selectedTags[which] = isChecked;
            });
            builder.setPositiveButton("OK", (dialog, which) -> {
                selectedTagList.clear();
                for (int i = 0; i < tags.length; i++) {
                    if (selectedTags[i]) {
                        selectedTagList.add(tags[i]);
                    }
                }
            });
            builder.show();
        });

        EditText input_title = findViewById(R.id.inputTitle);
        String title = input_title.getText().toString();
        EditText input_description = findViewById(R.id.inputDescription);
        String description = input_description.getText().toString();


        // publish & go to post
        Button publishButton = findViewById(R.id.publishBtn);
        publishButton.setOnClickListener(v -> {
            Intent intent = new Intent(CreateActivity.this, PostActivity.class);
            // intent.putExtra("beitrag_id", 42); // TODO id
            // in Post: int beitragID = getIntent().getIntExtra("titel_id", -1);
            startActivity(intent);
        });

        //Martin's Code für Bottom Navigation END
    }
}