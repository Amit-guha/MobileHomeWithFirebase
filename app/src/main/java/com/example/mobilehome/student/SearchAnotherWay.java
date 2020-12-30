package com.example.mobilehome.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;

public class SearchAnotherWay extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Spinner mPlace, mAmount, mAmount2;

    // private TextView tvfirst, tvSecnd,tv_placeSetUp;

    private LinearLayout mLayout;

    private String[] ALLPLACE;
    private String[] ALLAMOUNT;
    private String[] ALLAMOUNTMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_another_way);

        //Add custom toolbar
        Toolbar toolbar = findViewById(R.id.tvsearch);
        setSupportActionBar(toolbar);


        //adding back button
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //Change the status bar background color
        Window w = SearchAnotherWay.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(SearchAnotherWay.this, R.color.white));

       /* TextView tooltv=findViewById(R.id.toolbarTextview);
        ImageView toolimg=findViewById(R.id.toolbarimageview);

        tooltv.setText("Search Another Way");*/

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        //  tvfirst = findViewById(R.id.tvfirst);
        //  tvSecnd = findViewById(R.id.tvsecond);
        // tv_placeSetUp=findViewById(R.id.tv_PlaceSetUp);


        radioGroup = findViewById(R.id.R_group);

        mPlace = findViewById(R.id.SpinnerPlace);
        mAmount = findViewById(R.id.SpinnerAmout);
        mAmount2 = findViewById(R.id.SpinnnerAmount2);
        mLayout = findViewById(R.id.LIneralayout);


        ALLPLACE = getResources().getStringArray(R.array.List_Of_Place);
        ALLAMOUNT = getResources().getStringArray(R.array.TkRange);
        ALLAMOUNTMax = getResources().getStringArray(R.array.TkRange2);

        ArrayAdapter<String> SearchingPlace = new ArrayAdapter<>(this, R.layout.smaple_spinner, R.id.tv_Spinner, ALLPLACE);
        mPlace.setAdapter(SearchingPlace);

        ArrayAdapter<String> SearchingAmout = new ArrayAdapter<>(this, R.layout.smaple_spinner, R.id.tv_Spinner, ALLAMOUNT);
        mAmount.setAdapter(SearchingAmout);

        ArrayAdapter<String> SearchingAmout2 = new ArrayAdapter<>(this, R.layout.smaple_spinner, R.id.tv_Spinner, ALLAMOUNTMax);
        mAmount2.setAdapter(SearchingAmout2);

/*
        //setup First Amout---------------------------------------------
        mAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvfirst.setText(ALLAMOUNT[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvfirst.setText(" ");
            }
        });

        //setUp Second Amount---------------------------------
        mAmount2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvSecnd.setText(ALLAMOUNT[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvSecnd.setText(" ");
            }
        });

        //PlaceSetUp--------------------------------------------------
        mPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_placeSetUp.setText(ALLPLACE[position]);
               // Toast.makeText(SearchAnotherWay.this, ""+id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tv_placeSetUp.setText(" ");
            }
        });*/


        //Check the validity of Data
        findViewById(R.id.btn_Submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int id = radioGroup.getCheckedRadioButtonId();
                    //Toast.makeText(SearchAnotherWay.this, ""+id, Toast.LENGTH_SHORT).show();

                    if (id == -1) {
                        //Log.d("Check", mPlace.getSelectedItem().toString());
                        Toast.makeText(SearchAnotherWay.this, "Please fill up all the options", Toast.LENGTH_SHORT).show();
                    } else {
                        radioButton = findViewById(id);
                        final String Gender = radioButton.getText().toString().trim();

                        if ((mPlace.getSelectedItem().toString().trim().equals("Select any area"))) {
                            Toast.makeText(SearchAnotherWay.this, "Please Select Any Valid Place", Toast.LENGTH_SHORT).show();
                        } else {
                            String Tk = mAmount.getSelectedItem().toString();
                            String Tk2 = mAmount2.getSelectedItem().toString();
                            //String Place = mPlace.getSelectedItem().toString();
                            String place = mPlace.getSelectedItem().toString();
                            //Log.d("Place check", place);

                            long l1 = Long.parseLong(Tk);
                            long l2 = Long.parseLong(Tk2);

                            if (l1 > l2) {
                                Toast.makeText(SearchAnotherWay.this, "Wrong Amout Range! Your Right Amount is " +
                                        "always greater than your left Amout", Toast.LENGTH_LONG).show();
                            } else if (l1 == l2) {
                                Toast.makeText(SearchAnotherWay.this, "Wrong Amout Range! Your Right Amount is " +
                                        "always greater than your left Amout", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(SearchAnotherWay.this, ItemSerach.class);
                                intent.putExtra("g", Gender);
                                intent.putExtra("t", Tk);
                                intent.putExtra("t2", Tk2);
                                intent.putExtra("p", place);
                                startActivity(intent);

                                //Toast.makeText(SearchAnotherWay.this, "Gender : " + Gender + " Tk" + Tk + " place : " + place, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(SearchAnotherWay.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
