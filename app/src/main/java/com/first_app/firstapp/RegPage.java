package com.first_app.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegPage extends AppCompatActivity {

    EditText uName,uEmail,uPassword;
    Button regButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Userdata userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_page);
        uName=(EditText)findViewById(R.id.regUname);
        uEmail=(EditText)findViewById(R.id.regEmail);
        uPassword=(EditText)findViewById(R.id.regPass);
        regButton=(Button)findViewById(R.id.regB);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("firstapp-4007c-default-rtdb");
        userdata=new Userdata();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=uName.getText().toString();
                String email=uEmail.getText().toString();
                String password=uPassword.getText().toString();

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                    Toast.makeText(RegPage.this, "Add some data", Toast.LENGTH_SHORT).show();
                }
                else{
                   addDatatoFirebase(name,email,password);
                }
            }
        });
    }
    private void addDatatoFirebase(String name, String email, String password){
        userdata.setUsername(name);
        userdata.setPassword(password);
        userdata.setEmail(email);
        if(!validateUsername() | !validateEmail() | !validatePassword()){
            return;
        }
        else {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    databaseReference.setValue(userdata);
                    Toast.makeText(RegPage.this, "data added", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(RegPage.this, "Failed to add data " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


      private Boolean validateUsername(){
        String val = uName.getText().toString().trim();
        String noWhiteSpace = "\\A\\w{1,10}\\z";
        if (val.isEmpty()){
            uName.setError("Field cannot be empty");
            return false;
        }
        else if(val.length()>=10){
            uName.setError("Username too Long");
            return false;
        }
        else if(!val.matches(noWhiteSpace)){
            uName.setError("White spaces are not allowed");
            return false;
        }
        else {
            uName.setError(null);
            return true;
        }
    }

      private Boolean validateEmail(){
        String val = uEmail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            uEmail.setError("Field cannot be empty");
            return false;
        }
        else if (!val.matches(emailPattern)){
            uEmail.setError("Invalid email address");
            return false;
        }
        else {
            uEmail.setError(null);
            return true;
        }

    }

      private Boolean validatePassword(){
        String val = uPassword.getText().toString().trim();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white space
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()){
            uPassword.setError("Field cannot be empty");
            return false;
        }
        else if (!val.matches(passwordVal)){
            uPassword.setError("Password is too weak");
            return false;
        }
        else {
            uPassword.setError(null);
            return true;
        }

    }
}