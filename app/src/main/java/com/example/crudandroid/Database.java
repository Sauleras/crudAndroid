package com.example.crudandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CrudAndroid.db";
    public static final String TABLE_NAME = "pessoasEntrevistadas_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NOME";
    public static final String COL_3 = "CPF";
    public static final String COL_4 = "TELEFONE";
    public static final String COL_5 = "SINTOMAS_DENGUE";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+
                TABLE_NAME +
                " ("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                ""+COL_2+" TEXT," +
                ""+COL_3+" TEXT," +
                ""+COL_4+" TEXT," +
                ""+COL_5+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String nome, String cpf, String telefone, String sintomas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nome);
        contentValues.put(COL_3, cpf);
        contentValues.put(COL_4, telefone);
        contentValues.put(COL_5, sintomas);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return res;
    }

    public Cursor getDataById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID=?", new String[]{String.valueOf(id)});
    }

    public boolean updateData(int id, String name, String cpf, String phone, String symptoms) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, cpf);
        contentValues.put(COL_4, phone);
        contentValues.put(COL_5, symptoms);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{String.valueOf(id)});
        return true;
    }

    public boolean deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{String.valueOf(id)}) > 0;
    }

}
