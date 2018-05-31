package com.example.caio.clothingstore.Main.Activities;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.caio.clothingstore.Main.Adapter.ClothingAdapter;
import com.example.caio.clothingstore.Main.Database.Database;
import com.example.caio.clothingstore.Main.Helper.Base64Custom;
import com.example.caio.clothingstore.Main.Helper.Preferences;
import com.example.caio.clothingstore.Main.Models.Clothes;
import com.example.caio.clothingstore.Main.Models.Manager;
import com.example.caio.clothingstore.Main.Models.Store;
import com.example.caio.clothingstore.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Clothes> clotheList;
    private ArrayList<Clothes> productsSort;
    private ClothingAdapter clothingAdapter;
    private GridView gridView;
    private ImageView roundCheck;
    private ImageView notInStock;
    private SearchView searchView;
    private TextView itemsStock;
    private int itemClicked;


    private final String KEEP_USER_LOGGED = "LOGGED";
    private final String IS_LOGGED = "IS_LOGGED";
    private final String ITEMS_TOTAL_COUNT = "ITEMS_TOTAL_COUNT";
    private final String ITEMS_TOTAL_SUM = "ITEMS_TOTAL_SUM";
    private final int PERMISSION_REQUEST_CODE = 1;

    private Database database = new Database(this);

    private HashMap<Integer , Integer> productsMapCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(Build.VERSION.SDK_INT > 16){

            if (ContextCompat.checkSelfPermission(this
                    , Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_DENIED
            ||
                ContextCompat.checkSelfPermission(this
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_DENIED )
            {


                String[] listPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE ,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE };


                ActivityCompat.requestPermissions(this , listPermissions , PERMISSION_REQUEST_CODE);
            }
        }


        clotheList = new ArrayList<>();
        productsMapCount = new HashMap<>();
        productsSort = new ArrayList<>();
        clothingAdapter = new ClothingAdapter(this, clotheList);
        database.getWritableDatabase();

        database.open();

        Cursor cursor = database.getAllClothes();

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                clotheList.add(new Clothes(cursor.getInt(0),    // ID
                                           cursor.getString(2), // Clothe name
                                           cursor.getDouble(4), // Clothe price
                                           cursor.getInt(3)));  // Clothe numbers in stock

            }
        }else {

            //Initial data to database

            database.insertStores(new Store("Avondale", "4049090", 'Y'));
            database.insertStores(new Store("CBD Auckland", "4041111", 'N'));
            database.insertStores(new Store("Birkenhead", "7821231", 'N'));
            database.insertStores(new Store("Mission bay", "4048787", 'N'));

            database.insertClothes(new Clothes("Trousers", 17.21, 3), 1);
            database.insertClothes(new Clothes("Skirt", 4.99, 4), 1);
            database.insertClothes(new Clothes("Jeans", 3.13, 2), 4);
            database.insertClothes(new Clothes("T-Shirt", 44.55, 8), 2);
            database.insertClothes(new Clothes("Jacket", 38.11, 7), 3);
            database.insertClothes(new Clothes("Hat", 29.77, 4), 3);


            //Encryption
            String password = Base64Custom.encodeBase64("caio123");

            database.insertManager(new Manager(0 , "caio@yahoo.com" , password , "General manager" ) ,1);
            database.insertManager(new Manager(0 , "vini@yahoo.com" , password , "Local manager" ) ,2);
            database.insertManager(new Manager(0 , "reis@yahoo.com" , password , "Local manager" ) ,3);
            database.insertManager(new Manager(0 , "nascimento@yahoo.com" , password , "Local manager" ) ,4);

            cursor = database.getAllClothes();


            while (cursor.moveToNext()) {

                clotheList.add(new Clothes(cursor.getInt(0),    // ID
                                           cursor.getString(2), // Clothe name
                                           cursor.getDouble(4), // Clothe price
                                           cursor.getInt(3)));  // Clothe numbers in stock

            }

        }
        database.close();

        clothingAdapter.notifyDataSetChanged();

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(clothingAdapter);
        gridView.setHorizontalSpacing(2);
        gridView.setVerticalSpacing(2);

        Preferences preferences = new Preferences(this);
        String keepLogged = preferences.getIdentifier(KEEP_USER_LOGGED);
        if (!keepLogged.equals("YES")) {

            preferences.saveData(IS_LOGGED , "NO");
        }

        preferences.saveData(ITEMS_TOTAL_COUNT , "0");
        preferences.saveData(ITEMS_TOTAL_SUM , "0");


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                roundCheck = (ImageView) view.findViewById(R.id.imageViewRoundCheck);
                itemClicked = i;

                Clothes clothes = clotheList.get(i);


                Intent intent = new Intent(getApplicationContext(), BuyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ITEM_SELECTED",clothes);

                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                roundCheck = (ImageView) view.findViewById(R.id.imageViewRoundCheck);
                notInStock = (ImageView) view.findViewById(R.id.imageViewNotInStock);
                itemsStock = (TextView) view.findViewById(R.id.textViewItemsStock);

                if(roundCheck.getVisibility() == ImageView.VISIBLE){

                    Clothes clothes = clotheList.get(i);
                    checkDeleteItem(clothes , itemsStock);
                    return true;
                }else{

                    Toast.makeText(getApplicationContext() , "This item was not added to your list" , Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
          super.onPrepareOptionsMenu(menu);

          MenuItem searchViewMenuItem = menu.findItem(R.id.search);

        if(searchViewMenuItem != null){

          searchView = (SearchView) searchViewMenuItem.getActionView();
          ImageView v = (ImageView) searchView.findViewById(R.id.search_button);
          v.setImageResource(R.drawable.ic_search_red);

          searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override
              public boolean onQueryTextSubmit(String s) {


                  return false;
              }

              @Override
              public boolean onQueryTextChange(String s) {


                  int textLength = s.length();

                  if (textLength == 1){

                      if(productsSort.size() < clotheList.size()) {

                          for (Clothes c : clotheList) {

                              productsSort.add(c);
                          }
                      }

                      clotheList.clear();

                  }else if(textLength == 0){

                      for (Clothes c : clotheList){
                          ListIterator<Clothes> iterator = productsSort.listIterator();

                          while (iterator.hasNext()){
                              int next = iterator.next().getClothIdNumber();

                              if(c.getClothIdNumber() == next){

                                  iterator.set(c);
                                  break;
                              }
                          }
                      }

                      clotheList.clear();

                      for(Clothes c: productsSort){

                          clotheList.add(c);
                      }

                      productsSort.clear();

                  }else if (textLength > 1){

                      clotheList.clear();
                  }


                  for(int i=0; i < productsSort.size() ; i++ ){
                      if(textLength <= productsSort.get(i).getName().length()){
                          Log.d("Sort" , productsSort.get(i).getName().toLowerCase().trim());
                          if(productsSort.get(i).getName().toLowerCase().trim().contains(
                                  s.toLowerCase().trim())){

                              clotheList.add(productsSort.get(i));
                          }
                      }
                  }

                  clothingAdapter.notifyDataSetChanged();
                  return false;
              }
          });

        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {

            case R.id.settings_item:

                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.login_admin:

                View view = getLayoutInflater().inflate(R.layout.dialog_admin_login, null);

                final EditText userNameEditText = (EditText) view.findViewById(R.id.editTextAdminUserName);
                final EditText passwordEditText = (EditText) view.findViewById(R.id.editTextAdminPassword);

                final AlertDialog alertDialog = new AlertDialog.Builder
                         (this , android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT )
                           .setView(view)
                           .setTitle("Login")
                           .setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {

                               }
                           }).setNegativeButton("CANCEL" , null).create();


                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String userName = userNameEditText.getText().toString();
                                String userPassword = passwordEditText.getText().toString();

                                if(userName.isEmpty()){

                                    Toast.makeText(getApplicationContext() , "User name is empty" , Toast.LENGTH_LONG).show();
                                    userNameEditText.requestFocus();

                                }else if(userPassword.isEmpty()){

                                    Toast.makeText(getApplicationContext() , "Password is empty" , Toast.LENGTH_LONG).show();
                                    passwordEditText.requestFocus();

                                }else {

                                    database.open();

                                    Cursor cursor = database.getManagerByName(userName , Base64Custom.encodeBase64(userPassword));

                                    if (cursor.getCount() > 0){

                                        cursor.moveToFirst();
                                        String managerRole = cursor.getString(1);
                                        String managerPassword = cursor.getString(2);

                                        if(!managerPassword.equals("NOT FOUND")){

                                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);

                                            intent.putExtra("list_products" , clotheList);
                                            intent.putExtra("role_manager" , managerRole);
                                            startActivityForResult(intent ,0 );
                                            alertDialog.dismiss();

                                        }else {

                                            Toast.makeText(getApplicationContext() , "Password incorrect" , Toast.LENGTH_LONG).show();
                                        }

                                    }else {

                                        Toast.makeText(getApplicationContext() , "User not found" , Toast.LENGTH_LONG).show();
                                    }

                                    database.close();

                                }

                            }
                        });


                        negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Toast.makeText(getApplicationContext(), "You clicked on CANCEL", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });

                    }
                });

                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void checkDeleteItem(Clothes c , TextView t){

        final Clothes clothes = c;
        final TextView itemsInstock = t;
        final AlertDialog.Builder alert = new AlertDialog.Builder(this , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alert.setTitle("Delete Item");
        alert.setMessage("Are you sure you want to delete this Item from your list ?");
        alert.create();

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int qtd = productsMapCount.get(clothes.getClothIdNumber());
                double totalList = qtd * clothes.getPrice();

                Preferences preferences = new Preferences(getApplicationContext());
                double totalSumBase = Double.parseDouble(preferences.getIdentifier(ITEMS_TOTAL_SUM));
                int totalCountBase = Integer.parseInt(preferences.getIdentifier(ITEMS_TOTAL_COUNT));
                clothes.setNumberInStock(clothes.getNumberInStock() + qtd);
                clothes.setPicked(false);

                String stock = "Items in stock: " + clothes.getNumberInStock();
                itemsInstock.setText(stock);

                preferences.saveData(ITEMS_TOTAL_SUM , String.format("%.2f" ,totalSumBase - totalList));
                preferences.saveData(ITEMS_TOTAL_COUNT , String.valueOf(totalCountBase - qtd));

                roundCheck.setVisibility(ImageView.INVISIBLE);
                notInStock.setVisibility(ImageView.INVISIBLE);

                productsMapCount.remove(clothes.getClothIdNumber());
                Toast.makeText(getApplicationContext() , "Item deleted" , Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Toast.makeText(getApplicationContext() , "You clicked no" , Toast.LENGTH_LONG).show();
            }
        });

        alert.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data != null){

            Bundle extras = data.getExtras();
            Clothes objectResult;
            String result = extras.getString("result");

            switch (result) {

                case "PRODUCT_ADDED":

                    objectResult = (Clothes) extras.getSerializable("value");
                    int count = extras.getInt("count");

                    productsMapCount.put(objectResult.getClothIdNumber() , count);
                    clotheList.set(itemClicked , objectResult);
                    clothingAdapter.notifyDataSetChanged();

                    break;

                case "PAYMENT_CONFIRMED":

                    productsMapCount.clear();
                    database.open();


                    int userId = extras.getInt("userId");

                    objectResult = (Clothes) extras.getSerializable("value");
                    clotheList.set(itemClicked , objectResult);
                    for(Clothes c : clotheList){

                        if(c.isPicked()){

                            database.updateClothesInformation(c);
                            database.insertOrder(userId , c.getClothIdNumber());
                        }

                        c.setPicked(false);
                    }

                    clothingAdapter.notifyDataSetChanged();
                    break;

                case "PRODUCTS_REGISTERED":

                    ArrayList<Clothes> productsRegistered = (ArrayList<Clothes>) extras.getSerializable("values");

                        clotheList.clear();

                        for(Clothes c: productsRegistered){

                            clotheList.add(c);
                        }
                        clothingAdapter.notifyDataSetChanged();


                    break;

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
