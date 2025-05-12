package at.ac.univie.hci.tsug.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import at.ac.univie.hci.tsug.MainActivity;
import at.ac.univie.hci.tsug.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private EditText etSearchQuery, etDateFrom, etDateTo;
    private RadioGroup rgCategory;
    private Spinner spinnerRecency;
    private ChipGroup chipGroupTags;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        // Edge-to-edge Padding
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        // UI-Komponenten initialisieren
        etSearchQuery = findViewById(R.id.et_search_query);
        rgCategory     = findViewById(R.id.rg_category);
        etDateFrom     = findViewById(R.id.et_date_from);
        etDateTo       = findViewById(R.id.et_date_to);
        spinnerRecency = findViewById(R.id.spinner_recency);
        chipGroupTags  = findViewById(R.id.chip_group_tags);
        btnSearch      = findViewById(R.id.btn_search);

        // Default-Kategorie: "Alles"
        rgCategory.check(R.id.rb_alles);

        // Suchbegriff von MainActivity holen und vorfüllen
        String initialQuery = MainActivity.getSimpleSearchTerm();
        if (initialQuery != null) {
            etSearchQuery.setText(initialQuery);
        }

        // DatePicker-Dialoge für Reisedatum
        etDateFrom.setOnClickListener(v -> showDatePicker(etDateFrom));
        etDateTo.setOnClickListener(v   -> showDatePicker(etDateTo));

        // Tags aus arrays.xml als Chips erzeugen
        String[] tags = getResources().getStringArray(R.array.tag_options);
        for (String tag : tags) {
            Chip chip = new Chip(this);
            chip.setText(tag);
            chip.setCheckable(true);
            chipGroupTags.addView(chip);
        }

        // Such-Button Listener
        btnSearch.setOnClickListener(v -> {
            // a) Freitext (optional)
            String query = etSearchQuery.getText().toString().trim();

            // b) Kategorie (immer gesetzt)
            int catId = rgCategory.getCheckedRadioButtonId();
            String category = catId == R.id.rb_frage ? "Frage"
                    : catId == R.id.rb_tipp  ? "Tipp"
                    :                           "Alles";

            // c) Reisedatum (leer lassen, wenn nicht gesetzt)
            String dateFrom = etDateFrom.getText().toString().trim();
            String dateTo   = etDateTo.getText().toString().trim();

            // d) Aktualität (immer eine Auswahl via Spinner)
            String recency = spinnerRecency.getSelectedItem().toString();

            // e) Tags (kann leer sein)
            ArrayList<String> selectedTags = new ArrayList<>();
            for (int i = 0; i < chipGroupTags.getChildCount(); i++) {
                Chip c = (Chip) chipGroupTags.getChildAt(i);
                if (c.isChecked()) {
                    selectedTags.add(c.getText().toString());
                }
            }

            // Neue Activity starten und Filterkriterien übergeben
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra("query", query);
            intent.putExtra("category", category);
            intent.putExtra("dateFrom", dateFrom);
            intent.putExtra("dateTo", dateTo);
            intent.putExtra("recency", recency);
            intent.putStringArrayListExtra("tags", selectedTags);
            startActivity(intent);
        });
    }

    /**
     * Zeigt einen DatePickerDialog und trägt das Ergebnis ins Ziel-EditText ein.
     */
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
