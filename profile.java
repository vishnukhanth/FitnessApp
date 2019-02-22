package com.example.vishnu.fitnessapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    int flag=0;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth mAuth;
    EditText fname,lname,mail;
    String firstname,lastname,username,name;
    NavigationView nav;
    View headerview;
    TextView drawertext;

    public void logout()
    {
        Intent intent = new Intent(profile.this,Login.class);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_executed", false);
        edt.commit();
        startActivity(intent);
    }

    public boolean drawerfun()
    {
        DrawerLayout drawer1 = findViewById(R.id.drawerlayout);
        drawer1.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigator);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void connection()
    {
        Connection conn;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://fitnessapp.chy3gfgjavzg.ap-south-1.rds.amazonaws.com:3306/userdetails", "fitnessapp", "password");
            String query ="SELECT * FROM profiledetails WHERE emailid = '"+username+"'";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery(query);
            while(rs.next())
            {
                firstname = rs.getString("fname");
                lastname = rs.getString("lname");
            }
        } catch (ClassNotFoundException e) {
            Toast.makeText(profile.this,String.valueOf(e),Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e1)
        {
            Toast.makeText(profile.this,String.valueOf(e1),Toast.LENGTH_SHORT).show();
            flag=1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_profile);

        drawer = findViewById(R.id.drawerlayout);
        toggle = new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        username = prefs.getString("username", "UNKNOWN");
        connection();
        name = firstname + " " + lastname;
        nav = findViewById(R.id.navigator);
        headerview = nav.getHeaderView(0);
        drawertext = headerview.findViewById(R.id.draweruser);
        if(flag==0) {
            drawertext.setText(name);
        }
        fname = findViewById(R.id.profilefirstname);
        lname = findViewById(R.id.profilelastname);
        mail=findViewById(R.id.profilemail);
        fname.setText(firstname);
        fname.setEnabled(false);
        lname.setText(lastname);
        lname.setEnabled(false);
        mail.setText(username);
        mail.setEnabled(false);
        setNavigationViewListner();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pf:

                return true;
            case R.id.wt:

                return true;
            case R.id.dt:

                return true;
            case R.id.fs:

                return true;
            case R.id.bmi:

                return true;
            case R.id.fd:

                return true;
            case R.id.sh:

                return true;
            case R.id.lg:
            {
                logout();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        //boolean x = drawerfun();
        //return x;

    }

}
