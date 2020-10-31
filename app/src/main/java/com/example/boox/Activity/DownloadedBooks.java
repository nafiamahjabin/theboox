package com.example.boox.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boox.Adapter.MyDownloadBooksAdapter;
import com.example.boox.Model.BookModel.BookModel;
import com.example.boox.Model.BookModel.Result;
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

public class DownloadedBooks extends AppCompatActivity {

    List<Result> MyDownloadBookList;
    RecyclerView rv_mydownloadbooks;
    MyDownloadBooksAdapter myDownloadBooksAdapter;

    PrefManager prefManager;
    ProgressDialog progressDialog;
    String a_id, a_name, a_image, a_bio;

    TextView toolbar_title, txt_back;
    LinearLayout ly_dataNotFound;

    private static final String PDF_DIRECTORY = "/Ebooks";
    List<String> myList;

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
        setContentView(R.layout.downloadedbooks);

        ly_dataNotFound = findViewById(R.id.ly_dataNotFound);
        prefManager = new PrefManager(DownloadedBooks.this);

        progressDialog = new ProgressDialog(DownloadedBooks.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("" + getResources().getString(R.string.my_downloaded_book));
        rv_mydownloadbooks = (RecyclerView) findViewById(R.id.rv_mydownloadbooks);

        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (prefManager.getLoginId().equalsIgnoreCase("0")) {
            startActivity(new Intent(DownloadedBooks.this, LoginActivity.class));
        } else {
            downloadlist();
        }




    }

    private void downloadlist() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<BookModel> call = bookNPlayAPI.alldownload("" + prefManager.getLoginId());
        call.enqueue(new Callback<BookModel>() {
            @Override
            public void onResponse(Call<BookModel> call, Response<BookModel> response) {
                if (response.code() == 200) {

                    MyDownloadBookList = new ArrayList<>();
                    MyDownloadBookList = response.body().getResult();
                    Log.e("MyDownloadBookList", "" + MyDownloadBookList.size());
                    if (MyDownloadBookList.size() > 0) {
                        myDownloadBooksAdapter = new MyDownloadBooksAdapter(DownloadedBooks.this, MyDownloadBookList, "ViewAll");
                        rv_mydownloadbooks.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(DownloadedBooks.this,
                                LinearLayoutManager.HORIZONTAL, false);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(DownloadedBooks.this, 3,
                                LinearLayoutManager.VERTICAL, false);
                        rv_mydownloadbooks.setLayoutManager(gridLayoutManager);
                        rv_mydownloadbooks.setItemAnimator(new DefaultItemAnimator());
                        rv_mydownloadbooks.setAdapter(myDownloadBooksAdapter);
                        myDownloadBooksAdapter.notifyDataSetChanged();
                    } else {
                        ly_dataNotFound.setVisibility(View.VISIBLE);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<BookModel> call, Throwable t) {
                ly_dataNotFound.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }

        });
    }

}
