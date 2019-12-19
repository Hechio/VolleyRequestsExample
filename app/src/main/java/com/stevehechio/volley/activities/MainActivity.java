package com.stevehechio.volley.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stevehechio.volley.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {
    private TextView mText,mTextJson,txtName;
    private ImageView imageView;
    private String myUrl,imgUrl,myUrl1;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ImageRequest imageRequest;
    private JsonObjectRequest jsonObjectRequest;
    private JsonObjectRequest jsonObjectRequestWithArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViewIds();

        doRequest();
    }

    private void setViewIds(){
        mText = findViewById(R.id.txtServerResponse);
        imageView = findViewById(R.id.imageView1);
        mTextJson=findViewById(R.id.textJson);
        txtName=findViewById(R.id.textName);



    }

    //request a string from the url
    private void doRequest() {

        //instantiate cache
        Cache cache = new DiskBasedCache(getCacheDir(),1024 *1024);

        //Network use HttpUrlConnection as network client
        BasicNetwork network = new BasicNetwork(new HurlStack());
        //instantiate the request queue
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        myUrl = "https://api.myjson.com/bins/t0mto";
        myUrl1= "https://api.myjson.com/bins/13z82k";

        //string request

        stringRequest = new StringRequest(Request.Method.GET, myUrl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
        mText.append( response.substring(0,100));
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
           mText.setText(error.getMessage());

        }
    });

        //image request

        imgUrl ="https://images.hivisasa.com/1200/ly8QYto3N7hellobeautiful.jpg";
        imageRequest = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);


            }
        }, 200, 200, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error loading image",Toast.LENGTH_SHORT).show();
                    }
                });

        //jsonobject request

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               mTextJson.setText(response.toString());

                String name = response.optString("name");
                String email = response.optString("email");
                JSONObject object = response.optJSONObject("phone");
                String home = object.optString("home");
                long mobile = object.optLong("mobile");

                txtName.append("Name: "+name+"\nEmail: "+email+"\nHome phone: "+home+"\nMobile: "+mobile);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               mTextJson.setText(error.getMessage());

            }
        });
        jsonObjectRequestWithArray = new JsonObjectRequest(Request.Method.GET, myUrl1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("siblings");

                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("name");
                                int age = object.getInt("age");
                                String education = object.getString("education");

                                txtName.append("\n Name "+name+"\tAge "+age+"\tEducation "+education);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });



        requestQueue.add(jsonObjectRequestWithArray);
        requestQueue.add(jsonObjectRequest);
        requestQueue.add(imageRequest);
        requestQueue.add(stringRequest);
    }




}
