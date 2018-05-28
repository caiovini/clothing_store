package com.example.caio.clothingstore.Main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caio.clothingstore.Main.Activities.RegisterActivity;
import com.example.caio.clothingstore.Main.Helper.Preferences;
import com.example.caio.clothingstore.R;


public class LoginApp extends Fragment {

    private final String IS_LOGGED = "IS_LOGGED";
    private final String KEEP_USER_LOGGED = "LOGGED";

    private Button buttonLogin;
    private EditText userName;
    private EditText password;
    private TextView register;
    private onCommitTransaction mCallback;
    private Preferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_app, container, false);


        buttonLogin = (Button) view.findViewById(R.id.buttonLogin);
        userName = (EditText) view.findViewById(R.id.loginUserName);
        password = (EditText) view.findViewById(R.id.loginPassword);
        register = (TextView) view.findViewById(R.id.textViewRegister);

        /*
           Check if user is logged,
           if true call fragment details
        */


        preferences = new Preferences(getContext());
        String isLogged =  preferences.getIdentifier(IS_LOGGED);

        if(isLogged.equals("YES")) {

            mCallback.onTransaction();
            buttonLogin.setVisibility(Button.INVISIBLE);
        }


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext() , RegisterActivity.class);
                startActivity(intent);

            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userName.getText().toString().isEmpty()){

                    Toast.makeText(getContext() , "User name is empty" , Toast.LENGTH_LONG).show();
                    userName.requestFocus();

                }else if(password.getText().toString().isEmpty()){

                    Toast.makeText(getContext() , "Password is empty" , Toast.LENGTH_LONG).show();
                    password.requestFocus();

                }else{

                    preferences.saveData(IS_LOGGED, "YES");
                    mCallback.onTransaction();
                    buttonLogin.setVisibility(Button.INVISIBLE);

                }
            }
        });

        return view;
    }



    public interface onCommitTransaction {
           void onTransaction();
    }

    @Override
    public void onAttach(Context context) {

        try{

            mCallback = (onCommitTransaction) context;
        }catch (ClassCastException ex){

            throw new ClassCastException(context.toString()
                    + " must implement onCommitTransaction");
        }

        super.onAttach(context);
    }
}
