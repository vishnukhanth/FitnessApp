package com.example.vishnu.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.SignInMethodQueryResult;

public class Login extends AppCompatActivity {

    public static int flag;
    TextView registration,forgot;
    Button lgnbutton;
    EditText user_name,passwrd;
    String res,userr,passs;
    private FirebaseAuth mAuth;
    ProgressBar progress;

    public void userlogin()
    {
        if(userr.equals("")&&passs.equals(""))
        {
            user_name.setError("Username is required");
            user_name.requestFocus();
            passwrd.setError("Password is required");
            passwrd.requestFocus();
            return;
        }
        if(userr.equals(""))
        {
            user_name.setError("Username is required");
            user_name.requestFocus();
            return;
        }
        else if(passs.equals(""))
        {
            passwrd.setError("Password is required");
            passwrd.requestFocus();
            return;
        }
        progress.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(userr,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user.isEmailVerified()) {
                        progress.setVisibility(View.GONE);
                        flag=1;
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edt = pref.edit();
                        edt.putBoolean("activity_executed", true);
                        edt.commit();

                        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                        prefs.edit().putString("username", userr).commit();

                        Intent intent2 = new Intent(Login.this,profile.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                    }
                    else
                    {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Email not verified", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    progress.setVisibility(View.GONE);
                    Toast.makeText(Login.this,"Invalid username or password",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(flag==1)
        {
            Intent intent2 = new Intent(Login.this,profile.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
        }
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        registration = findViewById(R.id.Reg);
        user_name = findViewById(R.id.editText);
        passwrd = findViewById(R.id.editText2);
        lgnbutton = findViewById(R.id.button);
        forgot = findViewById(R.id.forgotpass);
        progress=findViewById(R.id.progressBar);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.vishnu.fitnessapp.Register");
                startActivity(intent);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword();
            }
        });
    }

    public void onbuttonclick(View view)
    {
         userr = user_name.getText().toString();
         passs = passwrd.getText().toString();
         userlogin();

    }
    public void forgotpassword()
    {
        userr = user_name.getText().toString();
        if(userr.equals(""))
        {
            user_name.setError("Username is required");
            user_name.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(userr).matches())
        {
            user_name.setError("Please enter a valid Email ID");
            user_name.requestFocus();
            return;
        }
        else
        {

            progress.setVisibility(View.VISIBLE);
            mAuth.fetchProvidersForEmail(userr).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                  boolean check = !task.getResult().getProviders().isEmpty();
                  if(!check)
                  {
                      progress.setVisibility(View.GONE);
                      Toast.makeText(Login.this,"User not Registered",Toast.LENGTH_SHORT).show();
                      return;
                  }
                  else
                  {
                      progress.setVisibility(View.GONE);
                  }
                }
            });
        }
        mAuth.sendPasswordResetEmail(userr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Login.this,"Password reset mail sent",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(Login.this,"Password reset failed",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}