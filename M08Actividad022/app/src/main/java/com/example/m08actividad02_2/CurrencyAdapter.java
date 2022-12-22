package com.example.m08actividad02_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrencyAdapter extends BaseAdapter {

    ArrayList<Currency> currencies;
    Context context;

    public CurrencyAdapter(ArrayList<Currency> currencies, Context context) {
        this.currencies = currencies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public Object getItem(int i) {
        return currencies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return currencies.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View currencyView = inflater.inflate(R.layout.item_currency, viewGroup, false);

        TextView currencyName = currencyView.findViewById(R.id.tvCurrencyName);
        TextView currencyRate = currencyView.findViewById(R.id.tvCurrencyRate);

        currencyName.setText(currencies.get(i).getName());
        currencyRate.setText(currencies.get(i).getRate().toString());

        return currencyView;
    }
}
