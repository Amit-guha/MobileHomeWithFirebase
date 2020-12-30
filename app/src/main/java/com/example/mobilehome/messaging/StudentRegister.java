package com.example.mobilehome.messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.actualstudent.ChatLogin;
import com.example.mobilehome.student.ContactDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class StudentRegister extends AppCompatActivity {

    private FirebaseAuth mauth;
    private TextInputEditText etdisplayname, etemail, etpassword;
    private Button buttonsubmit;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        //Change the status bar background color
        Window w= StudentRegister.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(StudentRegister.this,R.color.white));

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        etdisplayname = findViewById(R.id.NameField);
        etemail = findViewById(R.id.EmialFieldRegister);
        etpassword = findViewById(R.id.passwordFieldRegister);
        buttonsubmit = (Button) findViewById(R.id.button3);
        progressDialog = new ProgressDialog(StudentRegister.this);

        mauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
       // mDatabase = FirebaseDatabase.getInstance().getReference("users");
    }

    //-----REGISTER BUTTON IS PRESSED---
    public void buttonIsClicked(View view) {

        if (view.getId() == R.id.button3) {

            String displayname = etdisplayname.getEditableText().toString().trim();
            String email = etemail.getEditableText().toString().trim();
            String password = etpassword.getEditableText().toString().trim();

            Toast.makeText(this, ""+displayname, Toast.LENGTH_SHORT).show();

            //----CHECKING THE EMPTINESS OF THE EDITTEXT-----
            if (displayname.equals("")) {
                Toast.makeText(StudentRegister.this, "Please Fill the name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.equals("")) {
                Toast.makeText(StudentRegister.this, "Please Fill the email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(StudentRegister.this, "Password is too short", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setTitle("Registering User");
            progressDialog.setMessage("Please wait while we are creating your account... ");
            progressDialog.setCancelable(false);
            progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            register_user(displayname, email, password);
        }
    }


    //-----REGISTERING THE NEW USER------
    private void register_user(final String displayname, String email, String password) {

        mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //------IF USER IS SUCCESSFULLY REGISTERED-----
                if (task.isSuccessful()) {

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = current_user.getUid();
                   // String token_id = FirebaseInstanceId.getInstance().getToken();
                    Map userMap = new HashMap();
                  //  userMap.put("device_token", uid);
                    userMap.put("name", displayname);
                    userMap.put("status", "Hello Kshitiz");
                    userMap.put("image", "default");
                    userMap.put("thumb_image",uid);
                    userMap.put("online", "true");
                    //userMap.put("id",mauth.getCurrentUser().getUid());

                    mDatabase.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if (task1.isSuccessful()) {

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "New User is created", Toast.LENGTH_SHORT).show();
                                //Owner id----------->>>From StudentLogin
                                String Owner_id=getIntent().getStringExtra("userid");
                                Intent intent = new Intent(StudentRegister.this, StudentLogIn.class);
                                intent.putExtra("user_id",Owner_id);

                                //----REMOVING THE LOGIN ACTIVITY FROM THE QUEUE----
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();


                            } else {

                                Toast.makeText(StudentRegister.this, "YOUR NAME IS NOT REGISTERED... MAKE NEW ACCOUNT-- ", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                }
                //---ERROR IN ACCOUNT CREATING OF NEW USER---
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "ERROR REGISTERING USER....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}