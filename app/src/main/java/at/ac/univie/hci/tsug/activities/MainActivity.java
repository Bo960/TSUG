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

import java.util.Objects;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.elements.User;

public class MainActivity extends AppCompatActivity {

    private String username;
    private String password;
    public User logedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Get Login data:
        EditText loginUsername = findViewById(R.id.login_username);
        EditText loginPassword = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = String.valueOf(loginUsername.getText());
                password = String.valueOf(loginPassword.getText());

                User user = Container.getUser(username);
                if(user != null) {
                    if(Objects.equals(user.getPassword(), password)) {
                        logedIn = user;

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("user", logedIn);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
                        finish();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Password wrong!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "There is no User like that!", Toast.LENGTH_SHORT).show();
            }
        });

        //Wants to Regiter:
        Button registerButton = findViewById(R.id.login_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                finish();
            }
        });
    }
    public User getLogedIn() {
        return logedIn;
    }
}