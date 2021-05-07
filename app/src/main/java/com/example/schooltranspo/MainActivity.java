package com.example.schooltranspo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {
    LoginVerifier lv = new LoginVerifier();
    private int SPLASH_TIME_OUT = 3;
    UserData ud =new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv.LoginCheck(ud, this);
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();

    }

    private class LogoLauncher extends Thread{
        public void run(){
            try {
                sleep(1000 * SPLASH_TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            MainActivity.this.finish();
        }
    }
}