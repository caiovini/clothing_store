package com.example.caio.clothingstore.Main.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.caio.clothingstore.Main.Models.Clothes;
import com.example.caio.clothingstore.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView productPhotoAdd;
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productIdEditText;
    private EditText inStockEditText;
    private Button submitButton;
    private Button deleteButton;
    private boolean isImageAdded = false;
    private boolean isEdit = false;


    private ArrayList<Clothes> listProducts = new ArrayList<>();
    private ArrayList<Integer> ids = new ArrayList<>();
    private final int GALLERY_PICTURE = 1;
    private File file;
    private Clothes clotheEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            listProducts = (ArrayList<Clothes>) extras.getSerializable("list_products");

            for (Clothes c : listProducts) {

                ids.add(c.getClothIdNumber());
            }

        }


        productPhotoAdd = (ImageView) findViewById(R.id.imageViewPhotoAdd);
        productPhotoAdd.setOnClickListener(this);

        submitButton = (Button) findViewById(R.id.buttonSubmitId);
        submitButton.setOnClickListener(this);

        deleteButton = (Button) findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(this);

        productNameEditText = (EditText) findViewById(R.id.editTextProductName);
        productPriceEditText = (EditText) findViewById(R.id.editTextProductPrice);
        productIdEditText = (EditText) findViewById(R.id.editTextProductId);
        inStockEditText = (EditText) findViewById(R.id.editTextInStock);


    }

    @Override
    public void onBackPressed() {

        Intent intentResult = new Intent();
        //Send back if user adds product to the list
        intentResult.putExtra("result", "PRODUCTS_REGISTERED");
        intentResult.putExtra("values", listProducts);
        setResult(Activity.RESULT_OK, intentResult);
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        View view = getLayoutInflater().inflate(R.layout.dialog_edit_products, null);

        final Spinner spinnerIds = (Spinner) view.findViewById(R.id.spinnerIds);

        ArrayAdapter<String> adapter;
        List<String> list = new ArrayList<>();
        list.add("ID");

        for (Integer i : ids) {

            list.add(String.format("%04d", i));
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIds.setAdapter(adapter);


        final AlertDialog alertDialog = new AlertDialog.Builder
                (this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setView(view)
                .setTitle("Choose item id")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("CANCEL", null).create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String userSelection = spinnerIds.getSelectedItem().toString();

                        if (userSelection.equals("ID")) {

                            Toast.makeText(getApplicationContext() , "You must select an ID" , Toast.LENGTH_LONG).show();

                        } else {

                            int selectedItem = Integer.parseInt(userSelection);
                            clotheEdit = null;

                            for (Clothes c : listProducts) {

                                if (c.getClothIdNumber() == selectedItem) {

                                    clotheEdit = c;
                                    break;
                                }
                            }

                            if (clotheEdit != null) {

                                completeFields(clotheEdit);
                                deleteButton.setVisibility(Button.VISIBLE);
                            }

                            alertDialog.dismiss();
                        }
                    }
                });

                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });


            }
        });

        alertDialog.show();
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonDelete:

                int index = listProducts.indexOf(clotheEdit);
                listProducts.remove(index);

                ids.clear();

                for (Clothes c : listProducts) {

                    ids.add(c.getClothIdNumber());
                }

                deleteButton.setVisibility(Button.INVISIBLE);
                productNameEditText.setText("");
                productPriceEditText.setText("");
                productIdEditText.setEnabled(true);
                productIdEditText.setText("");
                inStockEditText.setText("");
                productPhotoAdd.setImageResource(R.drawable.ic_add_to_photos_light_blue);
                isEdit = false;
                isImageAdded = false;


                Toast.makeText(this , "Item removed" , Toast.LENGTH_LONG).show();

                break;

            case R.id.imageViewPhotoAdd:

                if (!isEdit) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_PICTURE);
                }
                break;

            case R.id.buttonSubmitId:

                if (!isImageAdded) {

                    Toast.makeText(this, "Add new photo", Toast.LENGTH_LONG).show();
                    productPhotoAdd.requestFocus();

                } else if (productNameEditText.getText().toString().isEmpty()) {

                    Toast.makeText(this, "Product name is empty", Toast.LENGTH_LONG).show();
                    productNameEditText.requestFocus();

                } else if (productPriceEditText.getText().toString().isEmpty()) {

                    Toast.makeText(this, "Product price is empty", Toast.LENGTH_LONG).show();
                    productPriceEditText.requestFocus();

                } else if (productIdEditText.getText().toString().isEmpty() && isEdit) {

                    Toast.makeText(this, "Product id is empty", Toast.LENGTH_LONG).show();
                    productIdEditText.requestFocus();

                } else if (ids.indexOf(Integer.parseInt(productIdEditText.getText().toString())) != -1 && !isEdit) {

                    Toast.makeText(this, "Product id already exists", Toast.LENGTH_LONG).show();
                    productIdEditText.requestFocus();

                }else if(inStockEditText.getText().toString().isEmpty() ||
                         Integer.parseInt(inStockEditText.getText().toString()) == 0){

                    Toast.makeText(this, "Products in stock invalid", Toast.LENGTH_LONG).show();
                    inStockEditText.requestFocus();

                } else {

                    if (isEdit){

                        int position = listProducts.indexOf(clotheEdit);

                        if(clotheEdit.getStorageAdress().isEmpty()){

                            listProducts.set(position ,
                                         new Clothes(Integer.parseInt(productIdEditText.getText().toString()),
                                             productNameEditText.getText().toString(),
                                             Double.parseDouble(productPriceEditText.getText().toString()),
                                             Integer.parseInt(inStockEditText.getText().toString())));
                        }else{

                            listProducts.set(position ,
                                         new Clothes(
                                             Integer.parseInt(productIdEditText.getText().toString()),
                                             productNameEditText.getText().toString(),
                                             Double.parseDouble(productPriceEditText.getText().toString()),
                                             file.getAbsolutePath(),
                                             Integer.parseInt(inStockEditText.getText().toString())));
                        }

                        Toast.makeText(this, "Product edited", Toast.LENGTH_LONG).show();

                    }else {


                        listProducts.add(new Clothes(
                                             Integer.parseInt(productIdEditText.getText().toString()),
                                             productNameEditText.getText().toString(),
                                             Double.parseDouble(productPriceEditText.getText().toString()),
                                             file.getAbsolutePath(),
                                             Integer.parseInt(inStockEditText.getText().toString())));

                        ids.add(Integer.parseInt(productIdEditText.getText().toString()));
                        Toast.makeText(this, "Product added", Toast.LENGTH_LONG).show();
                    }

                    productNameEditText.setText("");
                    productPriceEditText.setText("");
                    productIdEditText.setEnabled(true);
                    productIdEditText.setText("");
                    inStockEditText.setText("");
                    productPhotoAdd.setImageResource(R.drawable.ic_add_to_photos_light_blue);
                    isEdit = false;
                    isImageAdded = false;
                }

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Bitmap bitmap = null;

        if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {


            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String selectedImagePath = c.getString(columnIndex);
                c.close();

                file = new File(selectedImagePath);

                if (file.exists()) {

                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath()); // load

                    if (bitmap == null) {

                        Toast.makeText(this, "It was not possible to load image", Toast.LENGTH_LONG).show();
                    } else {

                        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                        productPhotoAdd.setImageBitmap(bitmap);
                        isImageAdded = true;
                        isEdit = false;
                        productIdEditText.setEnabled(true);
                    }

                }

            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void completeFields(Clothes clothes){

        productNameEditText.setText(clothes.getName());
        productPriceEditText.setText(String.valueOf(clothes.getPrice()));
        productIdEditText.setText(String.valueOf(clothes.getClothIdNumber()));
        inStockEditText.setText(String.valueOf(clothes.getNumberInStock()));

        if(clothes.getStorageAdress().isEmpty()) {

            switch (clothes.getClothIdNumber()) {

                case 0:

                    productPhotoAdd.setImageResource(R.drawable.trousers);
                    break;

                case 111:

                    productPhotoAdd.setImageResource(R.drawable.skirt);
                    break;

                case 222:

                    productPhotoAdd.setImageResource(R.drawable.jeans);
                    break;

                case 333:

                    productPhotoAdd.setImageResource(R.drawable.t_shirt);
                    break;

                case 444:

                    productPhotoAdd.setImageResource(R.drawable.jacket);
                    break;

                case 555:

                    productPhotoAdd.setImageResource(R.drawable.hat);
                    break;
            }
        }else{

            file = new File(clothes.getStorageAdress());

            if(file.exists()) {

                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath()); // load

                if (bitmap == null){

                    productPhotoAdd.setImageResource(R.mipmap.ic_launcher);
                }else {

                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                    productPhotoAdd.setImageBitmap(bitmap);
                }

            }else{

                productPhotoAdd.setImageResource(R.mipmap.ic_launcher);
            }


        }

        isImageAdded = true;
        isEdit = true;
        productIdEditText.setEnabled(false);
    }

}

