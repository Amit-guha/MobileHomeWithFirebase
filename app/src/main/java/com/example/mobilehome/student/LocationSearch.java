package com.example.mobilehome.student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;

public class LocationSearch extends AppCompatActivity {

    private RadioGroup mGroup;
    private RadioButton mRadioButton;
    private Button mSubmitBtn;
    private TextView mANotherSearchTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        //Add custom toolbar
        Toolbar toolbar=findViewById(R.id.toolbarandroid);
        setSupportActionBar(toolbar);

        //Change the status bar background color
        Window w=LocationSearch.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(LocationSearch.this,R.color.white));

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

       // getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
       // getSupportActionBar().setCustomView(R.layout.custom_toolbar);

        //adding back button
      //  getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGroup=findViewById(R.id.SerachRadioGroup);
        mSubmitBtn=findViewById(R.id.btnSumitSErach);
        mANotherSearchTv=findViewById(R.id.tv_SearchAnotherWay);



       // mSubmitBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int radioId = mGroup.getCheckedRadioButtonId();


                if (radioId==-1) {
                    Toast.makeText(LocationSearch.this, "Please Select One Place", Toast.LENGTH_SHORT).show();

                } else {
                    mRadioButton = findViewById(radioId);
                    String Location = mRadioButton.getText().toString();
                    //Toast.makeText(LocationSearch.this, "" + Location, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LocationSearch.this, StudentUpdateCheck.class);
                    intent.putExtra("Location", Location);
                    startActivity(intent);
                }
            }
        });

        mANotherSearchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LocationSearch.this,SearchAnotherWay.class);
                startActivity(intent);
            }
        });


    }
}
