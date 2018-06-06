package com.example.caio.clothingstore.Main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.caio.clothingstore.Main.Activities.RegisterActivity;
import com.example.caio.clothingstore.Main.Database.Database;
import com.example.caio.clothingstore.Main.Helper.Preferences;
import com.example.caio.clothingstore.Main.Models.CreditCard;
import com.example.caio.clothingstore.Main.Models.Customer;
import com.example.caio.clothingstore.R;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginApp extends Fragment {

    private final String IS_LOGGED = "IS_LOGGED";
    private final String USER_NAME = "USER_NAME";
    private final String KEEP_USER_LOGGED = "LOGGED";

    private Button buttonLogin;
    private EditText userName;
    private EditText password;
    private TextView register;
    private onCommitTransaction mCallback;
    private Preferences preferences;
    private Database database;

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

        database = new Database(getContext());
        database.getWritableDatabase();

        preferences = new Preferences(getContext());
        String isLogged =  preferences.getIdentifier(IS_LOGGED);

        if(isLogged.equals("YES")) {

            String userName =  preferences.getIdentifier(USER_NAME);

            Customer customer = getCustomerInformation(userName , "" , true);


            if(customer != null) {

                mCallback.onTransaction(customer);
                buttonLogin.setVisibility(Button.INVISIBLE);
            }
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



                    Customer customer = getCustomerInformation(userName.getText().toString() , hashPassword(password.getText().toString()) , false);

                    if (customer != null) {

                        preferences.saveData(IS_LOGGED, "YES");
                        preferences.saveData(USER_NAME, userName.getText().toString());
                        mCallback.onTransaction(customer);
                        buttonLogin.setVisibility(Button.INVISIBLE);
                    }

                }
            }
        });

        return view;
    }

    private Customer getCustomerInformation(String userName , String password ,  boolean isLogged ){

        Customer customer = null;

        database.open();
        Cursor cursor = database.getUserByUsername(userName);

        if (cursor.getCount() > 0){

            cursor.moveToFirst();
            String decodePassword = cursor.getString(2);

            if(!isLogged){

                if(!password.equals(decodePassword)){

                    Toast.makeText(getContext() , "Wrong password" , Toast.LENGTH_LONG).show();
                    return null;
                }
            }


            CreditCard creditCard = new CreditCard(cursor.getInt(4) , cursor.getInt(6) , cursor.getInt(7) , cursor.getString(5));
            customer = new Customer(cursor.getInt(0) , cursor.getString(1) , decodePassword , cursor.getString(3) , false , creditCard);

        }else{

            Toast.makeText(getContext() , "User not found" , Toast.LENGTH_LONG).show();
        }

        database.close();
        return customer;
    }

    private String hashPassword(String password){

        MessageDigest messageDigest = null;

        try {

            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes(),0,password.length());

        }catch (NoSuchAlgorithmException ex){

            Log.i("Error hashing password" , ex.toString());
        }

        return new BigInteger(1 , messageDigest.digest()).toString(16);
    }


    public interface onCommitTransaction {
           void onTransaction(Customer customer);
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
