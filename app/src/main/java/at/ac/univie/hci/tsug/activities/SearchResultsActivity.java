package at.ac.univie.hci.tsug.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.User;

//Ohne Recyclerview, weil das zu Problemen geführt hat
public class SearchResultsActivity extends AppCompatActivity {
    private static final String TAG = "SEARCH_RESULTS";
    private LinearLayout llResults;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);
        Log.d(TAG, "--- onCreate start ---");

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        //Aktuellen Nutzer laden
        currentUser = getIntent().getParcelableExtra("user");

        //Top Navigation
        ImageButton navBack = findViewById(R.id.nav_back);
        navBack.setOnClickListener(v -> finish());
        ImageButton navSettings = findViewById(R.id.nav_einstellungen);
        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        llResults = findViewById(R.id.ll_results);

        //Zacher Fehler war hier: Parcelling liefert möglicherweise unvollständige Posts -> hol Original aus Container
        ArrayList<Post> parcelResults = getIntent().getParcelableArrayListExtra("results");
        ArrayList<Post> results = new ArrayList<>();
        if (parcelResults != null) {
            for (Post p : parcelResults) {
                results.add(Container.getPost(p.getID()));
            }
        }
        Log.d(TAG, "Received results count: " + results.size());

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Post use : results) {
            try {
                Log.d(TAG, "Rendering post: " + use.getTitle());
                View item = inflater.inflate(R.layout.post_view, llResults, false);

                //Typ und Titel
                TextView tvType = item.findViewById(R.id.rv_type);
                TextView tvTitle = item.findViewById(R.id.rv_title);
                tvType.setText(use.getType());
                tvTitle.setText(use.getTitle());

                //Route vs. Region
                View visRoute = item.findViewById(R.id.visRoute);
                View visRegion = item.findViewById(R.id.visRegion);
                if (use.isRegion()) {
                    visRegion.setVisibility(View.VISIBLE);
                    visRoute.setVisibility(View.GONE);
                    TextView tvRegion = item.findViewById(R.id.rv_region);
                    tvRegion.setText(use.getRegion());
                } else {
                    visRoute.setVisibility(View.VISIBLE);
                    visRegion.setVisibility(View.GONE);
                    TextView tvStart = item.findViewById(R.id.rv_start);
                    TextView tvEnd = item.findViewById(R.id.rv_end);
                    if (use.getRoute() != null) {
                        tvStart.setText(use.getRoute().first);
                        tvEnd.setText(use.getRoute().second);
                    }
                }

                // ikes
                TextView tvLikes = item.findViewById(R.id.rv_likes);
                tvLikes.setText(use.getLikesStyle());

                //Klick auf Post öffnet Detail
                item.setOnClickListener(v -> {
                    Intent i = new Intent(this, PostActivity.class);
                    i.putExtra("beitrag_id", use.getID());
                    i.putExtra("user", currentUser);
                    startActivity(i);
                });

                llResults.addView(item);
            } catch (Exception e) {
                Log.e(TAG, "Error rendering post: " + use.getTitle(), e);
            }
        }
        Log.d(TAG, "Populated llResults child count = " + llResults.getChildCount());
        Log.d(TAG, "--- onCreate end ---");
    }
}
