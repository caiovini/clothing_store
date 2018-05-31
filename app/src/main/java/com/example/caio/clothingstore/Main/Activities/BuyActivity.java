package com.example.caio.clothingstore.Main.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.caio.clothingstore.Main.Fragments.DetailsFragment;
import com.example.caio.clothingstore.Main.Fragments.LoginApp;
import com.example.caio.clothingstore.Main.Helper.Preferences;
import com.example.caio.clothingstore.Main.Models.Clothes;
import com.example.caio.clothingstore.Main.Interface.Payment;
import com.example.caio.clothingstore.Main.Models.Customer;
import com.example.caio.clothingstore.R;
import java.io.File;

public class BuyActivity extends AppCompatActivity implements LoginApp.onCommitTransaction, View.OnClickListener, Payment {


    private ImageView productImage;
    private TextView itemsTextView;
    private TextView totalTextView;
    private TextView stockProducts;
    private TextView nameProduct;
    private TextView priceProduct;
    private Preferences preferences;
    private onValidateFields validateCallBack;

    private Clothes clothes;
    private int numberInStock;
    private int productsCount = 0;

    private ImageButton addToChartButton;
    private ImageButton purchaseButton;
    private final String ITEMS_TOTAL_COUNT = "ITEMS_TOTAL_COUNT";
    private final String ITEMS_TOTAL_SUM = "ITEMS_TOTAL_SUM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);


        preferences = new Preferences(this);

        productImage = (ImageView) findViewById(R.id.imageViewBuyProduct);

        itemsTextView = (TextView) findViewById(R.id.textViewItems);
        totalTextView = (TextView) findViewById(R.id.textViewTotal);
        nameProduct = (TextView) findViewById(R.id.textViewProductName);
        priceProduct = (TextView) findViewById(R.id.textViewPriceEach);
        stockProducts = (TextView) findViewById(R.id.textViewItemsInStock);

        addToChartButton = (ImageButton) findViewById(R.id.buttonBuy);
        addToChartButton.setOnClickListener(this);

        purchaseButton = (ImageButton) findViewById(R.id.buttonAddToBag);
        purchaseButton.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        clothes = (Clothes) extras.getSerializable("ITEM_SELECTED");
        numberInStock = clothes.getNumberInStock();


        String text = "Price  : " + clothes.getPrice();
        priceProduct.setText(text);

        text = "Name : " + clothes.getName();
        nameProduct.setText(text);

        text = "Items in stock : " + clothes.getNumberInStock();
        stockProducts.setText(text);



