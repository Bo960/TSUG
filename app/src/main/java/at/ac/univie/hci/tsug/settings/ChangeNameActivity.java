package at.ac.univie.hci.tsug.settings;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.activities.AccountActivity;
import at.ac.univie.hci.tsug.activities.CreateActivity;
import at.ac.univie.hci.tsug.activities.HomeActivity;
import at.ac.univie.hci.tsug.activities.SettingsActivity;
import at.ac.univie.hci.tsug.elements.User;

public class ChangeNameActivity extends AppCompatActivity {

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_name);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Recieveing User from Home:
        currentUser = getIntent().getParcelableExtra("user");

        //Get new Name:
        EditText password = findViewById(R.id.password_name);
        EditText newName = findViewById(R.id.new_username);
        Button accept = findViewById(R.id.username_changed);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Match password with account
                if (currentUser.getPassword().equals(password.getText().toString())) {
                    currentUser.setName(newName.getText().toString());

                    Intent intent = new Intent(ChangeNameActivity.this, SettingsActivity.class);
                    intent.putExtra("user", currentUser);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                } else {
                    Toast.makeText(ChangeNameActivity.this,
                            "Passwort is wrong!",
                            LENGTH_SHORT).show();
                }
            }
        });

        //Cancel Button:
        Button backNav = findViewById(R.id.cancel_change);
        backNav.setOnClickListener(v -> finish());
    }
}