package com.mancj.example.custom;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.mancj.example.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class RecommendActivity extends AppCompatActivity {
    RecyclerView rvRecommendation;
    JSONArray ARRAY;
    ArrayList restaurantNames=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        //new GetCurrent().execute();
        try {
            ARRAY = readJsonFromUrl("");
            for(int i=0;i<ARRAY.length();i++){
                restaurantNames.add(ARRAY.getJSONObject(i).get("name"));
            }
        }
        catch(Exception c){}
        if(getSharedPreferences("firstRun", MODE_PRIVATE).getBoolean("try_new", true)){
            restaurantNames.add("Mama Goto");
            restaurantNames.add("Mekong");
            restaurantNames.add("Yogisthaan");
            restaurantNames.add("Vasudev Adiga's");
            restaurantNames.add("Subway");
        }
        else {
            restaurantNames.add("Chianti");
            restaurantNames.add("Kobe");
            restaurantNames.add("Sol Bistro");
            restaurantNames.add("Toscano's");
            restaurantNames.add("Rajdhani");
        }
        rvRecommendation=findViewById(R.id.rvRecommendations);
        RecommendAdapter adapter = new RecommendAdapter(this);
        rvRecommendation.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
        rvRecommendation.setAdapter(adapter);
        adapter.setNames(restaurantNames);
        adapter.notifyDataSetChanged();
    }

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }
    private class GetCurrent extends AsyncTask<Void, Void, String> {
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n>0) {
                byte[] b = new byte[4096];
                n =  in.read(b);


                if (n>0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet("http://192.168.113.208:8080/api/user/");
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            }
            catch (Exception e) {
                return e.getLocalizedMessage();
            }
            return text;
        }

        protected void onPostExecute(String results) {
            if (results!=null) {
                Log.d("MainActivity","results:"+results);

            }
        }
    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
        }
        finally {
            is.close();
        }
    }
}
