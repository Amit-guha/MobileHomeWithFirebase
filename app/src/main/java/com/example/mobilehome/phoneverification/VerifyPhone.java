package com.example.mobilehome.phoneverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.arch.core.executor.TaskExecutor;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.messaging.StudentLogIn;
import com.example.mobilehome.messaging.StudentRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    ProgressBar progressBar;
    TextInputEditText editText;
    AppCompatButton buttonSignIn;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);



        //Change the status bar background color
        Window w= VerifyPhone.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(VerifyPhone.this,R.color.white));

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        String phoneNumber = getIntent().getStringExtra("PhoneNumber");//further check
        Log.d("number : ", phoneNumber);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    editText.setText(code);
                    verifyCode(code);
                }
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(VerifyPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d("Verify", "onCodeSent:" + s);
                verificationId = s;//little
            }
        };

        try {
            sendVerificationCode(phoneNumber);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });


    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            try {
                                String name = getIntent().getStringExtra("name");
                                String Phoneid = getIntent().getStringExtra("PhoneNumber");
                                String emailid = getIntent().getStringExtra("email");
                                String passwordid = getIntent().getStringExtra("password");

                                StoreDatabase(Phoneid, name, emailid, passwordid);
                            }catch (Exception e){
                                Toast.makeText(VerifyPhone.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                          /*  Intent intent = new Intent(VerifyPhone.this, LoginPhone.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/

                        } else {

                            Toast.makeText(VerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerifyPhone.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Error", e.getMessage());

                    }
                });
    }


    //Verification code sent to the user
    private void sendVerificationCode(String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        );
        progressBar.setVisibility(View.GONE);

    }

    private void StoreDatabase(final String phoneNumber, final String user, String Semail, String SPassword) {
        mAuth.createUserWithEmailAndPassword(Semail, SPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = current_user.getUid();
                    // String token_id = FirebaseInstanceId.getInstance().getToken();
                    Map userMap = new HashMap();
                    //  userMap.put("device_token", uid);
                    userMap.put("name", user);
                    userMap.put("status", "Hello Kshitiz");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", uid);
                    userMap.put("online", "true");
                    userMap.put("Phone", phoneNumber);

                    mDatabase.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "New User is created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VerifyPhone.this, LoginPhone.class);
                                //intent.putExtra("user_id",Owner_id);

                                //----REMOVING THE LOGIN ACTIVITY FROM THE QUEUE----
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(VerifyPhone.this, "YOUR NAME IS NOT REGISTERED... MAKE NEW ACCOUNT-- ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR REGISTERING USER....", Toast.LENGTH_SHORT).show();
                }
            }
        });





         /* FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
          final String uid = current_user.getUid();
         String ShortPhone = getIntent().getStringExtra("ShortPhone");//-----Without+880
        // String token_id = FirebaseInstanceId.getInstance().getToken();
        final Map userMap = new HashMap();
        //  userMap.put("device_token", uid);
        userMap.put("name", user);
        userMap.put("status","Hi" );
        userMap.put("image", "default");
        userMap.put("thumb_image",phoneNumber);//uid-------------------------------------
        userMap.put("online", "true");*/

    /*    mDatabase.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("Successfully","Completed");
                }
                else{
                    Log.d("Failed", "error ");
                }
            }
        });*/
    }


}
