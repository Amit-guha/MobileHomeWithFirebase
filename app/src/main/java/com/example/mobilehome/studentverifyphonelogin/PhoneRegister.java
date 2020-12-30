package com.example.mobilehome.studentverifyphonelogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.phoneverification.RegestrationPhone;
import com.example.mobilehome.phoneverification.VerifyPhone;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PhoneRegister extends AppCompatActivity {

    private TextInputEditText CountryCode, UserName, Phone,etemail, etpassword;
    AppCompatButton btnContinue;

    private DatabaseReference mDatabase;

    //Owner Regestration  && user Regestratin
    //Same UI


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);


        //Change the status bar background color
        Window w= PhoneRegister.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(PhoneRegister.this,R.color.white));

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        CountryCode=findViewById(R.id.editTextCountryCode);
        Phone=findViewById(R.id.editTextPhone);
        UserName=findViewById(R.id.NameField);
        etemail = findViewById(R.id.EmialFieldRegister);
        etpassword = findViewById(R.id.passwordFieldRegister);
        btnContinue=findViewById(R.id.buttonContinue);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=CountryCode.getEditableText().toString().trim();
                String phn=Phone.getEditableText().toString().trim();
                String user=UserName.getEditableText().toString().trim();
                String email = etemail.getEditableText().toString().trim();
                String password = etpassword.getEditableText().toString().trim();

                if (phn.isEmpty() || phn.length() < 10) {
                    Phone.setError("Valid number is required");
                    Phone.requestFocus();
                    return;
                }

                if(user.isEmpty()){
                    UserName.setError("UserName is required");
                    // UserName.requestFocus();
                    return;
                }

                if (email.equals("")) {
                    Toast.makeText(PhoneRegister.this, "Please Fill the email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(PhoneRegister.this, "Password is too short", Toast.LENGTH_SHORT).show();
                    return;
                }

                String phoneNumber = code + phn;
                Toast.makeText(PhoneRegister.this, ""+user, Toast.LENGTH_SHORT).show();


                if(!phoneNumber.isEmpty() && !user.isEmpty() ){
                    // StoreDatabase(phoneNumber,user);
                    String Owner_id=getIntent().getStringExtra("userid");

                    Intent intent=new Intent(PhoneRegister.this,PhoneVerify.class);
                    intent.putExtra("ShortPhone",phn);//Further check +880
                    intent.putExtra("PhoneNumber",phoneNumber);//Further check +880
                    intent.putExtra("name",user);
                    intent.putExtra("password",password);
                    intent.putExtra("email",email);
                    intent.putExtra("userid",Owner_id);
                    Toast.makeText(PhoneRegister.this, ""+Owner_id, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else {
                    Toast.makeText(PhoneRegister.this, "Failed to Verify", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
