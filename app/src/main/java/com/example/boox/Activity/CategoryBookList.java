package com.example.boox.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
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

import com.example.boox.Adapter.CategoryBookAdapter;
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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryBookList extends AppCompatActivity {

    RecyclerView rv_booklist;
    List<Result> BookList;
    CategoryBookAdapter categoryBookAdapter;

    PrefManager prefManager;
    ProgressDialog progressDialog;
    String cat_id, cat_name, cat_image;

    TextView toolbar_title, txt_back;
    RoundedImageView iv_thumb;
    LinearLayout ly_no_data;
    TextView txt_no_record;

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
        setContentView(R.layout.categorybooklist);
        PrefManager.forceRTLIfSupported(getWindow(), CategoryBookList.this);
        prefManager = new PrefManager(CategoryBookList.this);

        progressDialog = new ProgressDialog(CategoryBookList.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);


        ly_no_data = (LinearLayout) findViewById(R.id.ly_no_data);
        txt_no_record = (TextView) findViewById(R.id.txt_no_record);

        rv_booklist = (RecyclerView) findViewById(R.id.rv_booklist);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cat_id = bundle.getString("cat_id");
            cat_name = bundle.getString("cat_name");
            cat_image = bundle.getString("cat_image");
            Log.e("cat_id", "" + cat_id);
            toolbar_title.setText("" + cat_name);
            books_by_category();
        }





    }

    private void books_by_category() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<BookModel> call = bookNPlayAPI.books_by_category(cat_id);
        call.enqueue(new Callback<BookModel>() {
            @Override
            public void onResponse(Call<BookModel> call, Response<BookModel> response) {
                if (response.code() == 200) {

                    BookList = new ArrayList<>();
                    BookList = response.body().getResult();
                    Log.e("BookList", "" + BookList.size());

                    if (BookList.size() > 0) {
                        categoryBookAdapter = new CategoryBookAdapter(CategoryBookList.this, BookList);
                        rv_booklist.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(CategoryBookList.this,
                                LinearLayoutManager.HORIZONTAL, false);
//                    rv_booklist.setLayoutManager(mLayoutManager3);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryBookList.this, 3, LinearLayoutManager.VERTICAL, false);
                        rv_booklist.setLayoutManager(gridLayoutManager);
                        rv_booklist.setItemAnimator(new DefaultItemAnimator());
                        rv_booklist.setAdapter(categoryBookAdapter);
                        categoryBookAdapter.notifyDataSetChanged();

                        ly_no_data.setVisibility(View.GONE);
                        rv_booklist.setVisibility(View.VISIBLE);

                    } else {
                        txt_no_record.setText("" + Html.fromHtml(response.body().getMessage()));
                        ly_no_data.setVisibility(View.VISIBLE);
                        rv_booklist.setVisibility(View.GONE);
                    }
                } else {
                    txt_no_record.setText("" + Html.fromHtml(response.body().getMessage()));
                    ly_no_data.setVisibility(View.VISIBLE);
                    rv_booklist.setVisibility(View.GONE);
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
