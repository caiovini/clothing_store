package com.example.caio.clothingstore.Main.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.caio.clothingstore.Main.Activities.BuyActivity;
import com.example.caio.clothingstore.Main.Interfaces.Payment;
import com.example.caio.clothingstore.R;

public class DetailsFragment extends Fragment {

    private EditText address;
    private EditText postalCode;
    private EditText cardNumber;
    private EditText securityNumber;
    private EditText voucherNumber;
    private Spinner spinner;
    private Payment paymentCallBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_details, container, false);


        address = (EditText) view.findViewById(R.id.editTextAdress);
        postalCode = (EditText) view.findViewById(R.id.editTextPostalCode);
        cardNumber = (EditText) view.findViewById(R.id.editTextCardNumber);
        securityNumber = (EditText) view.findViewById(R.id.editTextSecurityNumber);
        voucherNumber = (EditText) view.findViewById(R.id.editTextVoucher);
        spinner = (Spinner) view.findViewById(R.id.spinner);

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

        }else if(postalCode.getText().toString().isEmpty()){

            Toast.makeText(getContext() , "Postal code is empty" , Toast.LENGTH_LONG).show();
            postalCode.requestFocus();

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

            if (!voucherString.isEmpty())
                    voucherInt = Integer.parseInt(voucherString);

            paymentCallBack.onConfirmPayment(voucherInt);

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

}
