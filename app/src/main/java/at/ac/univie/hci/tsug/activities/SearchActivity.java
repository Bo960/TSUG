package at.ac.univie.hci.tsug.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.MainActivity;
import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.elements.Post;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {
    private EditText etSearchQuery, etDateFrom, etDateTo;
    private RadioGroup rgCategory;
    private Spinner spinnerRecency;
    private ChipGroup chipGroupTags;
    private Button btnSearch;
    public String initalQuery;
    private String activityName = "Suchfilter";

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

        //Bottom Naviagtion:
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        //Homescreen
                        intent = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
                        return true;

                    case R.id.nav_neuer_beitrag:
                        //Beitrag erstellen Seite
                        intent = new Intent(SearchActivity.this, CreateActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
                        return true;

                    case R.id.nav_account:
                        //Account settings Seite
                        intent = new Intent(SearchActivity.this, AccountActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
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
            Intent intent = new Intent(SearchActivity.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
        });

        //TEXT
        TextView testText = findViewById(R.id.nav_text_testing);
        testText.setText(activityName);

        //activity Komponenten initialisieren
        etSearchQuery = findViewById(R.id.et_search_query);
        rgCategory     = findViewById(R.id.rg_category);
        etDateFrom     = findViewById(R.id.et_date_from);
        etDateTo       = findViewById(R.id.et_date_to);
        spinnerRecency = findViewById(R.id.spinner_recency);
        chipGroupTags  = findViewById(R.id.chip_group_tags);
        btnSearch      = findViewById(R.id.btn_search);
        spinnerRecency = findViewById(R.id.spinner_recency);

        //Adapter für Aktualität-Spinner setzen
        ArrayAdapter<CharSequence> recencyAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.aktualitaet_options,
                android.R.layout.simple_spinner_item
        );
        recencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecency.setAdapter(recencyAdapter);

        //Per Default ist Alles als Kategorie gewählt
        rgCategory.check(R.id.rb_alles);

        //Suchbegriff von MainActivity holen und vorfüllen - Ist manchmal als Fehler markiert und manchmal nicht WTF
        String initialQuery = MainActivity.getSimpleSearchTerm();
        if (initialQuery != null) {
            etSearchQuery.setText(initialQuery);
        }

        //DatePicker-Dialoge für Reisedatum - wenn es läuft -> mehr testen
        etDateFrom.setOnClickListener(v -> showDatePicker(etDateFrom));
        etDateTo.setOnClickListener(v   -> showDatePicker(etDateTo));

        //Tags aus arrays.xml als Chips erzeugen (Keine zum Essen, aber zum Draufdrücken)
        String[] tags = getResources().getStringArray(R.array.tag_options);
        for (String tag : tags) {
            Chip chip = new Chip(this);
            chip.setText(tag);
            chip.setCheckable(true);
            chipGroupTags.addView(chip);
        }

        //Such-Button Listener (richtig mit Ohren)
        btnSearch.setOnClickListener(v -> applyFilters());
        this.initalQuery= initialQuery;
    }

    //Filtert alle Posts anhand der Nutzerkriterien und startet die Ergebnis-Activity (-> activity_search_results.xml)
    private void applyFilters() {
        //LADEN VON DEN FILTERN
        //Freitext
        String query = etSearchQuery.getText().toString().trim().toLowerCase(Locale.getDefault());

        //Kategorie
        int catId = rgCategory.getCheckedRadioButtonId();
        String category = catId == R.id.rb_frage ? "Frage"
                : catId == R.id.rb_tipp  ? "Tipp"
                :                           "Alles";

        //Reisedatum
        String dateFrom = etDateFrom.getText().toString().trim();
        String dateTo   = etDateTo.getText().toString().trim();

        //Aktualität
        String recency = spinnerRecency.getSelectedItem().toString();

        //Tags
        List<String> selectedTags = new ArrayList<>();
        for (int i = 0; i < chipGroupTags.getChildCount(); i++) {
            Chip c = (Chip) chipGroupTags.getChildAt(i);
            if (c.isChecked()) selectedTags.add(c.getText().toString());
        }

        //Für Spinner, damit es nicht schon wieder spinnt...
        LocalDate now = LocalDate.now();
        LocalDate threshold = null;
        switch (recency) {
            case "Heute":
                threshold = now;
                break;
            case "Diese Woche":
                threshold = now.minusWeeks(1);
                break;
            case "Diesen Monat":
                threshold = now.minusMonths(1);
                break;
        }

        //ALLE POSTS LADEN UND FILTERN (d.h. Filter anwenden)
        List<Post> allPosts = new ArrayList<>(Container.getAllPosts()); //getAllPosts ist rot, aber es wird in activity_main.xml verwendet?! - MAAAAAAAAARRRRRRTTTTTTTTIIIIIIIINNNNNNNNNNNN
        LocalDate finalThreshold = threshold;
        List<Post> filtered = allPosts.stream()
                .filter(p -> {
                    //Textsuche in Titel oder Beschreibung
                    boolean matchesText = query.isEmpty()
                            || p.getTitle().toLowerCase().contains(query)
                            || p.getDes().toLowerCase().contains(query);
                    if (!matchesText) return false;

                    //Kategorie
                    if (!category.equals("Alles") && !p.getTags().contains(category)) return false;

                    //Datumsbereich (Als dd.mm.yyyy -> gehe davon aus)
                    if (!dateFrom.isEmpty() || !dateTo.isEmpty()) {
                        try {
                            java.time.LocalDate from = dateFrom.isEmpty()
                                    ? java.time.LocalDate.MIN
                                    : java.time.LocalDate.parse(dateFrom, java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                            java.time.LocalDate to = dateTo.isEmpty()
                                    ? java.time.LocalDate.MAX
                                    : java.time.LocalDate.parse(dateTo, java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                            if (p.getDate().isBefore(from) || p.getDate().isAfter(to)) return false;
                        } catch (Exception x) {
                            //ignorieren bei Parse-Fehlern
                        }
                    }

                    //Aktualität (Spinner) anwenden - wenn es denn mal geht...
                    if (finalThreshold != null && p.getDate().isBefore(finalThreshold)) return false;

                    //Tags
                    if (!selectedTags.isEmpty()) {
                        if (p.getTags().stream().noneMatch(selectedTags::contains)) return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        //Ergebnisse weiterreichen
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putParcelableArrayListExtra("results", new ArrayList<>(filtered)); //Hier manchmal rot markiert das new ArrayList...
        startActivity(intent);
    }

    //Zeigt einen DatePickerDialog und trägt das Ergebnis ins Ziel-EditText ein.
    private void showDatePicker(final EditText target) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (DatePicker view, int y, int m, int d) -> {
                    String formatted = String.format(
                            Locale.getDefault(), "%02d.%02d.%04d", d, m + 1, y
                    );
                    target.setText(formatted);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
