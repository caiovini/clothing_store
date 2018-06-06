package com.example.caio.clothingstore.Main.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.caio.clothingstore.Main.Activities.BuyActivity;
import com.example.caio.clothingstore.Main.Database.Database;
import com.example.caio.clothingstore.Main.Helper.AndroidLoginController;
import com.example.caio.clothingstore.Main.Helper.Configure;
import com.example.caio.clothingstore.Main.Helper.Preferences;
import com.example.caio.clothingstore.Main.Interface.Payment;
import com.example.caio.clothingstore.Main.Models.CreditCard;
import com.example.caio.clothingstore.Main.Models.Customer;
import com.example.caio.clothingstore.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsFragment extends Fragment {

    private EditText address;
    private EditText cardNumber;
    private EditText securityNumber;
    private EditText voucherNumber;
    private Spinner spinner;
    private Payment paymentCallBack;
    private Database database;
    private Customer customer;

    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> spinnerList;

    private final String USER_NAME = "USER_NAME";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_details, container, false);


        address = (EditText) view.findViewById(R.id.editTextAdress);
        cardNumber = (EditText) view.findViewById(R.id.editTextCardNumber);
        securityNumber = (EditText) view.findViewById(R.id.editTextSecurityNumber);
        voucherNumber = (EditText) view.findViewById(R.id.editTextVoucher);
        spinner = (Spinner) view.findViewById(R.id.spinner);


        spinnerList = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(getContext() , android.R.layout.simple_list_item_1 , spinnerList);


        database = new Database(getContext());
        Preferences preferences = new Preferences(getContext());

        String userName =  preferences.getIdentifier(USER_NAME);

        customer = getCustomerInformation(userName);

        database.open();

        Cursor cursor = database.getAllStores();
        spinnerList.add("Select a location to be delivered or pick up");
        spinnerList.add("My address");

        while (cursor.moveToNext()){

            String stName = cursor.getString(1);
            spinnerList.add(stName);
        }
        database.close();
        spinner.setAdapter(spinnerAdapter);

        address.setText(customer.getAddress());
        cardNumber.setText(String.valueOf(customer.getCreditCard().getCreditCardNumber()));
        securityNumber.setText(String.valueOf(customer.getCreditCard().getSecurityCreditCardNumber()));

        ((BuyActivity) getActivity()).setValidate(new BuyActivity.onValidateFields(){
            @Override
            public void onValidate(){

                validateFields();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!spinner.getSelectedItem().toString().equals("Select a location to be delivered or pick up") &&
                   !spinner.getSelectedItem().toString().equals("My address")) {

                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alert.setTitle("Reservation message");
                    alert.setMessage("Once payment is confirmed, the product will be reserved at the branch " + spinner.getSelectedItem().toString());
                    alert.create();

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });
                    alert.show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }


    public void validateFields() {

        if(address.getText().toString().isEmpty()){

            Toast.makeText(getContext() , "Address is empty" , Toast.LENGTH_LONG).show();
            address.requestFocus();

        }else if(cardNumber.getText().toString().isEmpty()){

            Toast.makeText(getContext() , "Card number is empty" , Toast.LENGTH_LONG).show();
            cardNumber.requestFocus();

        }else if(securityNumber.getText().toString().isEmpty()){

            Toast.makeText(getContext() , "Security number is empty" , Toast.LENGTH_LONG).show();
            securityNumber.requestFocus();

        }else if(spinner.getSelectedItem().toString().equals("Select a location to be delivered or pick up")){

            Toast.makeText(getContext() , "Select a location" , Toast.LENGTH_LONG).show();
            spinner.requestFocus();

        }else {

            String voucherString = voucherNumber.getText().toString();
            int voucherInt = 0;

            if (!voucherString.isEmpty()){

                voucherInt = Integer.parseInt(voucherString);
                checkVoucher(String.valueOf(voucherInt));

            }else {

                database.open();
                database.updateUserInformation(customer);
                database.updateCreditCardInformation(customer.getCreditCard());
                database.close();

                int userId = customer.getUserId();

                paymentCallBack.onConfirmPayment(voucherInt , userId);
            }


        }

    }

    @Override
    public void onAttach(Context context) {

        try{

            paymentCallBack = (Payment) context;
        }catch (ClassCastException ex){

            throw new ClassCastException(this.toString()
                    + " must implement onConfirmPayment");
        }


        super.onAttach(context);
    }

    private Customer getCustomerInformation(String userName){

        Customer customer;

        database.open();
        Cursor cursor = database.getUserByUsername(userName);
        cursor.moveToFirst();

        String decodePassword = cursor.getString(2);

        CreditCard creditCard = new CreditCard(cursor.getInt(4) , cursor.getInt(6) , cursor.getInt(7) , cursor.getString(5));
        customer = new Customer(cursor.getInt(0) , cursor.getString(1) , decodePassword , cursor.getString(3) , false , creditCard);

        database.close();
        return customer;
    }

    private void checkVoucher(final String voucherNumber){

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Configure.REQUEST_VOUCHER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONObject vouch =  jsonObject.getJSONObject("Data");
                        String data = vouch.getString("Voucher");

                        database.open();
                        database.updateUserInformation(customer);
                        database.updateCreditCardInformation(customer.getCreditCard());
                        database.close();

                        int userId = customer.getUserId();

                        paymentCallBack.onConfirmPayment(Integer.parseInt(data) , userId);


                    }else {

                        Toast.makeText(getContext() , "Voucher not found" , Toast.LENGTH_LONG).show();
                    }


                }catch(JSONException ex){

                    Log.i("Json error" , ex.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext() , "Error: " + error.toString() , Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String , String> params = new HashMap<>();
                params.put("voucher_number" , voucherNumber);

                return params;
            }
        };

        String myTag = "req_voucher";
        AndroidLoginController.getmInstance().addToRequestQueue(stringRequest, myTag);

    }
}
