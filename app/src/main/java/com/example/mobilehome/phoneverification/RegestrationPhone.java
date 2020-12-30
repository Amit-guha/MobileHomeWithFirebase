package com.example.mobilehome.phoneverification;

import androidx.annotation.NonNull;
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
import com.example.mobilehome.actualstudent.ChatLogin;
import com.example.mobilehome.messaging.StudentRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegestrationPhone extends AppCompatActivity {
    private TextInputEditText CountryCode, UserName, Phone,etemail, etpassword;
    AppCompatButton btnContinue;

    private DatabaseReference mDatabase;

    //actual--------------------Owner login
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regestration_phone);


        //Change the status bar background color
        Window w= RegestrationPhone.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(RegestrationPhone.this,R.color.white));

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
                    Toast.makeText(RegestrationPhone.this, "Please Fill the email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(RegestrationPhone.this, "Password is too short", Toast.LENGTH_SHORT).show();
                    return;
                }

                String phoneNumber = code + phn;
                Toast.makeText(RegestrationPhone.this, ""+user, Toast.LENGTH_SHORT).show();


                if(!phoneNumber.isEmpty() && !user.isEmpty() ){
                   // StoreDatabase(phoneNumber,user);
                    Intent intent=new Intent(RegestrationPhone.this,VerifyPhone.class);
                    intent.putExtra("ShortPhone",phn);//Further check +880
                    intent.putExtra("PhoneNumber",phoneNumber);//Further check +880
                    intent.putExtra("name",user);
                    intent.putExtra("password",password);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegestrationPhone.this, "Failed to Verify", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void StoreDatabase(String phoneNumber, String user) {
      //  FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
      //  final String uid = current_user.getUid();
        // String token_id = FirebaseInstanceId.getInstance().getToken();
        final Map userMap = new HashMap();
        //  userMap.put("device_token", uid);
        userMap.put("name", user);
        userMap.put("status", phoneNumber);
        userMap.put("image", "default");
        userMap.put("thumb_image","uid");
        userMap.put("online", "true");

        mDatabase.child(phoneNumber).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("Successfully","Completed");
                }
                else{
                    Log.d("Failed", "error ");
                }
            }
        });
    }

}