        if(clothes.getStorageAdress().isEmpty()) {

            switch (clothes.getClothIdNumber()) {

                case 1:

                    productImage.setImageResource(R.drawable.trousers);
                    break;

                case 2:

                    productImage.setImageResource(R.drawable.skirt);
                    break;

                case 3:

                    productImage.setImageResource(R.drawable.jeans);
                    break;

                case 4:

                    productImage.setImageResource(R.drawable.t_shirt);
                    break;

                case 5:

                    productImage.setImageResource(R.drawable.jacket);
                    break;

                case 6:

                    productImage.setImageResource(R.drawable.hat);
                    break;
            }
        }else{

           File file = new File(clothes.getStorageAdress());

            if(file.exists()) {

                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath()); // load

                if (bitmap == null){

                    productImage.setImageResource(R.mipmap.ic_launcher);
                }else {

                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                    productImage.setImageBitmap(bitmap);
                }

            }else{

                productImage.setImageResource(R.mipmap.ic_launcher);
            }


        }

        String sumString = preferences.getIdentifier(ITEMS_TOTAL_SUM);
        String countString = preferences.getIdentifier(ITEMS_TOTAL_COUNT);

        if (sumString.equals("NOT FOUND")){

            String totalPrice = "Total : " + 0;
            totalTextView.setText(totalPrice);

            String items = "Items : " + 0;
            itemsTextView.setText(items);
        }else {

            String totalPrice = "Total : " + sumString;
            totalTextView.setText(totalPrice);

            String items = "Items : " + countString;
            itemsTextView.setText(items);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_share , menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {

            case R.id.settings_buy_item:

                intent = new Intent(this , SettingsActivity.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.share_item:

                Uri localImage = Uri.parse(productImage.toString());


                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM , localImage);
                intent.setType("image/");
                startActivity(Intent.createChooser(intent , "Share image"));
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {

            Bundle extras = data.getExtras();

            String result = extras.getString("result");

            if (result.equals("LOG_OUT")) {

                preferences.saveData(ITEMS_TOTAL_COUNT, "0");
                preferences.saveData(ITEMS_TOTAL_SUM, "0");
                onBackPressed();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTransaction(Customer customer) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,  new DetailsFragment()).commit();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.buttonBuy:

                String itemsCount = preferences.getIdentifier(ITEMS_TOTAL_COUNT);

                if (validateCallBack != null) {

                    if(itemsCount.equals("0")){

                        Toast.makeText(this,"Your list is empty" , Toast.LENGTH_LONG).show();
                    }else {

                        validateCallBack.onValidate();
                    }
                }else{

                    Toast.makeText(this,"You must login to continue" , Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.buttonAddToBag:

                double sum;
                int count;

                productsCount ++;

                if (productsCount > numberInStock){

                    Toast.makeText(this , "This item is not in stock anymore" , Toast.LENGTH_LONG).show();

                }else {

                    Intent intentResult = new Intent();
                    clothes.setPicked(true);
                    clothes.setNumberInStock(clothes.getNumberInStock() -1);

                    //Send back if user adds product to the list
                    intentResult.putExtra("result", "PRODUCT_ADDED");
                    intentResult.putExtra("value", clothes);
                    intentResult.putExtra("count", productsCount);
                    setResult(Activity.RESULT_OK, intentResult);


                    String sumString = preferences.getIdentifier(ITEMS_TOTAL_SUM);
                    String countString = preferences.getIdentifier(ITEMS_TOTAL_COUNT);

                    if (sumString.equals("NOT FOUND")) {

                        sum = 0;
                        count = 0;
                    } else {

                        sum = Double.parseDouble(sumString);
                        count = Integer.parseInt(countString);
                    }

                    count++;
                    sum += clothes.getPrice();

                    preferences.saveData(ITEMS_TOTAL_COUNT, String.valueOf(count));
                    preferences.saveData(ITEMS_TOTAL_SUM, String.format("%.2f", sum));

                    String text = "Total : " + String.format("%.2f", sum);
                    totalTextView.setText(text);

                    text = "Items : " + count;
                    itemsTextView.setText(text);

                    text = "Items in stock : " + clothes.getNumberInStock();
                    stockProducts.setText(text);

                    Toast.makeText(this, "Item added to your list", Toast.LENGTH_SHORT).show();

                    break;

                }
        }

    }


    @Override
    public void onConfirmPayment(int voucher , final int userId) {

        String totalString = preferences.getIdentifier(ITEMS_TOTAL_SUM);
        String discountTotal = String.format("%.2f" , Double.parseDouble(totalString) * 0.9);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if(voucher != 0){

            alert.setTitle("Payment confirmation");
            alert.setMessage("You will have 10% of discount on your purchase, total :" + discountTotal);
            alert.create();

        }else{

            alert.setTitle("Payment confirmation");
            alert.setMessage("Your purchase total :" + totalString);
            alert.create();

        }

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intentResult = new Intent();
                    //Send back if user adds product to the list
                    intentResult.putExtra("result" , "PAYMENT_CONFIRMED");
                    intentResult.putExtra("value", clothes);
                    intentResult.putExtra("userId", userId);
                    setResult(Activity.RESULT_OK , intentResult);

                    Toast.makeText(getApplicationContext() , "Payment approved" , Toast.LENGTH_LONG).show();
                    preferences.saveData(ITEMS_TOTAL_COUNT , "0");
                    preferences.saveData(ITEMS_TOTAL_SUM , "0");
                    onBackPressed();

                }
            });

            alert.show();
    }



    public interface onValidateFields{

        void onValidate();
    }

    public void setValidate(onValidateFields field){

        this.validateCallBack = field;
    }

}
