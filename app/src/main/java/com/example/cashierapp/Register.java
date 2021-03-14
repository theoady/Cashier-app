package com.example.cashierapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
   private EditText editTextEmail;
    private EditText editTextUsername;
   private EditText editTextPassword;
   private Button BtnRegister;
    private Context context;
    private ProgressDialog pDialog;
    private String finaldata;
    private TextView btnlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = Register.this;
        pDialog = new ProgressDialog(context);
        btnlogin = findViewById(R.id.login);
        editTextUsername=(EditText) findViewById(R.id.username);
        editTextEmail=(EditText) findViewById(R.id.email);
        editTextPassword=(EditText) findViewById(R.id.password);
        BtnRegister = (Button) findViewById((R.id.register));

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotologin = new Intent(Register.this,Login.class);
                startActivity(gotologin);
            }
        });
    }

        private void register() {
            final  String email  = editTextEmail.getText().toString().trim();
            final  String username  = editTextUsername.getText().toString().trim();
            final String password = editTextPassword.getText().toString().trim();
            pDialog.setMessage("Register Process......");
            showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://cashier-api.000webhostapp.com/UserRegistration/register.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("response api", response );

                    try {
                        JSONObject datalogin = new JSONObject(response);
                        finaldata = datalogin.toString();
                        Log.v("code", datalogin.getString("code") );
                        if (datalogin.getString("code").contains("200") ) {
                            hideDialog();
                            Toast.makeText(context, "Success Register ", Toast.LENGTH_LONG).show();
                            gotoLogin();
                        } else {
                            hideDialog();
                            Toast.makeText(context, "Ooops, try again", Toast.LENGTH_LONG).show();


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
                    params.put("username", username);
                    params.put("password", password);
                    params.put("email", email);


                    return params;
                }
            };
            Volley.newRequestQueue(this).add(stringRequest);
        }
    private void gotoLogin() {

        Intent intent = new Intent(Register.this,Login.class);
//        intent.putExtra("data", finaldata.toString());
        startActivity(intent);
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
