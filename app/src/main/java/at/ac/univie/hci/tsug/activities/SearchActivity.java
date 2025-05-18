package at.ac.univie.hci.tsug.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.User;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SEARCH_ACTIVITY";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private EditText etSearchQuery, etDateFrom, etDateTo;
    private RadioGroup rgCategory;
    private Spinner spinnerRecency;
    private ChipGroup chipGroupTags;
    private Button btnSearch;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        currentUser = getIntent().getParcelableExtra("user");

        //Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Intent intent;
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                intent = new Intent(this, HomeActivity.class);
            } else if (id == R.id.nav_neuer_beitrag) {
                intent = new Intent(this, CreateActivity.class);
            } else if (id == R.id.nav_account) {
                intent = new Intent(this, AccountActivity.class);
            } else return false;
            intent.putExtra("user", currentUser);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
            return true;
        });

        //Top Navigation
        ImageButton backNav = findViewById(R.id.nav_back);
        backNav.setOnClickListener(v -> finish());
        ImageButton setNav = findViewById(R.id.nav_einstellungen);
        setNav.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
        });

        //Init UI
        etSearchQuery = findViewById(R.id.et_search_query);
        rgCategory     = findViewById(R.id.rg_category);
        etDateFrom     = findViewById(R.id.et_date_from);
        etDateTo       = findViewById(R.id.et_date_to);
        spinnerRecency = findViewById(R.id.spinner_recency);
        chipGroupTags  = findViewById(R.id.chip_group_tags);
        btnSearch      = findViewById(R.id.btn_search);

        //Spinner Adapter - sonst spinnt es schon wieder
        ArrayAdapter<CharSequence> recencyAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.aktualitaet_options,
                android.R.layout.simple_spinner_item
        );
        recencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecency.setAdapter(recencyAdapter);

        //Default Auswahl vom Radiobutton
        rgCategory.check(R.id.rb_alles);

        //DatePicker - sehr angeberisch für das Datum
        etDateFrom.setOnClickListener(v -> showDatePicker(etDateFrom));
        etDateTo.setOnClickListener(v -> showDatePicker(etDateTo));

        //Chips aus Array
        String[] tags = getResources().getStringArray(R.array.tag_options);
        for (String tag : tags) {
            Chip chip = new Chip(this);
            chip.setText(tag);
            chip.setCheckable(true);
            chipGroupTags.addView(chip);
        }

        btnSearch.setOnClickListener(v -> applyFilters());
    }

    // und Start von SearchResultsActivity
    private void applyFilters() {
        String query = etSearchQuery.getText().toString().trim().toLowerCase(Locale.getDefault());
        int catId = rgCategory.getCheckedRadioButtonId();
        final String filterType;
        if (catId == R.id.rb_frage) {
            filterType = "FRAGE";
        } else if (catId == R.id.rb_tipp) {
            filterType = "TIPP";
        } else {
            filterType = null;
        }

        List<Post> allPosts = new ArrayList<>(Container.getAllPosts());
        //Debug ausgewählter Typ-Filter
        Log.d(TAG, "Selected filterType = " + filterType);
        Log.d(TAG, "Total posts: " + allPosts.size());
        //Debug Ausgeben aller Post-Typen
        for (Post p : allPosts) {
            Log.d(TAG, "AllPost: title='" + p.getTitle() + "', type=" + p.getType());
        }

        List<Post> filtered = allPosts.stream()
                .filter(p -> {
                    boolean matchesText = query.isEmpty()
                            || p.getTitle().toLowerCase().contains(query)
                            || p.getDes().toLowerCase().contains(query);
                    if (!matchesText) return false;
                    if (filterType != null && !p.getType().equalsIgnoreCase(filterType)) return false;
                    return true;
                })
                .collect(Collectors.toList());

        Log.d(TAG, "Filtered posts: " + filtered.size());

        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putParcelableArrayListExtra("results", new ArrayList<>(filtered));
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }

    //Zeigt einen DatePickerDialog und trägt das Ergebnis ins EditText ein.
    private void showDatePicker(final EditText target) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int day) -> {
                    String formatted = String.format(Locale.getDefault(), "%02d.%02d.%04d", day, month+1, year);
                    target.setText(formatted);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
