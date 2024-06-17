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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ass_restadr.Adapter.BillAdapter;
import com.example.ass_restadr.Interface.BillInterface;
import com.example.ass_restadr.db.BillDTO;
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

public class bill extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton fltAddProduct;
    List<BillDTO> listBill;
    BillAdapter billAdapter;
    String TAG = "aaaaaaaaaaa";
    EditText edtAddDateB,edtFindBill;
    Spinner spnAddEmailB;
    TextView txtUDEM;
    Button btnAddBill;
        static String BASE_URL = "http://10.0.2.2:3000/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        recyclerView = view.findViewById(R.id.rscBill);
        fltAddProduct = view.findViewById(R.id.fltAddBill);
        edtFindBill = view.findViewById(R.id.edtFindBill);
        ImageButton btnFindBill = view.findViewById(R.id.btnTimKiem);
        listBill = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        billAdapter = new BillAdapter(getContext(),listBill);
        recyclerView.setAdapter(billAdapter);
        getBill();
        fltAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDiaLogAddBill();
            }
        });
        btnFindBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = edtFindBill.getText().toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    searchBill(keyword);
                } else {
                    Toast.makeText(getContext(), "bạn cần nhập vào từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void getBill() {
        // tạo coverter
        Gson gson = new GsonBuilder().setLenient().create();


        // Khởi tạo Retrofit Client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Tạo interface
        BillInterface billInterface = retrofit.create(BillInterface.class);


        // ta đối tượng Call
        Call<List<BillDTO>> objCall = billInterface.lay_danh_sach();
        // thực hiện gọi hàm enqeue lấy dữ liệu
        objCall.enqueue(new Callback<List<BillDTO>>() {
            @Override
            public void onResponse(Call<List<BillDTO>> call, Response<List<BillDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: lay du lieu thanh cong " + response.body().toString());
                    // cập nhật vào list và hiển th lên danh sach
                    listBill.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    listBill.addAll(response.body());
                    billAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: khong lay duoc du lieu");
                }
            }


            @Override
            public void onFailure(Call<List<BillDTO>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                throwable.printStackTrace();// in ra danh sách các file liên quan tới lõi
            }
        });
    }

    void addNewProducts(BillDTO billDTO) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        BillInterface billInterface = retrofit.create(BillInterface.class);
        Call<BillDTO> objCall = billInterface.them_danh_sach(billDTO);
        objCall.enqueue(new Callback<BillDTO>() {
            @Override
            public void onResponse(Call<BillDTO> call, Response<BillDTO> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse : them moi thanh cong" + response.body().toString());
                    getBill();
                } else {
                    Log.d("zzzzzz", "onRespone : khong them duoc du lieu");
                }
            }

            @Override
            public void onFailure(Call<BillDTO> call, Throwable throwable) {
                Log.d("zzzzzzzz", "OnFailure : Loi" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
    public void OpenDiaLogAddBill(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // tao layout
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_add_bill, null);
        // gan lay out len view
        builder.setView(view);

        // tao hoi thoai
        Dialog dialog = builder.create();
        dialog.show();
        // anh xa
        edtAddDateB = view.findViewById(R.id.edtAddDateB);
        spnAddEmailB = view.findViewById(R.id.spnAddEmailB);
        txtUDEM = view.findViewById(R.id.txtUDEM);
        btnAddBill = view.findViewById(R.id.btnAddBill);
        loadEmailBill();
        spnAddEmailB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Lấy ID sản phẩm được chọn
                String selectedProductId = (String) parentView.getItemAtPosition(position);

                // Lưu ID sản phẩm vào một text khác, ví dụ là một TextView
                txtUDEM.setText(selectedProductId); // Lưu ID sản phẩm vào TextView
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }
        });
        btnAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = edtAddDateB.getText().toString();
                String email = txtUDEM.getText().toString();
                BillDTO billDTO = new BillDTO();
                if (TextUtils.isEmpty(date)) {
                    Toast.makeText(getContext(), "Vui lòng nhập vào ngày thang", Toast.LENGTH_SHORT).show();
                }
                else {
                    billDTO.setDate(date);
                    billDTO.setEmail(email);
                    addNewProducts(billDTO);
                    dialog.dismiss();
                }
            }

        });
    }

    private void loadEmailBill() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BillInterface billInterface = retrofit.create(BillInterface.class);
        Call<List<BillDTO>> call = billInterface.lay_danh_sach();
        call.enqueue(new Callback<List<BillDTO>>() {
            @Override
            public void onResponse(Call<List<BillDTO>> call, Response<List<BillDTO>> response) {
                if (response.isSuccessful()) {
                    List<BillDTO> categories = response.body();

                    List<String> _id = new ArrayList<>();
                    for (BillDTO billDTO : categories) {
                        _id.add(billDTO.get_id());
                    }
                    // Hiển thị danh sách danh mục lên Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, _id);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnAddEmailB.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BillDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void searchBill(String keyword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BillInterface billInterface = retrofit.create(BillInterface.class);
        Call<List<BillDTO>> call = billInterface.timKiem(keyword);
        call.enqueue(new Callback<List<BillDTO>>() {
            @Override
            public void onResponse(Call<List<BillDTO>> call, Response<List<BillDTO>> response) {
                if (response.isSuccessful()) {
                    listBill.clear();
                    listBill.addAll(response.body());
                    billAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "khong tim thay ket qua tim kiem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BillDTO>> call, Throwable throwable) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }
}