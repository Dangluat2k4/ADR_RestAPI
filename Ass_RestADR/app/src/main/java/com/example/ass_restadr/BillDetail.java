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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ass_restadr.Adapter.BillDetailAdapter;
import com.example.ass_restadr.Interface.BillDetailInterface;
import com.example.ass_restadr.Interface.BillInterface;
import com.example.ass_restadr.Interface.ProductInterface;
import com.example.ass_restadr.db.BillDTO;
import com.example.ass_restadr.db.BillDetailDTO;
import com.example.ass_restadr.db.ProductDTO;
import com.example.ass_restadr.db.thongKeDTO;
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

public class BillDetail extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton fltAddBillDetail;
    List<BillDetailDTO> listBillDT;
    BillDetailAdapter detailAdapter;
    EditText edtAddDateB;
    Spinner spnAddBID,spnAddBProduct;
    Button btnAddBillDT;
    TextView txtSelectID,txtSelectBID,txtThongKe;
    String TAG = "ccccccc";
    static String BASE_URL = "http://10.0.2.2:3000/";
    static String BASE_URLPR = "http://10.0.2.2:3000/api/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_detail, container, false);

        recyclerView = view.findViewById(R.id.rscBillDT);
        fltAddBillDetail = view.findViewById(R.id.fltAddBillDT);
        txtThongKe = view.findViewById(R.id.txtThongKe);
        listBillDT = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        detailAdapter = new BillDetailAdapter(getContext(),listBillDT);
        recyclerView.setAdapter(detailAdapter);
        getBill();
        thongKe();
        fltAddBillDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDiaLogAddBillDT();
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
        BillDetailInterface billDetailInterface = retrofit.create(BillDetailInterface.class);


        // ta đối tượng Call
        Call<List<BillDetailDTO>> objCall = billDetailInterface.lay_danh_sach();
        // thực hiện gọi hàm enqeue lấy dữ liệu
        objCall.enqueue(new Callback<List<BillDetailDTO>>() {
            @Override
            public void onResponse(Call<List<BillDetailDTO>> call, Response<List<BillDetailDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: lay du lieu thanh cong " + response.body().toString());
                    // cập nhật vào list và hiển th lên danh sach
                    listBillDT.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    listBillDT.addAll(response.body());
                    detailAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: khong lay duoc du lieu");
                }
            }


            @Override
            public void onFailure(Call<List<BillDetailDTO>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                throwable.printStackTrace();// in ra danh sách các file liên quan tới lõi
            }
        });
    }

    void addNewBillDetail(BillDetailDTO billDetailDTO) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        BillDetailInterface billDetailInterface = retrofit.create(BillDetailInterface.class);
        Call<BillDetailDTO> objCall = billDetailInterface.them_danh_sach(billDetailDTO);
        objCall.enqueue(new Callback<BillDetailDTO>() {
            @Override
            public void onResponse(Call<BillDetailDTO> call, Response<BillDetailDTO> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse : them moi thanh cong" + response.body().toString());
                    getBill();
                } else {
                    Log.d("zzzzzz", "onRespone : khong them duoc du lieu");
                }
            }

            @Override
            public void onFailure(Call<BillDetailDTO> call, Throwable throwable) {
                Log.d("zzzzzzzz", "OnFailure : Loi" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

    public void OpenDiaLogAddBillDT(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // tao layout
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_add_bill_detail, null);
        // gan lay out len view
        builder.setView(view);

        // tao hoi thoai
        Dialog dialog = builder.create();
        dialog.show();
        // anh xa
        edtAddDateB = view.findViewById(R.id.edtAddDateBDT);
        spnAddBID = view.findViewById(R.id.spnAddBID);
        spnAddBProduct = view.findViewById(R.id.spnAddBPR);
        txtSelectID = view.findViewById(R.id.txtSelectID);
        txtSelectBID = view.findViewById(R.id.txtSelectBID);

        spnAddBID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Lấy ID sản phẩm được chọn
                String selectedProductId = (String) parentView.getItemAtPosition(position);

                // Lưu ID sản phẩm vào một text khác, ví dụ là một TextView
                txtSelectBID.setText(selectedProductId); // Lưu ID sản phẩm vào TextView
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }
        });

        spnAddBProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Lấy ID sản phẩm được chọn
                String selectedProductId = (String) parentView.getItemAtPosition(position);

                // Lưu ID sản phẩm vào một text khác, ví dụ là một TextView
             txtSelectID.setText(selectedProductId); // Lưu ID sản phẩm vào TextView
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }
        });


        btnAddBillDT = view.findViewById(R.id.btnAddBillDT);
        loadIDProduct();
        loadBillID();
        btnAddBillDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soluong = edtAddDateB.getText().toString();
                String bill = txtSelectBID.getText().toString();
                String product = txtSelectID.getText().toString();
                BillDetailDTO billDTO = new BillDetailDTO();
                if (TextUtils.isEmpty(soluong)) {
                    Toast.makeText(getContext(), "Vui lòng nhập vào số lượng", Toast.LENGTH_SHORT).show();
                }else if(!isNumeric(soluong)){
                    Toast.makeText(getContext(), "số lượng cần là số", Toast.LENGTH_SHORT).show();
                }
                else {
                    billDTO.setQuantity(Integer.parseInt(soluong));
                    billDTO.setBillID(bill);
                    billDTO.setProductID(product);
                    addNewBillDetail(billDTO);
                    dialog.dismiss();
                }
            }

        });
    }
    private void loadIDProduct() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URLPR)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductInterface productInterface = retrofit.create(ProductInterface.class);
        Call<List<ProductDTO>> call = productInterface.lay_danh_sach();
        call.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if (response.isSuccessful()) {
                    List<ProductDTO> productID = response.body();

                    List<String> _id = new ArrayList<>();
                    for (ProductDTO productDTO : productID) {
                        _id.add(productDTO.get_id());
                    }
                    // Hiển thị danh sách danh mục lên Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, _id);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnAddBProduct.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBillID() {
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
                    spnAddBID.setAdapter(adapter);
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


    void  thongKe(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BillDetailInterface billDetailInterface = retrofit.create(BillDetailInterface.class);
        Call<thongKeDTO> call  = billDetailInterface.thongKe();
        call.enqueue(new Callback<thongKeDTO>() {
            @Override
            public void onResponse(Call<thongKeDTO> call, Response<thongKeDTO> response) {
                if (response.isSuccessful()){
                    thongKeDTO thongKeDTO = response.body();
                    Toast.makeText(getContext(), "tong thu :" + thongKeDTO.getTongTien() , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "khong the thong ke", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<thongKeDTO> call, Throwable throwable) {
                Toast.makeText(getContext(), "Loi", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}