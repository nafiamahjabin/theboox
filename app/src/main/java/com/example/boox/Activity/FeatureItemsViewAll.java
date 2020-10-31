package com.example.boox.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boox.Adapter.FeatureAdapter;
import com.example.boox.Model.BookModel.BookModel;
import com.example.boox.R;
import com.example.boox.Utility.PrefManager;
import com.example.boox.Webservice.AppAPI;
import com.example.boox.Webservice.BaseURL;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeatureItemsViewAll extends AppCompatActivity {

    List<com.example.boox.Model.BookModel.Result> FeatureList;
    RecyclerView rv_feature_item;
    FeatureAdapter featureAdapter;

    PrefManager prefManager;
    ProgressDialog progressDialog;
    String a_id, a_name, a_image, a_bio;

    TextView toolbar_title, txt_back;

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
        setContentView(R.layout.featureitemviewall);
        PrefManager.forceRTLIfSupported(getWindow(), FeatureItemsViewAll.this);
        prefManager = new PrefManager(FeatureItemsViewAll.this);

        progressDialog = new ProgressDialog(FeatureItemsViewAll.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("" + getResources().getString(R.string.Top_Reading_Book));
        rv_feature_item = (RecyclerView) findViewById(R.id.rv_feature_item);

        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FeatureItem();


    }

    private void FeatureItem() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<BookModel> call = bookNPlayAPI.feature_item();
        call.enqueue(new Callback<BookModel>() {
            @Override
            public void onResponse(Call<BookModel> call, Response<BookModel> response) {
                if (response.code() == 200) {

                    FeatureList = new ArrayList<>();
                    FeatureList = response.body().getResult();
                    Log.e("FeatureList", "" + FeatureList.size());

                    featureAdapter = new FeatureAdapter(FeatureItemsViewAll.this, FeatureList, "ViewAll");
                    rv_feature_item.setHasFixedSize(true);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(FeatureItemsViewAll.this, 3,
                            LinearLayoutManager.VERTICAL, false);
                    rv_feature_item.setLayoutManager(gridLayoutManager);
                    rv_feature_item.setItemAnimator(new DefaultItemAnimator());
                    rv_feature_item.setAdapter(featureAdapter);
                    featureAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<BookModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

}
