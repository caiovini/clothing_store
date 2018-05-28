package com.example.caio.clothingstore.Main.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.caio.clothingstore.Main.Models.Clothes;
import java.io.File;
import java.util.ArrayList;
import com.example.caio.clothingstore.R;


public class ClothingAdapter extends ArrayAdapter<Clothes>{

    private ArrayList<Clothes> clothes;
    private Context context;

    public ClothingAdapter(@NonNull Context context, ArrayList<Clothes> objects) {

        super(context, 0 , objects);
        this.context = context;
        this.clothes = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (this.clothes != null){

            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_base , parent , false);


            TextView productName = (TextView) view.findViewById(R.id.textViewClothingName);
            TextView productPrice = (TextView) view.findViewById(R.id.textViewPrice);
            TextView itemsStock = (TextView) view.findViewById(R.id.textViewItemsStock);
            ImageView productImage = (ImageView) view.findViewById(R.id.imageViewProduct);
            ImageView roundCheck = (ImageView) view.findViewById(R.id.imageViewRoundCheck);
            ImageView notInStock = (ImageView) view.findViewById(R.id.imageViewNotInStock);


            Clothes clothes = this.clothes.get(position);

            if(clothes.getNumberInStock() == 0){

                notInStock.setVisibility(ImageView.VISIBLE);}



            if(clothes.isPicked()){

                roundCheck.setVisibility(ImageView.VISIBLE);}



            if(clothes.getStorageAdress().isEmpty()) {

                switch (clothes.getName()) {

                    case "Trousers":

                        productImage.setImageResource(R.drawable.trousers);
                        break;

                    case "Skirt":

                        productImage.setImageResource(R.drawable.skirt);
                        break;

                    case "Jeans":

                        productImage.setImageResource(R.drawable.jeans);
                        break;

                    case "T-Shirt":

                        productImage.setImageResource(R.drawable.t_shirt);
                        break;

                    case "Jacket":

                        productImage.setImageResource(R.drawable.jacket);
                        break;

                    case "Hat":

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

            String price = "NZD: " + clothes.getPrice();
            String stock = "Items in stock: " + clothes.getNumberInStock();

            productName.setText(clothes.getName());
            productPrice.setText(price);
            itemsStock.setText(stock);


        }

        return view;
    }

    @Override
    public int getCount() {

        return this.clothes.size();
    }
}
