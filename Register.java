package com.example.vishnu.fitnessapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends AppCompatActivity {

    EditText fname,lname,mailid,pass,connfirmpass;
    Button registration;
    String Mail,password,firstname,lastname;
    ProgressBar progress;
    public int flag=0,flag1=0;
    private FirebaseAuth mAuth;

    private void registeruser()
    {
         if(!Patterns.EMAIL_ADDRESS.matcher(Mail).matches())
         {
            mailid.setError("Please enter a valid Email ID");
            mailid.requestFocus();
            return;
         }
         if(password.length()<6)
         {
             pass.setError("Minimum length of password should be 6");
             pass.requestFocus();
             return;
         }
         flag=1;
         progress.setVisibility(View.VISIBLE);

    }
    public void connection()
    {
        Connection conn;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://fitnessapp.chy3gfgjavzg.ap-south-1.rds.amazonaws.com:3306/userdetails", "fitnessapp", "password");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO profiledetails VALUES ('"+firstname+"','"+lastname+"','"+Mail+"')");
            ps.execute();
        } catch (ClassNotFoundException e) {
            Toast.makeText(Register.this,String.valueOf(e),Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e1)
        {
            Toast.makeText(Register.this,String.valueOf(e1),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registration = findViewById(R.id.RegisterButtton);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = findViewById(R.id.firstname);
                lname = findViewById(R.id.Lastname);
                mailid = findViewById(R.id.username);
                pass = findViewById(R.id.pass);
                progress = findViewById(R.id.progressBar);
                connfirmpass = findViewById(R.id.confirmpass);
                firstname = fname.getText().toString();
                lastname = lname.getText().toString();
                Mail = mailid.getText().toString().trim();
                password = pass.getText().toString().trim();
                String confirmpassword = connfirmpass.getText().toString().trim();
                if (firstname.equals("")||lastname.equals("")||Mail.equals("")||password.equals("")||confirmpassword.equals(""))
                {
                    Toast.makeText(Register.this,"Please fill all the fields",Toast.LENGTH_LONG).show();
                }
                else {
                    if(password.equals(confirmpassword)) {
                        registeruser();
                        if(flag==1) {
                            mAuth.createUserWithEmailAndPassword(Mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                  if(task.isSuccessful())
                                  {
                                      progress.setVisibility(View.GONE);
                                      final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                      firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                                  if(task.isSuccessful())
                                                  {
                                                    AlertDialog.Builder alertbox = new AlertDialog.Builder(Register.this);
                                                    alertbox.setCancelable(false).setTitle("Verification").setMessage("Verification mail sent and verify to login").setPositiveButton("Ok",
                                                            new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            connection();
                                                            Intent intent1 = new Intent(Register.this, Login.class);
                                                            startActivity(intent1);
                                                        }
                                                    });
                                                    AlertDialog alert = alertbox.create();
                                                    alert.show();
                                                  }
                                                  if(!task.isSuccessful())
                                                  {
                                                      mailid.setError("Enter a valid existing Email Id");
                                                      mailid.requestFocus();
                                                  }
                                          }
                                      });
                                  }
                                  else
                                  {
                                      if(task.getException() instanceof FirebaseAuthUserCollisionException )
                                      {
                                          progress.setVisibility(View.GONE);
                                          Toast.makeText(Register.this,"User already Registered",Toast.LENGTH_SHORT).show();
                                      }
                                      else
                                      {
                                          progress.setVisibility(View.GONE);
                                          Toast.makeText(Register.this,"Registration Failed",Toast.LENGTH_SHORT).show();

                                      }
                                  }
                                }
                            });
                        }
                    }
                    else
                    {
                        connfirmpass.setError("Password doesn't match");
                        connfirmpass.requestFocus();
                    }
                }
            }
        });
    }
}