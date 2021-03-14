package com.example.cashierapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
   private EditText editTextEmail;
   private EditText editTextPassword;
   private Button BtnLogin;
    private Context context;
    private ProgressDialog pDialog;
    private String finaldata;
    private TextView btn_new_account;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = Login.this;
        pDialog = new ProgressDialog(context);
        btn_new_account = findViewById(R.id.register);
        editTextEmail=(EditText) findViewById(R.id.username);
        editTextPassword=(EditText) findViewById(R.id.password);
        BtnLogin = (Button) findViewById((R.id.login));

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(Login.this,Register.class);
                startActivity(gotoregisterone);
            }
        });


    }



        private void login() {
            final  String email  = editTextEmail.getText().toString().trim();
            final String password = editTextPassword.getText().toString().trim();
            pDialog.setMessage("Login Process......");
            showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://cashier-api.000webhostapp.com/UserRegistration/login.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("response api", response );

                    try {
                        JSONObject datalogin = new JSONObject(response);
                        finaldata = datalogin.toString();
                        Log.v("code", datalogin.getString("code") );
                        if (datalogin.getString("code").contains("200") ) {

                            JSONArray jsonarray = new JSONArray(datalogin.getString("data"));
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String token = jsonobject.getString("id");
                                Log.v("token", token);
                                // simpan username (key) kepada local
                                SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", token);
                                editor.apply();
                            }


                            hideDialog();
                            gotoMainActivity();
                        } else {
                            hideDialog();
                            Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show();


                        }

                    } catch (JSONException e) {
                        hideDialog();
                        e.printStackTrace();
                    }

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error == null || error.networkResponse == null) {
                                return;
                            }

                            String body;
                            //get status code here
                            final String statusCode = String.valueOf(error.networkResponse.statusCode);
                            //get response body and parse with appropriate encoding
                            try {
                                body = new String(error.networkResponse.data,"UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                // exception
                            }
                        }
                    }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("email", email);
                    params.put("password", password);

                    return params;
                }
            };
            Volley.newRequestQueue(this).add(stringRequest);
        }
    private void gotoMainActivity() {

        Intent intent = new Intent(Login.this,MainActivity.class);
        intent.putExtra("data", finaldata.toString());
        startActivity(intent);
        finish();
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
