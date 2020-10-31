package com.example.boox.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.boox.R;
import com.example.boox.Utility.PrefManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Privacypolicy extends AppCompatActivity {
    PrefManager prefManager;
    TextView txt_back, txt_privacy_policy;
    String Privacypolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            //switch_theme.setChecked(true);
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
            getSupportActionBar().hide();
        }
        setContentView(R.layout.privacypolicy);

        PrefManager.forceRTLIfSupported(getWindow(), Privacypolicy.this);
        prefManager = new PrefManager(Privacypolicy.this);

        txt_privacy_policy = findViewById(R.id.privacy_policy);
        txt_back = (TextView) findViewById(R.id.txt_back);

        Privacypolicy = prefManager.getValue("privacy_policy");
        Log.e("privacy_policy", "" + Privacypolicy);


        txt_privacy_policy.setText(Privacypolicy);

        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }


}
