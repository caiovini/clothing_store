package com.example.caio.clothingstore.Main.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.caio.clothingstore.R;


public class RegisterActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText secondName;
    private EditText userName;
    private EditText password;
    private EditText address;
    private EditText postalCode;
    private EditText creditCardNumber;
    private EditText securityCreditCardNumber;
    private Button buttonSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firstName = (EditText) findViewById(R.id.registerFirstName);
        secondName = (EditText) findViewById(R.id.registerSecondName);
        userName = (EditText) findViewById(R.id.registerUserName);
        password = (EditText) findViewById(R.id.registerPassword);
        address = (EditText) findViewById(R.id.registerAdress);
        postalCode = (EditText) findViewById(R.id.registerPostalCode);
        creditCardNumber = (EditText) findViewById(R.id.registerCreditCard);
        securityCreditCardNumber = (EditText) findViewById(R.id.registerSecurityNumber);
        buttonSubmit = (Button) findViewById(R.id.buttonRegister);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fieldsAreValid()){

                    Toast.makeText(getApplicationContext() , "Submission complete" , Toast.LENGTH_LONG).show();
                    onBackPressed();
                }else{

                    Toast.makeText(getApplicationContext() , "Error during submission" , Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private boolean fieldsAreValid(){

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

        }else if(password.getText().toString().isEmpty()){

            Toast.makeText(this , "Password is empty" , Toast.LENGTH_LONG).show();
            password.requestFocus();
            return false;

        }else if(address.getText().toString().isEmpty()){

            Toast.makeText(this , "Address is empty" , Toast.LENGTH_LONG).show();
            address.requestFocus();
            return false;

        }else if(postalCode.getText().toString().isEmpty()){

            Toast.makeText(this , "Postal code is empty" , Toast.LENGTH_LONG).show();
            postalCode.requestFocus();
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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
}
