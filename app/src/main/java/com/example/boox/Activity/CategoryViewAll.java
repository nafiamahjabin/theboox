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

import com.example.boox.Adapter.CategoryAdapter;
import com.example.boox.Model.CategoryModel.CategoryModel;
import com.example.boox.Model.CategoryModel.Result;
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

public class CategoryViewAll extends AppCompatActivity {

    PrefManager prefManager;
    ProgressDialog progressDialog;

    List<Result> CategoryList;
    RecyclerView ry_category;
    CategoryAdapter categoryAdapter;
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
        setContentView(R.layout.categoryactivity);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("" + getResources().getString(R.string.Categories));
        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ry_category = (RecyclerView) findViewById(R.id.ry_category);

        prefManager = new PrefManager(CategoryViewAll.this);

        progressDialog = new ProgressDialog(CategoryViewAll.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        Get_Category();




    }

    private void Get_Category() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<CategoryModel> call = bookNPlayAPI.categorylist();
        call.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.code() == 200) {

                    CategoryList = new ArrayList<>();
                    CategoryList = response.body().getResult();
                    Log.e("CategoryList", "" + CategoryList.size());
                    categoryAdapter = new CategoryAdapter(CategoryViewAll.this, CategoryList, "ViewAll");
                    ry_category.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(CategoryViewAll.this,
                            LinearLayoutManager.HORIZONTAL, false);
//                    ry_category.setLayoutManager(mLayoutManager3);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryViewAll.this, 3, LinearLayoutManager.VERTICAL, false);
                    ry_category.setLayoutManager(gridLayoutManager);
                    ry_category.setItemAnimator(new DefaultItemAnimator());
                    ry_category.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

}

