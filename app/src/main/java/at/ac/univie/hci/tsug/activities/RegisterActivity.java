package at.ac.univie.hci.tsug.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.elements.User;

public class RegisterActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Get new Info:
        EditText registerUsername = findViewById(R.id.register_username);
        EditText registerEmail = findViewById(R.id.register_email);
        EditText registerPassword = findViewById(R.id.register_password);

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = registerUsername.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Passwort, Email oder Username kann nicht leer gelassen werden!", Toast.LENGTH_SHORT).show();
                } else {
                    User newUser = new User(username, email, password);

                    if(Container.addUser(newUser)) {
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        intent.putExtra("user", newUser);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
                        finish();
                    }
                }
            }
        });
    }
}