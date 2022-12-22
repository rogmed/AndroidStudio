package com.example.m08actividad02_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DataController {

    final private Context context;

    public DataController(Context context) {
        this.context = context;
    }

    public void wipeCurrencyData() {
        CurrencyData admin = new CurrencyData(context, "Administration", null, 1);

        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        baseDeDatos.execSQL("DELETE FROM CURRENCY");
    }

    public void updateCurrencyData(ArrayList<Currency> currencies) {
        CurrencyData admin = new CurrencyData(context, "Administration", null, 1);

        //Abrimos la base de datos
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();

        // Insertar en base de datos
        for (Currency currency : currencies) {
            ContentValues registry = new ContentValues();

            registry.put("id", currency.getId());
            registry.put("currency", currency.getName());
            registry.put("rate", currency.getRate());
            registry.put("date", currency.geDate());

            baseDeDatos.insert("currency", null, registry);
        }
    }

    public ArrayList<Currency> getCurrencyData() {
        ArrayList<Currency> data = new ArrayList<>();

        CurrencyData admin = new CurrencyData(context, "Administration", null, 1);

        //Abrimos la base de datos
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();

        String query = "SELECT * FROM currency ";
        Cursor c = baseDeDatos.rawQuery(query, null);

        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndexOrThrow("id"));
            String name = c.getString(c.getColumnIndexOrThrow("currency"));
            double rate = c.getDouble(c.getColumnIndexOrThrow("rate"));
            String date = c.getString(c.getColumnIndexOrThrow("date"));

            data.add(new Currency(id, name, rate, date));
        }

        c.close();

        return data;
    }
}
