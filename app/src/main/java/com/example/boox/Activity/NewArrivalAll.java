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

import com.example.boox.Adapter.NewArrivalAdapter;
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

public class NewArrivalAll extends AppCompatActivity {

    List<com.example.boox.Model.BookModel.Result> NewArrivalList;
    RecyclerView rv_newarrival;
    NewArrivalAdapter newArrivalAdapter;

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
        setContentView(R.layout.newarrivalall);
        PrefManager.forceRTLIfSupported(getWindow(), NewArrivalAll.this);
        prefManager = new PrefManager(NewArrivalAll.this);

        progressDialog = new ProgressDialog(NewArrivalAll.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("" + getResources().getString(R.string.New_Arrival_Book));
        rv_newarrival = (RecyclerView) findViewById(R.id.rv_newarrival);

        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        NewArrival();



    }

    private void NewArrival() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<BookModel> call = bookNPlayAPI.newarriaval();
        call.enqueue(new Callback<BookModel>() {
            @Override
            public void onResponse(Call<BookModel> call, Response<BookModel> response) {
                if (response.code() == 200) {

                    NewArrivalList = new ArrayList<>();
                    NewArrivalList = response.body().getResult();
                    Log.e("NewArrivalList", "" + NewArrivalList.size());

                    newArrivalAdapter = new NewArrivalAdapter(NewArrivalAll.this, NewArrivalList, "ViewAll");
                    rv_newarrival.setHasFixedSize(true);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(NewArrivalAll.this, 3,
                            LinearLayoutManager.VERTICAL, false);
                    rv_newarrival.setLayoutManager(gridLayoutManager);
                    rv_newarrival.setItemAnimator(new DefaultItemAnimator());
                    rv_newarrival.setAdapter(newArrivalAdapter);
                    newArrivalAdapter.notifyDataSetChanged();
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
