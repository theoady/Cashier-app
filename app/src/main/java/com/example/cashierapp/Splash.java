package com.example.cashierapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getToken();
    }


    public void getToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token= sharedPreferences.getString("token","");
        if(token.isEmpty()){
            // setting timer untuk 2 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // merubah activity ke activity lain
                    Intent gogetstarted = new Intent(Splash.this,Login.class);
                    startActivity(gogetstarted);
                    finish();
                }
            }, 2000); // 2000 ms = 2s
        }
        else {
            // setting timer untuk 2 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // merubah activity ke activity lain
                    Intent gogethome = new Intent(Splash.this,MainActivity.class);
                    startActivity(gogethome);
                    finish();
                }
            }, 2000); // 2000 ms = 2s
        }
    }

//    private void gotoMainActivity() {
//
//        Intent intent = new Intent(Splash.this,MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    private void gotoLogin() {
//
//        Intent intent = new Intent(Splash.this,Login.class);
//        startActivity(intent);
//        finish();
//    }
}