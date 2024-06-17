package com.example.ass_restadr;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ass_restadr.Adapter.CategoryAdapter;
import com.example.ass_restadr.Interface.CategoryInterface;
import com.example.ass_restadr.Interface.ProductInterface;
import com.example.ass_restadr.db.CategoryDTO;
import com.example.ass_restadr.db.ProductDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class frm_category extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton fltAddCategory;
    List<CategoryDTO> listCT;
    CategoryAdapter categoryAdapter;
    EditText edtCate;
    Button btnAddCTG;
    static String BASE_URL_CT = "http://10.0.2.2:3000/";
    String TAG = "zzzzzzzz";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_category, container, false);
        recyclerView = view.findViewById(R.id.rscCTG);
        fltAddCategory = view.findViewById(R.id.fltAddCTG);

        listCT = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(getActivity(),listCT);
        recyclerView.setAdapter((categoryAdapter));
        getCategory();
        fltAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogAdd();
            }
        });

        return view;
    }


    public void getCategory() {
        // tạo coverter
        Gson gson = new GsonBuilder().setLenient().create();


        // Khởi tạo Retrofit Client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Tạo interface
        CategoryInterface categoryInterface = retrofit.create(CategoryInterface.class);


        // ta đối tượng Call
        Call<List<CategoryDTO>> objCall = categoryInterface.lay_danh_sach();
        // thực hiện gọi hàm enqeue lấy dữ liệu
        objCall.enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: lay du lieu thanh cong " + response.body().toString());
                    // cập nhật vào list và hiển th lên danh sach
                    listCT.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    listCT.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: khong lay duoc du lieu");
                }
            }


            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                throwable.printStackTrace();// in ra danh sách các file liên quan tới lõi
            }
        });
    }
    void addNewCategory(CategoryDTO categoryDTO) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CategoryInterface categoryInterface = retrofit.create(CategoryInterface.class);
        Call<CategoryDTO> objCall = categoryInterface.them_danh_sach(categoryDTO);
        objCall.enqueue(new Callback<CategoryDTO>() {
            @Override
            public void onResponse(Call<CategoryDTO> call, Response<CategoryDTO> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse : them moi thanh cong" + response.body().toString());
                    getCategory();
                } else {
                    Log.d("zzzzzz", "onRespone : khong them duoc du lieu");
                }
            }

            @Override
            public void onFailure(Call<CategoryDTO> call, Throwable throwable) {
                Log.d("zzzzzzzz", "OnFailure : Loi" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
    public void openDiaLogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // tao layout
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_add_category, null);
        // gan lay out len view
        builder.setView(view);

        // tao hoi thoai
        Dialog dialog = builder.create();
        dialog.show();
        // anh xa
        edtCate = view.findViewById(R.id.edtAddCTG);
        btnAddCTG = view.findViewById(R.id.btnAddCTG);
        btnAddCTG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cate = edtCate.getText().toString();
                CategoryDTO categoryDTO = new CategoryDTO();
                if (TextUtils.isEmpty(cate)) {
                    Toast.makeText(getContext(), "Vui lòng nhập vào cate", Toast.LENGTH_SHORT).show();
                }
                else {
                    categoryDTO.setCateName(cate);
                    addNewCategory(categoryDTO);
                    dialog.dismiss();
                }
            }

        });
    }

}