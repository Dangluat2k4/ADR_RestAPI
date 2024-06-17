package com.example.ass_restadr;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.example.ass_restadr.Adapter.ProductAdpater;
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

public class frm_home extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton fltAddProduct;
    List<ProductDTO> listPrd;
    ProductAdpater productAdpater;
    static String BASE_URL = "http://10.0.2.2:3000/api/";
    static String BASE_URL_CT = "http://10.0.2.2:3000/";
    String TAG = "zzzzzzzz";

    EditText edtName, edtMTP, edtPriceP, edtTimKiemPrd;
    Button btnAddP;
    ImageButton btnFindPRD,btnCart;
    TextView txtCate;
    Spinner spnCate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_home, container, false);

        recyclerView = view.findViewById(R.id.rscProduct);
        fltAddProduct = view.findViewById(R.id.fltAddProduct);
        edtTimKiemPrd = view.findViewById(R.id.edtTimKiemPrd);
        btnFindPRD = view.findViewById(R.id.btnFindPRD);
        btnCart = view.findViewById(R.id.btnCart);
        listPrd = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        productAdpater = new ProductAdpater(getActivity(), listPrd);
        recyclerView.setAdapter(productAdpater);
        getProduct();
        fltAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogAdd();
            }
        });
        btnFindPRD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = edtTimKiemPrd.getText().toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    searchProduct(keyword);
                } else {
                    Toast.makeText(getContext(), "bạn cần nhập vào từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, new cart());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void getProduct() {
        // tạo coverter
        Gson gson = new GsonBuilder().setLenient().create();


        // Khởi tạo Retrofit Client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Tạo interface
        ProductInterface productInterface = retrofit.create(ProductInterface.class);


        // ta đối tượng Call
        Call<List<ProductDTO>> objCall = productInterface.lay_danh_sach();
        // thực hiện gọi hàm enqeue lấy dữ liệu
        objCall.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: lay du lieu thanh cong " + response.body().toString());
                    // cập nhật vào list và hiển th lên danh sach
                    listPrd.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    listPrd.addAll(response.body());
                    productAdpater.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: khong lay duoc du lieu");
                }
            }


            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                throwable.printStackTrace();// in ra danh sách các file liên quan tới lõi
            }
        });
    }


    void addNewProducts(ProductDTO productDTO) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ProductInterface productInterface = retrofit.create(ProductInterface.class);
        Call<ProductDTO> objCall = productInterface.them_danh_sach(productDTO);
        objCall.enqueue(new Callback<ProductDTO>() {
            @Override
            public void onResponse(Call<ProductDTO> call, Response<ProductDTO> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse : them moi thanh cong" + response.body().toString());
                    getProduct();
                } else {
                    Log.d("zzzzzz", "onRespone : khong them duoc du lieu");
                }
            }

            @Override
            public void onFailure(Call<ProductDTO> call, Throwable throwable) {
                Log.d("zzzzzzzz", "OnFailure : Loi" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

    public void openDiaLogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // tao layout
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_add_product, null);
        // gan lay out len view
        builder.setView(view);

        // tao hoi thoai
        Dialog dialog = builder.create();
        dialog.show();
        // anh xa
        edtName = view.findViewById(R.id.edtNameP);
        edtMTP = view.findViewById(R.id.edtMTP);
        edtPriceP = view.findViewById(R.id.edtPriceP);
        btnAddP = view.findViewById(R.id.btnAddNewPrd);
        txtCate = view.findViewById(R.id.txtCate);
        spnCate = view.findViewById(R.id.spnCate);
        loadCategories();
        spnCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Lấy ID sản phẩm được chọn
                String selectedProductId = (String) parentView.getItemAtPosition(position);

                // Lưu ID sản phẩm vào một text khác, ví dụ là một TextView
                txtCate.setText(selectedProductId); // Lưu ID sản phẩm vào TextView
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }
        });
        btnAddP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String mieuTa = edtMTP.getText().toString();
                String price = edtPriceP.getText().toString();
                String cate = txtCate.getText().toString();
                ProductDTO productDTO = new ProductDTO();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "Vui lòng nhập vào tên sản phẩm", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mieuTa)) {
                    Toast.makeText(getContext(), "Vui lòng nhập vào miêu tả sản phẩm", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(price)) {
                    Toast.makeText(getContext(), "Vui lòng nhập vào giá sản phẩm", Toast.LENGTH_SHORT).show();
                } else if (!isNumeric(price)) {
                    Toast.makeText(getContext(), "Giá sản phẩm phải là một số", Toast.LENGTH_SHORT).show();
                } else {
                    productDTO.setProductName(name);
                    productDTO.setDescription(mieuTa);
                    productDTO.setPrice(Integer.parseInt(price));
                    productDTO.setCateID(cate);
                    addNewProducts(productDTO);
                    dialog.dismiss();
                }
            }

        });
    }

    private void loadCategories() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CategoryInterface categoryInterface = retrofit.create(CategoryInterface.class);
        Call<List<CategoryDTO>> call = categoryInterface.lay_danh_sach();
        call.enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                if (response.isSuccessful()) {
                    List<CategoryDTO> categories = response.body();

                    List<String> cateNames = new ArrayList<>();
                    for (CategoryDTO categoryDTO : categories) {
                        cateNames.add(categoryDTO.getCateName());
                    }
                    // Hiển thị danh sách danh mục lên Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cateNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCate.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void searchProduct(String keyword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductInterface productInterface = retrofit.create(ProductInterface.class);
        Call<List<ProductDTO>> call = productInterface.timKiem(keyword);
        call.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if (response.isSuccessful()) {
                    listPrd.clear();
                    listPrd.addAll(response.body());
                    productAdpater.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "khong tim thay ket qua tim kiem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable throwable) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }


    /// update san pham
// Hàm kiểm tra xem một chuỗi có phải là số nguyên không
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