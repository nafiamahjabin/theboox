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

import com.example.boox.Adapter.PaidBookAdapter;
import com.example.boox.Model.FreeBookModel.FreeBookModel;
import com.example.boox.Model.FreeBookModel.Result;
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

public class Paidbookallview extends AppCompatActivity {

    List<Result> paidbookList;
    RecyclerView rv_paidbook;
    PaidBookAdapter paidBookAdapter;

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
        setContentView(R.layout.paidbookallview);
        PrefManager.forceRTLIfSupported(getWindow(), Paidbookallview.this);
        prefManager = new PrefManager(Paidbookallview.this);

        progressDialog = new ProgressDialog(Paidbookallview.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(""+getResources().getString(R.string.Paid_Book));
        rv_paidbook = (RecyclerView) findViewById(R.id.rv_paid_book);

        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        paid_all_books();

    }

    private void paid_all_books() {
        progressDialog.show();
        AppAPI appAPI = BaseURL.getVideoAPI();
        Call<FreeBookModel> call = appAPI.free_paid_booklist("1");
        call.enqueue(new Callback<FreeBookModel>() {
            @Override
            public void onResponse(Call<FreeBookModel> call, Response<FreeBookModel> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    Log.e("paid_book_data", "" + response);
                    Log.e("paid_book_data", "" + response.body().getResult());
                    paidbookList = new ArrayList<>();
                    paidbookList = response.body().getResult();
                    paidBookAdapter = new PaidBookAdapter(Paidbookallview.this, paidbookList, "Home");
                    rv_paidbook.setHasFixedSize(true);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(Paidbookallview.this, 3, LinearLayoutManager.VERTICAL, false);
                    rv_paidbook.setLayoutManager(gridLayoutManager);
                    rv_paidbook.setItemAnimator(new DefaultItemAnimator());
                    rv_paidbook.setAdapter(paidBookAdapter);
                    paidBookAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<FreeBookModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("free_book_error", "" + t.getMessage());
            }
        });
    }


}
