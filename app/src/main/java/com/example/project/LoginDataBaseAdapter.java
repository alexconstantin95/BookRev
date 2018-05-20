package com.example.proiect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class LoginDataBaseAdapter
{
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;

    static final String DATABASE_CREATE = "create table "+"LOGIN"+
            "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text); ";

    static final String DATABASE_CREATE2 = "create table "+"MOVIE"+
            "( " +"ID"+" integer primary key autoincrement,"+ "USERID  text, MOVIEID text); ";

    public  SQLiteDatabase db;

    private final Context context;

    private DataBaseHelper dbHelper;
    public  LoginDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  LoginDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public void insertEntry(String userName,String password)
    {
        ContentValues newValues = new ContentValues();

        newValues.put("USERNAME", userName);
        newValues.put("PASSWORD",password);

        db.insert("LOGIN", null, newValues);
    }

    public void insertMovie(String userid,String movieid)
    {
        ContentValues newValues = new ContentValues();

        newValues.put("USERID", userid);
        newValues.put("MOVIEID",movieid);

        db.insert("MOVIE", null, newValues);
    }

    public void removeMovie(String userid,String movieid)
    {
        db.execSQL("DELETE FROM MOVIE WHERE  USERID='"+ userid +"' AND MOVIEID= '" + movieid + "'");
    }

    public ArrayList<String> getMovies(String userid)
    {
        ArrayList<String> v=new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM MOVIE WHERE TRIM(USERID) = '"+userid.trim()+"'", null);
        if (cursor.moveToFirst()) {
            do
            {
                String movieid=cursor.getString(cursor.getColumnIndex("MOVIEID"));
                v.add(movieid);
                Log.e(movieid,"alex");
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return v;
    }

    public int deleteEntry(String UserName)
    {
        String where="USERNAME=?";
        int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
        return numberOFEntriesDeleted;
    }

    public String getSingleEntry(String userName)
    {
        Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1)
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }

    public String getEntryID(String userName)
    {
        Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1)
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String userid= cursor.getString(cursor.getColumnIndex("ID"));
        cursor.close();
        return userid;
    }

    public boolean verifySingleEntry(String userName)
    {
        Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()==1)
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public void  updateEntry(String userName,String password)
    {
        ContentValues updatedValues = new ContentValues();

        updatedValues.put("USERNAME", userName);
        updatedValues.put("PASSWORD",password);

        String where="USERNAME = ?";
        db.update("LOGIN",updatedValues, where, new String[]{userName});
    }
}