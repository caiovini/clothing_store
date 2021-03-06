package com.example.caio.clothingstore.Main.Activities;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.caio.clothingstore.Main.Helper.Preferences;
import com.example.caio.clothingstore.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveDataButton;
    private Button goBackButton;
    private Preferences preferences;
    private RadioButton radioButtonKeepLogged;
    private RadioButton radioButtonNotKeepLogged;

    private RadioGroup radioGroup;
    private final String KEEP_USER_LOGGED = "LOGGED";
    private final String IS_LOGGED = "IS_LOGGED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        preferences = new Preferences(this);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroupLogged);
        radioButtonKeepLogged = (RadioButton) findViewById(R.id.radioButtonKeepMeLogged);
        radioButtonNotKeepLogged = (RadioButton) findViewById(R.id.radioButtonNotKeepMeLogged);

        String keepLogged =  preferences.getIdentifier(KEEP_USER_LOGGED);

        if(keepLogged.equals("YES")) {

            radioButtonKeepLogged.setChecked(true);
        }else{

            radioButtonNotKeepLogged.setChecked(true);
        }

        saveDataButton = (Button) findViewById(R.id.buttonLogout);
        saveDataButton.setOnClickListener(this);

        goBackButton = (Button) findViewById(R.id.buttonGoBackSettings);
        goBackButton.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                switch (id){

                    case R.id.radioButtonKeepMeLogged:

                        preferences.saveData(KEEP_USER_LOGGED  , "YES");
                        break;
                    case R.id.radioButtonNotKeepMeLogged:

                        preferences.saveData(KEEP_USER_LOGGED  , "NO");
                        break;
                }

            }
        });


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.buttonLogout:

                preferences.saveData(KEEP_USER_LOGGED  , "NO");
                preferences.saveData(IS_LOGGED , "NO");
                Intent intent = new Intent();
                intent.putExtra("result" , "LOG_OUT");
                setResult(Activity.RESULT_OK , intent);
                onBackPressed();
                break;

            case R.id.buttonGoBackSettings:

                onBackPressed();
                break;
        }

    }
}
