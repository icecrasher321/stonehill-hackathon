package com.mancj.example.custom;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.mancj.example.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    public int i=1;
    public ArrayList<String> appName;
    Context ctx;
    String restaurant_choice;

    public void setNames(ArrayList<String> names) {
        this.appName=names;
    }

    @NonNull
    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommend_row, viewGroup, false);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //restaurant_choice=v.
                ctx.startActivity(new Intent(ctx, RestaurantActivity.class));
                return false;
            }
        });
        if(i%5==1) {
            v.setBackgroundColor(Color.parseColor("#6200EE"));
        }
        if(i%5==2) {
            v.setBackgroundColor(Color.parseColor("#3700B3"));
        }
        if(i%5==3) {
            v.setBackgroundColor(Color.parseColor("#03DAC5"));
        }
        if(i%5==4) {
            v.setBackgroundColor(Color.parseColor("#FF0266"));
        }
        if(i%5==0) {
            v.setBackgroundColor(Color.parseColor("#FFDE03"));
        }
        i++;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecommendAdapter.ViewHolder viewHolder, int position) {
        String name=appName.get(position);
        viewHolder.restaurantName.setText(name);
        Log.d("MainActivity","restaurant name:"+name);
    }

    @Override
    public int getItemCount() {
        return appName.size();
    }

    public RecommendAdapter(Context context) {
        ctx=context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName;

        public ViewHolder(View itemView) {
            super(itemView);
            restaurantName= itemView.findViewById(R.id.restaurant_name);
        }
    }
    private class PostDetails extends AsyncTask<Void, Void, String> {
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
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                //HttpGet httpGet = new HttpGet("http://192.168.113.208:8080/api/dish/");
                HttpPost httppost = new HttpPost("http://192.168.113.208:8080/api/user/");

                List<NameValuePair> parameters = new ArrayList<NameValuePair>(4);
                parameters.add(new BasicNameValuePair("username",restaurant_choice));
                httppost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));

                HttpResponse response = httpClient.execute(httppost);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    try (InputStream instream = entity.getContent()) {
                        Log.d("MainActivity", "the content of get");
                    }
                }
            }
            catch(Exception c){}
            return "hello";
        }


        @Override
        protected void onPostExecute(String results) {
            if (results!=null) {
                Log.d("MainActivity","the results are:"+results);
            }

        }
    }

}




