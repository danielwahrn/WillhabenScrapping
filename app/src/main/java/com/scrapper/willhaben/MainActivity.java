package com.scrapper.willhaben;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ListView carListView;
    FloatingActionButton fab;
    HashMap<String, Integer> occurrences = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        initLoadURL();

        initListener();

    }

    private void initComponents() {
        recyclerView = findViewById(R.id.wordList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        fab = findViewById(R.id.fab);

    }

    private void initLoadURL() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://www.willhaben.at/iad/gebrauchtwagen/auto/audi-gebrauchtwagen/a3/")
//                .baseUrl("https://www.willhaben.at/iad/gebrauchtwagen/auto/audi-gebrauchtwagen/")
                .baseUrl("https://www.willhaben.at/iad/gebrauchtwagen/auto/l/oldtimer/")
//                .baseUrl("https://www.willhaben.at/iad/gebrauchtwagen/auto/l/oldtimer?page=2/")
                .addConverterFactory(ScalarsConverterFactory.create())
//                .baseUrl("https://www.journaldev.com/")
                .client(okHttpClient).build();


        final ApiService apiService = retrofit.create(ApiService.class);


        Call<String> stringCall = apiService.getStringResponse();
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String responseString = response.body();
                    Document doc = Jsoup.parse(responseString);
                    Element script = doc.getElementsByAttributeValue("type", "application/ld+json").first();
                    for (DataNode node : script.dataNodes()) {
//                        Log.d("scriptsdata", node.getWholeData());
                        String data = node.getWholeData();
                        try {
                            JSONObject obj = new JSONObject(data);
                            JSONArray json  = obj.getJSONArray("itemListElement");
                            carListView = findViewById(R.id.carlist);
                            CarsAdapter adapter = new CarsAdapter(getApplicationContext(), json);
                            carListView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void initListener() {

    }

}