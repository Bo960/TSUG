package at.ac.univie.hci.tsug.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.User;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    private LinearLayout llResults;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        //Container für Ergebnisse
        llResults = findViewById(R.id.ll_results);

        //Ergebnisse aus Intent auslesen
        //Da getParcelableArrayListExtra ev. ArrayList<Parcelable> liefert, wandeln wir es manuell um
        ArrayList<Post> results = new ArrayList<>();
        ArrayList<?> rawList = getIntent().getParcelableArrayListExtra("results");
        if (rawList != null) {
            for (Object obj : rawList) {
                if (obj instanceof Post) {
                    results.add((Post) obj);
                }
            }
        }

        if (!results.isEmpty()){
            for (Post p : results) {
                //Titel
                TextView tvTitle = new TextView(this);
                tvTitle.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tvTitle.setText(p.getTitle());
                tvTitle.setTextSize(18);
                tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

                //Beschreibung
                TextView tvDesc = new TextView(this);
                tvDesc.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tvDesc.setText(p.getDes());
                tvDesc.setPadding(0, 0, 0, 16);

                llResults.addView(tvTitle);
                llResults.addView(tvDesc);
            }
        } else {
            TextView tvNone = new TextView(this);
            tvNone.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tvNone.setText("Keine Ergebnisse gefunden.");
            llResults.addView(tvNone);
        }
    }
}
