package com.example.caio.clothingstore.Main.Activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.caio.clothingstore.Main.Database.Database;
import com.example.caio.clothingstore.Main.Models.CreditCard;
import com.example.caio.clothingstore.Main.Models.Customer;
import com.example.caio.clothingstore.R;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class RegisterActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText secondName;
    private EditText userName;
    private EditText password;
    private EditText address;
    private EditText creditCardNumber;
    private EditText securityCreditCardNumber;
    private Button buttonSubmit;

    private final Database database = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database.getWritableDatabase();


        firstName = (EditText) findViewById(R.id.registerFirstName);
        secondName = (EditText) findViewById(R.id.registerSecondName);
        userName = (EditText) findViewById(R.id.registerUserName);
        password = (EditText) findViewById(R.id.registerPassword);
        address = (EditText) findViewById(R.id.registerAdress);
        creditCardNumber = (EditText) findViewById(R.id.registerCreditCard);
        securityCreditCardNumber = (EditText) findViewById(R.id.registerSecurityNumber);
        buttonSubmit = (Button) findViewById(R.id.buttonRegister);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fieldsAreValid()){

                    //Encryption
                    String passwordEncrypted = hashPassword(password.getText().toString());


                    Customer customer = new Customer(0 , userName.getText().toString() ,
                                                                passwordEncrypted ,
                                                                address.getText().toString() , false);

                    CreditCard creditCard = new CreditCard(0 , Integer.parseInt(creditCardNumber.getText().toString()) ,
                                                                           Integer.parseInt(securityCreditCardNumber.getText().toString()) ,
                                                                           "Visa" );

                    database.open();

                    if(database.insertCustomer(customer)) {

                        int idInserted = database.getLastCustomerId();

                        if (database.insertCreditCard(creditCard , idInserted)) {

                            Toast.makeText(getApplicationContext(), "Submission complete", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else {

                            Toast.makeText(getApplicationContext(), "Error inserting in database", Toast.LENGTH_LONG).show();
                        }
                    }
                }else{

                    Toast.makeText(getApplicationContext() , "Error during submission" , Toast.LENGTH_LONG).show();
                }

                database.close();

            }
        });

    }

    private boolean fieldsAreValid(){

        database.open();
        Cursor cursor = database.getUserByUsername(userName.getText().toString());

        if(cursor.getCount() > 0){

            Toast.makeText(this , "User already exists" , Toast.LENGTH_LONG).show();
            userName.requestFocus();
            return false;
        }
        database.close();

        if(firstName.getText().toString().isEmpty()){

            Toast.makeText(this , "First name is empty" , Toast.LENGTH_LONG).show();
            firstName.requestFocus();
            return false;

        }else if(secondName.getText().toString().isEmpty()){

            Toast.makeText(this , "Second name is empty" , Toast.LENGTH_LONG).show();
            secondName.requestFocus();
            return false;

        }else if(userName.getText().toString().isEmpty()){

            Toast.makeText(this , "User name is empty" , Toast.LENGTH_LONG).show();
            userName.requestFocus();
            return false;

        }else if(address.getText().toString().isEmpty()){

            Toast.makeText(this , "Address is empty" , Toast.LENGTH_LONG).show();
            address.requestFocus();
            return false;

        }else if(creditCardNumber.getText().toString().isEmpty()){

            Toast.makeText(this , "Credit card number is empty" , Toast.LENGTH_LONG).show();
            creditCardNumber.requestFocus();
            return false;

        }else if(securityCreditCardNumber.getText().toString().isEmpty()){

            Toast.makeText(this , "Security number is empty" , Toast.LENGTH_LONG).show();
            securityCreditCardNumber.requestFocus();
            return false;

        }else{

            return true;

        }
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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
}
