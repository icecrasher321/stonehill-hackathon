package com.mancj.example.custom;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import com.mancj.example.R;


import com.mancj.example.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnNext;
    Intent permissionIntent;
    Intent threshold_intent;
    Number currentIdNum;
    boolean button_check=false;
    boolean username=false;
    boolean password=false;
    boolean price_check=false;
    String username_text;
    String password_text;
    private static final String POST_PARAMS = "userName=Pankaj";
    int price_for_post=300;
    int nutrition_val=0;
    int age_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WelcomeActivity","hello from WelcomeActivity");
        // Checking for first time launch - before calling setContentView()
        //launchHomeScreen();
        //finish();


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        //btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);
        //btnPermission=findViewById(R.id.btn_permission);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        /*btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });*/

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current=getItem(0);
                int next = getItem(+1);
                if(current==0){
                    viewPager.setCurrentItem(next);
                }
                else if(current==1) {
                    if (username && password) {
                        viewPager.setCurrentItem(next);
                    }
                    else{
                        Snackbar snackbar_details = Snackbar
                                .make(findViewById(R.id.LayoutThreshold), "please enter your details", Snackbar.LENGTH_SHORT);
                        snackbar_details.show();
                    }
                }
                else if(current==2) {
                    if (price_check) {
                        new PostDetails().execute();
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    }
                    else {
                        Snackbar snackbar_preferences = Snackbar
                                .make(findViewById(R.id.LayoutThreshold), "upper limit for price must be greater than 300", Snackbar.LENGTH_SHORT);
                        snackbar_preferences.show();
                    }
                }

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                //btnSkip.setVisibility(View.GONE);
            }
            else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                //btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private View view;
        LinearLayout linearLayoutPermission;
        Button btnPermission;
        LinearLayout linearLayoutThreshold;
        LinearLayout linearLayoutPreferences;
        TextThumbSeekBar price;
        TextThumbSeekBar nutrition;
        TimePicker pickThreshold;
        int minutes;
        int threshold_time;
        Button set_threshold;
        TextInputEditText username_box;
        TextInputEditText password_box;
        NumberPicker age;


        int i=0;

        public MyViewPagerAdapter() {
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(layouts[position], container, false);

            if(position==1){
                linearLayoutThreshold=view.findViewById(R.id.LayoutThreshold);
                username_box=view.findViewById(R.id.username);
                password_box=view.findViewById(R.id.password);
                username_box.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if(s.length() != 0) {
                            username = true;
                            username_text=s.toString();
                        }
                        else{
                            username = false;
                        }
                    }
                });
                password_box.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if(s.length() != 0) {
                            password = true;
                            password_text=s.toString();
                        }
                        else{
                            password = false;
                        }
                    }
                });
            }

            if(position==2){
                linearLayoutPreferences=view.findViewById(R.id.LayoutPreferences);
                price=linearLayoutPreferences.findViewById(R.id.price);
                nutrition=linearLayoutPreferences.findViewById(R.id.nutrition);
                price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (progress >= 300) {
                            price_check = true;
                            price_for_post=progress;
                        }
                        else{
                            price_check = false;
                        }
                    }
                });
                nutrition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        nutrition_val=progress;
                    }
                });
            }
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
    private class PostDetails extends AsyncTask <Void, Void, String> {
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

                List<NameValuePair> parameters = new ArrayList<NameValuePair>(5);
                parameters.add(new BasicNameValuePair("username",username_text));
                parameters.add(new BasicNameValuePair("password", password_text));
                //parameters.add(new BasicNameValuePair("age",String.valueOf(age_value)));
                parameters.add(new BasicNameValuePair("nutrition",String.valueOf(nutrition_val)));
                parameters.add(new BasicNameValuePair("price", String.valueOf(price_for_post)));
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

