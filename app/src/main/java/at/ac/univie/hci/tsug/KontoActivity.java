package at.ac.univie.hci.tsug;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

public class KontoActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    String activityName = "Konto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_konto);
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
                        intent = new Intent(KontoActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_neuer_beitrag:
                        //Beitrag erstellen Seite
                        intent = new Intent(KontoActivity.this, ErstellenActivity.class);
                        startActivity(intent);
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
            Intent intent = new Intent(KontoActivity.this, EinstellungActivity.class);
            startActivity(intent);
        });

        //TESTING TEXT TODO DELETE LATER
        TextView testText = findViewById(R.id.nav_text_testing);
        testText.setText(activityName);

        //Martin's Code für Bottom Navigation END
    }
}