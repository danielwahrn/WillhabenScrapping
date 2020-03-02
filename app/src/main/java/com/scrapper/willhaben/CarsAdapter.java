package com.scrapper.willhaben;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CarsAdapter extends BaseAdapter {
    Context context;
    JSONArray carsList;
    LayoutInflater inflter;
    int index = 0;

    public CarsAdapter(Context context, JSONArray list) {
        this.context = context;
        this.carsList = list;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return carsList.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.list_item, null);
        JSONObject child = null;
        try {
            child = carsList.getJSONObject(i);

            JSONObject item = child.getJSONObject("item");

            String name = item.getString("name");
            TextView tv_name = (TextView) view.findViewById(R.id.name);
            tv_name.setText(name);

            String description = item.getString("description");

            String url = item.getString("url");

            String image = item.getString("image");
            ImageView icon = (ImageView) view.findViewById(R.id.image);
            Picasso.get().load(image).into(icon);

            String model = item.getString("model");

            String id = item.getString("sku");
            TextView tv_id = view.findViewById(R.id.id);
            tv_id.setText(id);

            JSONObject offers = item.getJSONObject("offers");

            String price = offers.getString("price");
            TextView tv_price = (TextView) view.findViewById(R.id.price);
            tv_price.setText(price);

            String currency = offers.getString("priceCurrency");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}

