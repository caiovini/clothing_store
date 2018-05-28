package com.example.caio.clothingstore.Main.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.caio.clothingstore.Main.Models.Clothes;
import com.example.caio.clothingstore.Main.Models.CreditCard;
import com.example.caio.clothingstore.Main.Models.Manager;
import com.example.caio.clothingstore.Main.Models.Store;
import com.example.caio.clothingstore.Main.Models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "clothing_store.db";
    private SQLiteDatabase sqLiteDatabase;
    private static final int DATABASE_VERSION = 1;

    /*
       Table tbl_user
       Primary key --> COL_USER_ID
    */
    private final String TBL_USER = "tbl_customer";
    private final String COL_USER_ID = "user_id";
    private final String COL_USER_NAME = "user_name";
    private final String COL_USER_PASSWORD = "user_password";
    private final String COL_ADDRESS = "address";

    /*
       Table tbl_credit_card
       Primary key --> COL_CREDIT_CARD_ID
       Foreign key --> COL_USER_ID from TBL_USER
    */
    private final String TBL_CREDIT_CARD = "tbl_credit_card";
    private final String COL_CREDIT_CARD_ID = "credit_card_id";
    private final String COL_CREDIT_CARD_NUMBER = "credit_card_number";
    private final String COL_CREDIT_CARD_SECURITY_NUMBER = "security_card_number";
    private final String COL_CREDIT_CARD_TYPE = "credit_card_ty";


    /*
       Table tbl_order
       Primary key --> COL_ORDER_ID
       Foreign key --> COL_USER_ID from TBL_USER
       foreign key --> COL_CLOTHING_ID from TBL_CLOTHING
    */
    private final String TBL_ORDER = "tbl_order";
    private final String COL_ORDER_ID = "order_id";
    private final String COL_PURCHASE_DATE = "purchase_date";


    /*
       Table tbl_clothing
       Primary key --> COL_CLOTHING_ID
       Foreign key --> COL_STORE_ID from TBL_STORE
    */
    private final String TBL_CLOTHING = "tbl_clothing";
    private final String COL_CLOTHING_ID = "clothing_id";
    private final String COL_CLOTHE_NAME = "clothe_name";
    private final String COL_NUMBERS_STOCK = "numbers_stock";
    private final String COL_PRICE = "price ";


    /*
       Table tbl_store
       Primary key --> COL_STORE_ID
    */
    private final String TBL_STORE = "tbl_store";
    private final String COL_STORE_ID = "store_id";
    private final String COL_BRANCH_ADDRESS = "branch_address";
    private final String COL_BRANCH_TELEPHONE = "telephone_number";
    private final String COL_FLG_MAIN_BRANCH = "main_branch";

    /*
       Table tbl_manager
       Primary key --> COL_ID_MANAGER
       Foreign key --> COL_STORE_ID from TBL_STORE
    */
    private final String TBL_MANAGER = "tbl_manager";
    private final String COL_ID_MANAGER = "id_manager";
    private final String COL_MANAGER_NAME = "manager_name";
    private final String COL_MANAGER_PASSWORD = "manager_password";
    private final String COL_ROLE = "role";



    private Context c;


    public Database(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL
                ("create table " + TBL_USER +
                 "(" + COL_USER_ID + " integer primary key AUTOINCREMENT , " +
                       COL_USER_NAME + " text not null , " +
                       COL_USER_PASSWORD + " text not null , " +
                       COL_ADDRESS + " text not null);");

        sqLiteDatabase.execSQL
                ("create table " + TBL_CREDIT_CARD +
                 "("  + COL_CREDIT_CARD_ID + " integer primary key AUTOINCREMENT , " +
                        COL_USER_ID + " integer not null , " +
                        COL_CREDIT_CARD_NUMBER + " integer not null , " +
                        COL_CREDIT_CARD_SECURITY_NUMBER + " integer not null , " +
                        COL_CREDIT_CARD_TYPE + " text not null , " +
                        " FOREIGN KEY (" +  COL_USER_ID + ") REFERENCES " + TBL_USER + "("+ COL_USER_ID +") ON UPDATE CASCADE);");

        sqLiteDatabase.execSQL
                ("create table " + TBL_STORE +
                 "("  + COL_STORE_ID + " integer primary key AUTOINCREMENT , " +
                        COL_BRANCH_ADDRESS + " text not null , " +
                        COL_BRANCH_TELEPHONE + " integer not null , " +
                        COL_FLG_MAIN_BRANCH + " text not null);");


        sqLiteDatabase.execSQL
                ("create table " + TBL_MANAGER +
                 "("  + COL_ID_MANAGER + " integer primary key AUTOINCREMENT , " +
                        COL_STORE_ID + " integer not null , " +
                        COL_MANAGER_NAME + " text not null , " +
                        COL_MANAGER_PASSWORD + " text not null , " +
                        COL_ROLE + " text not null , " +
                        " FOREIGN KEY (" +  COL_STORE_ID + ") REFERENCES " + TBL_STORE + "("+ COL_STORE_ID +") ON UPDATE CASCADE);");


        sqLiteDatabase.execSQL
                ("create table " + TBL_CLOTHING +
                 "("  + COL_CLOTHING_ID + " integer primary key AUTOINCREMENT , " +
                        COL_STORE_ID + " integer not null , " +
                        COL_CLOTHE_NAME + " text not null , " +
                        COL_NUMBERS_STOCK + " integer not null , " +
                        COL_PRICE + " double not null , " +
                        " FOREIGN KEY (" +  COL_STORE_ID + ") REFERENCES " + TBL_STORE + "("+ COL_STORE_ID +") ON UPDATE CASCADE);");


        sqLiteDatabase.execSQL
                ("create table " + TBL_ORDER +
                 "("  + COL_ORDER_ID + " integer primary key AUTOINCREMENT , " +
                        COL_USER_ID  + " integer not null , " +
                        COL_CLOTHING_ID + " integer not null , " +
                        COL_PURCHASE_DATE + " date not null , " +
                        " FOREIGN KEY (" +  COL_USER_ID + ") REFERENCES " + TBL_USER + "("+ COL_USER_ID +") ON UPDATE CASCADE ," +
                        " FOREIGN KEY (" +  COL_CLOTHING_ID + ") REFERENCES " + TBL_CLOTHING + "("+ COL_CLOTHING_ID +") ON UPDATE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists " + TBL_USER);
        sqLiteDatabase.execSQL("drop table if exists " + TBL_CREDIT_CARD);
        sqLiteDatabase.execSQL("drop table if exists " + TBL_MANAGER);
        sqLiteDatabase.execSQL("drop table if exists " + TBL_CLOTHING);
        sqLiteDatabase.execSQL("drop table if exists " + TBL_ORDER);
        sqLiteDatabase.execSQL("drop table if exists " + TBL_STORE);
        onCreate(sqLiteDatabase);
    }

    public void open(){


        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON");

    }

    public void close(){ sqLiteDatabase.close(); }

    public boolean insertClothes(Clothes clothes , int storeId){

        ContentValues cv = new ContentValues();
        cv.put(COL_STORE_ID , storeId);
        cv.put(COL_CLOTHE_NAME , clothes.getName());
        cv.put(COL_NUMBERS_STOCK , clothes.getNumberInStock());
        cv.put(COL_PRICE , clothes.getPrice());

        return this.sqLiteDatabase.insert(TBL_CLOTHING , null , cv) != -1;
    }

    public boolean insertStores(Store store){

        ContentValues cv = new ContentValues();
        cv.put(COL_BRANCH_ADDRESS , store.getBranchAddress());
        cv.put(COL_BRANCH_TELEPHONE , store.getTelephoneNumber());
        cv.put(COL_FLG_MAIN_BRANCH , String.valueOf(store.getIsMainBranch()));

        return this.sqLiteDatabase.insert(TBL_STORE , null , cv) != -1;
    }


    public boolean insertUser(User user){


        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME , user.getUserName());
        cv.put(COL_USER_PASSWORD , user.getUserPassword());
        cv.put(COL_ADDRESS , user.getAddress());

        return this.sqLiteDatabase.insert(TBL_USER , null , cv) != -1;
    }

    public boolean insertManager(Manager manager , int storeId){

        ContentValues cv = new ContentValues();
        cv.put(COL_STORE_ID , storeId);
        cv.put(COL_MANAGER_NAME , manager.getManagerName());
        cv.put(COL_MANAGER_PASSWORD , manager.getManagerPassword());
        cv.put(COL_ROLE , manager.getRole());

        return this.sqLiteDatabase.insert(TBL_MANAGER , null , cv) != - 1;
    }

    public boolean insertCreditCard(CreditCard card , int userId){

        ContentValues cv = new ContentValues();
        cv.put(COL_USER_ID , userId);
        cv.put(COL_CREDIT_CARD_NUMBER , card.getCreditCardNumber());
        cv.put(COL_CREDIT_CARD_SECURITY_NUMBER , card.getSecurityCreditCardNumber());
        cv.put(COL_CREDIT_CARD_TYPE , card.getCreditCardType());

        return this.sqLiteDatabase.insert(TBL_CREDIT_CARD , null , cv) != - 1;
    }


    public boolean insertOrder(int userId , int clothingId ){

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);

        ContentValues cv = new ContentValues();
        cv.put(COL_USER_ID , userId);
        cv.put(COL_CLOTHING_ID , clothingId);
        cv.put(COL_PURCHASE_DATE , formattedDate);

        return this.sqLiteDatabase.insert(TBL_ORDER , null , cv) != - 1;
    }

    public Cursor getAllCreditCards(){

        return sqLiteDatabase.rawQuery("SELECT * FROM " + TBL_CREDIT_CARD , null);
    }

    public Cursor getAllOrders(){

        return sqLiteDatabase.rawQuery("SELECT * FROM " + TBL_ORDER , null);
    }

    public Cursor getAllManagers(){

        return sqLiteDatabase.rawQuery("SELECT * FROM " + TBL_MANAGER , null);
    }

    public Cursor getAllClothes(){

        return sqLiteDatabase.rawQuery("SELECT * FROM " + TBL_CLOTHING , null);
    }

    public Cursor getAllStores(){

        return sqLiteDatabase.rawQuery("SELECT * FROM " + TBL_STORE , null);
    }

}
