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

import com.example.boox.Adapter.FreebookAdapter;
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

public class FreeBookallview extends AppCompatActivity {

    List<Result> freebookList;
    RecyclerView rv_freebook;
    FreebookAdapter freebookAdapter;

    PrefManager prefManager;
    ProgressDialog progressDialog;
    String a_id, a_name, a_image, a_bio;

    TextView toolbar_title, txt_back;
    int number;

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

        setContentView(R.layout.freepaidallview);
        PrefManager.forceRTLIfSupported(getWindow(), FreeBookallview.this);
        prefManager = new PrefManager(FreeBookallview.this);

        progressDialog = new ProgressDialog(FreeBookallview.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("" + getResources().getString(R.string.Free_Book));
        rv_freebook = (RecyclerView) findViewById(R.id.rv_free_paid_book);

        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Free_all_books();


    }

    private void Free_all_books() {
        progressDialog.show();
        AppAPI appAPI = BaseURL.getVideoAPI();
        Call<FreeBookModel> call = appAPI.free_paid_booklist("0");
        call.enqueue(new Callback<FreeBookModel>() {
            @Override
            public void onResponse(Call<FreeBookModel> call, Response<FreeBookModel> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    Log.e("free_book_data", "" + response);
                    Log.e("free_book_data", "" + response.body().getResult());
                    freebookList = new ArrayList<>();
                    freebookList = response.body().getResult();
                    freebookAdapter = new FreebookAdapter(FreeBookallview.this, freebookList, "Home");
                    rv_freebook.setHasFixedSize(true);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(FreeBookallview.this, 3, LinearLayoutManager.VERTICAL, false);
                    rv_freebook.setLayoutManager(gridLayoutManager);
                    rv_freebook.setItemAnimator(new DefaultItemAnimator());
                    rv_freebook.setAdapter(freebookAdapter);
                    freebookAdapter.notifyDataSetChanged();
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
