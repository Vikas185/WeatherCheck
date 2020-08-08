package com.example.weathercheck;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
    EditText cityname; TextView mausam;
    public void findweather(View view){

        Log.i("Weather",cityname.getText().toString());

        InputMethodManager mgr= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityname.getWindowToken(),0);
        try {
            String encodedcityname= URLEncoder.encode(cityname.getText().toString(),"UTF-8");
            Downloadtask task=new Downloadtask();
            task.execute("https://openweathermap.org/data/2.5/weather?q="+encodedcityname +"&appid=439d4b804bc8187953eb36d2a8c26a02");
        }

        catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            Toast.makeText(this,"could not find city",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityname=(EditText)findViewById(R.id.cityname);
        mausam=(TextView)findViewById(R.id.mausam);


    }

    @SuppressLint("StaticFieldLeak")
    public class Downloadtask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection httpURLConnection=null;

            try {
                url=new URL(urls[0]);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream in=httpURLConnection.getInputStream();
                InputStreamReader streamReader=new InputStreamReader(in);
                int data=streamReader.read();
                while(data!=-1){

                    char current=(char)data;
                    result=result+current;
                    data=streamReader.read();

                }return result;



            } catch (Exception e) {

                Toast.makeText(getApplicationContext(),"could not find city",Toast.LENGTH_LONG);

            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {String message="";
                JSONObject jsonObject=new JSONObject(result);
                String weatherinfo=jsonObject.getString("weather");
                Log.i("weather info ",weatherinfo);
                JSONArray jsonArray=new JSONArray(weatherinfo);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonpart=jsonArray.getJSONObject(i);
                    String main=""; String descp="";
                    main=jsonpart.getString("main");
                    descp=jsonpart.getString("description");
                    if(main!=""&& descp!=""){

                        message +=main+":" +descp;
                    }

                }
                if(message!=""){

                    mausam.setText(message);

                }else{

                    Toast.makeText(getApplicationContext(),"could not find city",Toast.LENGTH_LONG).show();
                }
            }

            catch (JSONException e) {

                Toast.makeText(getApplicationContext(),"could not find city",Toast.LENGTH_LONG).show();
            }


        }
    }
}