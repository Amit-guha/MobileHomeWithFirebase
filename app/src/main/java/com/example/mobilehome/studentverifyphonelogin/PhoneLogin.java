package com.example.mobilehome.studentverifyphonelogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.messaging.SendRequestProfile;
import com.example.mobilehome.messaging.StudentLogIn;
import com.example.mobilehome.owner.UpDelCheck;
import com.example.mobilehome.phoneverification.LoginPhone;
import com.example.mobilehome.phoneverification.RegestrationPhone;
import com.example.mobilehome.student.ContactDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PhoneLogin extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private TextInputEditText mEmail, mPassword;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mauth;

    private TextView mGoneView;

    //For only customer------------>>>>>>>>>>>>Student

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);


        //Change the status bar background color
        Window w= PhoneLogin.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(PhoneLogin.this,R.color.white));

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        //Gone view-----------------------Owner id save purpose
        mGoneView=findViewById(R.id.tvGonetextview);
        String Owner_id=getIntent().getStringExtra("user_id");
        mGoneView.setText(Owner_id);

        progressDialog = new ProgressDialog(PhoneLogin.this);
        mEmail = findViewById(R.id.EmialField);
        mPassword = findViewById(R.id.passwordField);

        //DatabaseReference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        // mDatabaseReference= FirebaseDatabase.getInstance().getReference("users");
        mauth = FirebaseAuth.getInstance();

    }


    //----SHOWING ALERT DIALOG FOR EXITING THE APP----
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PhoneLogin.this);
        builder.setMessage("Do u Want to Exit?");
        builder.setTitle("Exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new PhoneLogin.MyLisitiner());
        builder.setNegativeButton("Cancel", null);
       // mauth.signOut();
        builder.show();

    }

    public class MyLisitiner implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }


    public void buttonIsClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonSign:

                String email = mEmail.getEditableText().toString().trim();
                String password = mPassword.getEditableText().toString().trim();

               // Toast.makeText(this, "" + email, Toast.LENGTH_SHORT).show();


                //---CHECKING IF EMAIL AND PASSWORD IS NOT EMPTY----
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(PhoneLogin.this, "Please Fill all blocks", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setTitle("Logging in");
                progressDialog.setMessage("Please wait while we are checking the credentials..");
                progressDialog.setCancelable(false);
                progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                login_user(email, password);
                break;

            case R.id.buttonRegister:

                Intent intent = new Intent(PhoneLogin.this, PhoneRegister.class);
                 intent.putExtra("userid",mGoneView.getText().toString());//
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void login_user(String email, String password) {

        //---SIGN IN FOR THE AUTHENTICATE EMAIL-----
        mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {

                            //---ADDING DEVICE TOKEN ID AND SET ONLINE TO BE TRUE---
                            //---DEVICE TOKEN IS USED FOR SENDING NOTIFICATION----
                            if (mauth.getCurrentUser() != null) {
                                final String user_id = mauth.getCurrentUser().getUid();
                                //  String token_id= FirebaseInstanceId.getInstance().getToken();
                                Map addValue = new HashMap();
                                //  addValue.put("device_token",token_id);
                                addValue.put("online", "true");

                                //---IF UPDATE IS SUCCESSFULL , THEN OPEN MAIN ACTIVITY---
                                mDatabaseReference.child(user_id).updateChildren(addValue, new DatabaseReference.CompletionListener() {

                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        // try {
                                        if (databaseError == null) {
                                            String Owner_Id = getIntent().getStringExtra("user_id");
                                            //---OPENING MAIN ACTIVITY---
                                            Log.e("Login : ", "Logged in Successfully");
                                            Toast.makeText(getApplicationContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(PhoneLogin.this, SendRequestProfile.class);
                                             intent.putExtra("user_id", mGoneView.getText().toString());
                                            // intent.putExtra("userLogin", user_id);
                                            //Toast.makeText(StudentLogIn.this, "" + mGoneView.getText(), Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            //finish();


                                        } else {
                                            Toast.makeText(PhoneLogin.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                                            Log.e("Error is : ", databaseError.toString());

                                        }
                                       /* }catch (Exception e){
                                            Toast.makeText(StudentLogIn.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


                                        }
*/


                                    }
                                });


                            }
                        } else {
                            //---IF AUTHENTICATION IS WRONG----
                            Toast.makeText(PhoneLogin.this, "Wrong Credentials" +
                                    "", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
