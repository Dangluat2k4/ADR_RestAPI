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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ass_restadr.Adapter.AccountAdapter;
import com.example.ass_restadr.Interface.AccountInterface;
import com.example.ass_restadr.Interface.ProductInterface;
import com.example.ass_restadr.db.AccountDTO;
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


public class Person extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton fltAddProduct;
    List<AccountDTO> listAC;
    AccountAdapter accountAdapter;

    EditText edtAddPassAC,edtAddFullNameAC;
    Button btnAddAccount;
    static String BASE_URL = "http://10.0.2.2:3000/";

    String TAG = "zzzzzzzz";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        recyclerView = view.findViewById(R.id.rscAC);
        fltAddProduct = view.findViewById(R.id.fltAddAC);

        listAC = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        accountAdapter = new AccountAdapter(getContext(), listAC);
        recyclerView.setAdapter(accountAdapter);
        getAccount();
        fltAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogAdd();
            }
        });
        return  view;
    }

    public void getAccount() {
        // tạo coverter
        Gson gson = new GsonBuilder().setLenient().create();


        // Khởi tạo Retrofit Client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Tạo interface
        AccountInterface accountInterface = retrofit.create(AccountInterface.class);


        // ta đối tượng Call
        Call<List<AccountDTO>> objCall = accountInterface.lay_danh_sach();
        // thực hiện gọi hàm enqeue lấy dữ liệu
        objCall.enqueue(new Callback<List<AccountDTO>>() {
            @Override
            public void onResponse(Call<List<AccountDTO>> call, Response<List<AccountDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: lay du lieu thanh cong " + response.body().toString());
                    // cập nhật vào list và hiển th lên danh sach
                    listAC.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    listAC.addAll(response.body());
                    accountAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: khong lay duoc du lieu");
                }
            }


            @Override
            public void onFailure(Call<List<AccountDTO>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                throwable.printStackTrace();// in ra danh sách các file liên quan tới lõi
            }
        });
    }

    void addNewAccount(AccountDTO accountDTO) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        AccountInterface accountInterface = retrofit.create(AccountInterface.class);
        Call<AccountDTO> objCall = accountInterface.them_danh_sach(accountDTO);
        objCall.enqueue(new Callback<AccountDTO>() {
            @Override
            public void onResponse(Call<AccountDTO> call, Response<AccountDTO> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse : them moi thanh cong" + response.body().toString());
                    getAccount();
                } else {
                    Log.d("zzzzzz", "onRespone : khong them duoc du lieu");
                }
            }

            @Override
            public void onFailure(Call<AccountDTO> call, Throwable throwable) {
                Log.d("zzzzzzzz", "OnFailure : Loi" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
    public void openDiaLogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // tao layout
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_add_acc_count, null);
        // gan lay out len view
        builder.setView(view);

        // tao hoi thoai
        Dialog dialog = builder.create();
        dialog.show();
        // anh xa
        edtAddFullNameAC = view.findViewById(R.id.edtAddFullNameAC);
        edtAddPassAC = view.findViewById(R.id.edtAddPassAC);
        btnAddAccount = view.findViewById(R.id.btnAddAccount);
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtAddFullNameAC.getText().toString();
                String pass = edtAddPassAC.getText().toString();
                AccountDTO accountDTO = new AccountDTO();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "Vui lòng nhập vào tên ", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getContext(), "Vui lòng nhập vào mật khẩu", Toast.LENGTH_SHORT).show();
                }
                else {
                    accountDTO.setFullName(name);
                    accountDTO.setPassWord(pass);
                    addNewAccount(accountDTO);
                    Toast.makeText(getContext(), "Bạn đã thêm thành công ", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

        });
    }

}