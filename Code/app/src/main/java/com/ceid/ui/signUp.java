package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signUp extends AppCompatActivity {
    private EditText username,password,name,surname,phoneNumber;
    private CheckBox check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        check = findViewById(R.id.checkBox);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        phoneNumber = findViewById(R.id.phoneNumber);

    }
    public void signUpButton(View view)
    {
        if(check.isChecked())
        {
            if (username.getText().toString().isEmpty())
            {
                    Toast.makeText(getApplicationContext(), "Username cannot be empty!",
                            Toast.LENGTH_LONG).show();
                    return;
            }
            if (password.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Password cannot be empty!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (name.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Name cannot be empty!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (surname.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Surname cannot be empty!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (phoneNumber.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Phone number cannot be empty!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (phoneNumber.getText().toString().length()!=10)
            {
                Toast.makeText(getApplicationContext(), "Not a valid phone number!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(getApplicationContext(),MainScreen.class);
            startActivity(intent);

        }
        else
        {
            Toast.makeText(getApplicationContext(), "You must agree to the Terms of Service and Privacy Policy!",
                    Toast.LENGTH_LONG).show();
        }
    }
    public void signIn(View view)
    {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
