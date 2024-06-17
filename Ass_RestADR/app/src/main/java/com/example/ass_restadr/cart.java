package com.example.ass_restadr;

import android.os.Bundle;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ass_restadr.Adapter.CartAdapter;
import com.example.ass_restadr.Interface.CartInterface;
import com.example.ass_restadr.Interface.CategoryInterface;
import com.example.ass_restadr.Interface.ProductInterface;
import com.example.ass_restadr.db.CartDTO;
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

public class cart extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton fltAddCart;
    List<CartDTO> listCart;
    CartAdapter cartAdapter;
    Button btnAddCart;
    ImageButton btnFindCart;
    EditText edtFundCart;

    static String BASE_URL = "http://10.0.2.2:3000/";
    //TextView txtProductName,txtProductDescription,txtProductPrice;
    String TAG = "ggggggg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.rscCart);
        edtFundCart = view.findViewById(R.id.edtFindCart);
        btnFindCart = view.findViewById(R.id.btnFindCart);
        listCart = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(getActivity(), listCart);
        recyclerView.setAdapter(cartAdapter);
        getCart();


        Bundle bundle = getArguments();
        if (bundle != null) {

            String productID = bundle.getString("productID");
            String productName = bundle.getString("productName");
            String productDescription = bundle.getString("productDescription");
            int productPrice = bundle.getInt("productPrice", 0);

            // Hiển thị dữ liệu trong Fragment
            TextView txtProductId = view.findViewById(R.id.txtProductId);
            TextView txtProductName = view.findViewById(R.id.txtProductName);
            TextView txtProductDescription = view.findViewById(R.id.txtProductDescription);
            TextView txtProductPrice = view.findViewById(R.id.txtProductPrice);
            btnAddCart = view.findViewById(R.id.btnAddCart);


            txtProductId.setText("ID: " + productID);
            txtProductName.setText("Tên sản phẩm: " + productName);
            txtProductDescription.setText("Miêu tả: " + productDescription);
            txtProductPrice.setText(String.valueOf(productPrice));
            btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                    CartDTO cartDTO = new CartDTO();

                    cartDTO.setProductID(productID);
                    cartDTO.setName(productName);
                    cartDTO.setPrice(productPrice);
                    cartDTO.setDescription(productDescription);
                    addNewCart(cartDTO);
                }
            });
        }
        btnFindCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = edtFundCart.getText().toString().trim();
                if(!TextUtils.isEmpty(keyword)){
                    searchCart(keyword);
                }else {
                    Toast.makeText(getContext(), "bạn cần nhập vào từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    public void getCart() {
        // tạo coverter
        Gson gson = new GsonBuilder().setLenient().create();


        // Khởi tạo Retrofit Client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Tạo interface
        CartInterface cartInterface = retrofit.create(CartInterface.class);


        // ta đối tượng Call
        Call<List<CartDTO>> objCall = cartInterface.lay_danh_sach();
        // thực hiện gọi hàm enqeue lấy dữ liệu
        objCall.enqueue(new Callback<List<CartDTO>>() {
            @Override
            public void onResponse(Call<List<CartDTO>> call, Response<List<CartDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: lay du lieu thanh cong " + response.body().toString());
                    // cập nhật vào list và hiển th lên danh sach
                    listCart.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    listCart.addAll(response.body());
                    cartAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: khong lay duoc du lieu");
                }
            }


            @Override
            public void onFailure(Call<List<CartDTO>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                throwable.printStackTrace();// in ra danh sách các file liên quan tới lõi
            }
        });
    }

    void addNewCart(CartDTO cartDTO) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CartInterface cartInterface = retrofit.create(CartInterface.class);
        Call<CartDTO> objCall = cartInterface.them_danh_sach(cartDTO);
        objCall.enqueue(new Callback<CartDTO>() {
            @Override
            public void onResponse(Call<CartDTO> call, Response<CartDTO> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse : them moi thanh cong" + response.body().toString());
                    getCart();
                } else {
                    Log.d("zzzzzz", "onRespone : khong them duoc du lieu");
                }
            }

            @Override
            public void onFailure(Call<CartDTO> call, Throwable throwable) {
                Log.d("zzzzzzzz", "OnFailure : Loi" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
    void searchCart(String keyword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartInterface cartInterface = retrofit.create(CartInterface.class);
        Call<List<CartDTO>> call = cartInterface.timKiem(keyword);
        call.enqueue(new Callback<List<CartDTO>>() {
            @Override
            public void onResponse(Call<List<CartDTO>> call, Response<List<CartDTO>> response) {
                if (response.isSuccessful()) {
                    listCart.clear();
                    listCart.addAll(response.body());
                    cartAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "khong tim thay ket qua tim kiem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartDTO>> call, Throwable throwable) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }
}