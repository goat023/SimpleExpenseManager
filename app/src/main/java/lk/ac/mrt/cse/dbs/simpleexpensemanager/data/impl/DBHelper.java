package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "bank.db";
    private static final int DATABASE_VERSION = 1;


    public static final String ACCOUNT_TABLE_NAME = "account";
    public static final String ACCOUNT_NO = "accountNo";
    public static final String ACCOUNT_BANKNAME = "bankName";
    public static final String ACCOUNT_HOLDERNAME = "accountHolderName";
    public static final String ACCOUNT_BALANCE = "balance";

    public static final String TRANSACTION_TABLE_NAME = "transaction_table";
    public static final String TRANSACTION_ID = "trans_id";
    public static final String TRANSACTION_ACCOUNT_NO = "accountNo";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_TYPE = "expenseType";
    public static final String TRANSACTION_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + ACCOUNT_TABLE_NAME +
                        "(" + ACCOUNT_NO + " TEXT PRIMARY KEY, " +
                        ACCOUNT_BANKNAME + " TEXT, " +
                        ACCOUNT_HOLDERNAME + " TEXT, " +
                        ACCOUNT_BALANCE + " REAL)"
        );

        db.execSQL(
                "CREATE TABLE " + TRANSACTION_TABLE_NAME +
                        "(" + TRANSACTION_ID + " TEXT PRIMARY KEY, "  + TRANSACTION_DATE + " TEXT, " +
                        TRANSACTION_AMOUNT + " REAL, " +
                        TRANSACTION_TYPE + " TEXT, " +
                        TRANSACTION_ACCOUNT_NO + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);
        onCreate(db);

    }

    public void insertAccount(String accNo, String name, String bank_name, double balance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ACCOUNT_NO, accNo );
        contentValues.put(ACCOUNT_BANKNAME, bank_name);
        contentValues.put(ACCOUNT_HOLDERNAME, name);
        contentValues.put(ACCOUNT_BALANCE, balance);

        db.insert(ACCOUNT_TABLE_NAME, null, contentValues);

    }

    public List<String> getAccountNumberList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT " + ACCOUNT_NO +" FROM " + ACCOUNT_TABLE_NAME, null );
        List<String> mArrayList = new ArrayList<String>();
        for(res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {

            mArrayList.add(res.getString(0));
        }

        return mArrayList;

    }

    public List<Account> getAccountList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + ACCOUNT_TABLE_NAME, null );
        List<Account> mArrayList = new ArrayList<>();
        for(res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            mArrayList.add(new Account(res.getString(res.getColumnIndex(ACCOUNT_NO)), res.getString(res.getColumnIndex(ACCOUNT_BANKNAME)), res.getString(res.getColumnIndex(ACCOUNT_HOLDERNAME)),res.getDouble(res.getColumnIndex(ACCOUNT_BALANCE))));

        }
        return mArrayList;
    }

    public Account getAccount(String accNo) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + ACCOUNT_NO +"= '" + accNo + "'", null );
        if (res != null)
            res.moveToFirst();

        Account ac = new Account(
                res.getString(res.getColumnIndex("accountNo")),
                res.getString(res.getColumnIndex("bankName")),
                res.getString(res.getColumnIndex("accountHolderName")),
                res.getDouble(res.getColumnIndex("balance")));


        return ac;
    }

    public void  removeAccount(String accNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "DELETE * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + ACCOUNT_NO +"= '" + accNo + "'", null );
    }

    public void  updateAccount(String accNo, Double balance) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "UPDATE " + ACCOUNT_TABLE_NAME + " SET " + ACCOUNT_BALANCE + "=" + balance +" WHERE " + ACCOUNT_NO +"= '" + accNo + "'", null );
    }

    public void insertTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();



        contentValues.put(TRANSACTION_ID, new Date().getTime());
        contentValues.put(TRANSACTION_DATE, transaction.getDate().getTime());
        contentValues.put(TRANSACTION_AMOUNT, transaction.getAmount());
        contentValues.put(TRANSACTION_TYPE, transaction.getExpenseType().toString());
        contentValues.put(TRANSACTION_ACCOUNT_NO, transaction.getAccountNo());

        db.insert(TRANSACTION_TABLE_NAME, null, contentValues);

    }

    public List<Transaction> getTransactionList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + TRANSACTION_TABLE_NAME, null );


        List<Transaction> mArrayList = new ArrayList<>();
        for(res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            mArrayList.add(new Transaction(new Date(res.getLong(res.getColumnIndex(TRANSACTION_DATE))), res.getString(res.getColumnIndex(TRANSACTION_ACCOUNT_NO)),ExpenseType.valueOf(res.getString(res.getColumnIndex(TRANSACTION_TYPE))),res.getDouble(res.getColumnIndex(TRANSACTION_AMOUNT))));
        }
        return mArrayList;
    }

    public List<Transaction> getLimitedTransactionList(int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + TRANSACTION_TABLE_NAME + " LIMIT "+ limit, null );


        List<Transaction> mArrayList = new ArrayList<>();
        for(res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            mArrayList.add(new Transaction(new Date(res.getLong(res.getColumnIndex(TRANSACTION_DATE))), res.getString(res.getColumnIndex(TRANSACTION_ACCOUNT_NO)),ExpenseType.valueOf(res.getString(res.getColumnIndex(TRANSACTION_TYPE))),res.getDouble(res.getColumnIndex(TRANSACTION_AMOUNT))));
        }
        return mArrayList;
    }


}
