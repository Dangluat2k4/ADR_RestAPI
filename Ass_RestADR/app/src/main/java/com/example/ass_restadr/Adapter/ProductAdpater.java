package com.example.ass_restadr.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass_restadr.Interface.CategoryInterface;
import com.example.ass_restadr.Interface.ProductInterface;
import com.example.ass_restadr.R;
import com.example.ass_restadr.add_cart;
import com.example.ass_restadr.cart;
import com.example.ass_restadr.db.CategoryDTO;
import com.example.ass_restadr.db.ProductDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductAdpater extends RecyclerView.Adapter<ProductAdpater.ViewHolder>{
    private final Context context;
    private final List<ProductDTO> listProduct;

    EditText edtUDNameP,edtUDMTP,edtUDPriceP,edtUDCateP;
    TextView txtUDCate;
    Button btnUDPrd;
    Spinner spnUDCate;
    String TAG ="zzzzzzzzzzzz";
    static String BASE_URL = "http://10.0.2.2:3000/api/";
    static String BASE_URL_CT = "http://10.0.2.2:3000/";

    public ProductAdpater(Context context, List<ProductDTO> productDTOS) {
        this.context = context;
        this.listProduct = productDTOS;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductDTO productDTO = listProduct.get(position);

        holder.txtPrdName.setText("Tên sản phẩm : "+productDTO.getProductName());
        holder.txtPrdDct.setText("Miêu tả :" +productDTO.getDescription());
        holder.txtPrdPrice.setText(Html.fromHtml("<font color=\"#FF0000\">Giá sản phẩm :</font>" + String.valueOf(productDTO.getPrice())));

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogUD(productDTO);
            }
        });
        holder.btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteProduct(productDTO.get_id());
                Toast.makeText(context, "click" + productDTO.get_id(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDatMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Bundle để đính kèm dữ liệu
                Bundle bundle = new Bundle();
                bundle.putString("productID", productDTO.get_id());
                bundle.putString("productName", productDTO.getProductName());
                bundle.putString("productDescription", productDTO.getDescription());
                bundle.putInt("productPrice", productDTO.getPrice());

                // Tạo Fragment mới và đặt dữ liệu cho nó
                cart fragment = new cart();
                fragment.setArguments(bundle);

                // Hiển thị Fragment mới
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, fragment);
                transaction.addToBackStack(null); // Nếu bạn muốn Fragment bị thêm vào BackStack
                transaction.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtPrdName, txtPrdDct, txtPrdPrice;
        Button btnDeleteProduct, btnUpdate,btnDatMua;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            txtPrdName = itemView.findViewById(R.id.txtPrdName);
            txtPrdDct = itemView.findViewById(R.id.txtPrdDct);
            txtPrdPrice = itemView.findViewById(R.id.txtPrdPrice);
            btnUpdate = itemView.findViewById(R.id.btnUpdatePRD);
            btnDeleteProduct = itemView.findViewById(R.id.btnDeleteProduct);
            btnDatMua = itemView.findViewById(R.id.btnDatMua);

        }
    }

    void updateProduct(ProductDTO prd) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ProductInterface productInterface = retrofit.create(ProductInterface.class);
        Call<ProductDTO> objCall = productInterface.update_danh_sach(prd.get_id(), prd);
        objCall.enqueue(new Callback<ProductDTO>() {
            @Override
            public void onResponse(Call<ProductDTO> call, Response<ProductDTO> response) {
                if (response.isSuccessful()) {
                    int index = listProduct.indexOf(prd);
                    if (index != -1) {
                        listProduct.set(index, response.body());
                        // Cập nhật giao diện người dùng sau khi cập nhật dữ liệu
                        notifyDataSetChanged(); // Đảm bảo bạn gọi đúng adapter.notifyDataSetChanged()
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductDTO> call, Throwable throwable) {
                Log.e("zzzzzzzzzzzzzz", "onFailure: loi" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

    public void openDiaLogUD(ProductDTO productDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_update_product, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        // Ánh xạ
        edtUDNameP = view.findViewById(R.id.edtUDNameP);
        edtUDMTP = view.findViewById(R.id.edtUDMTP);
        edtUDPriceP = view.findViewById(R.id.edtUDPriceP);
        btnUDPrd = view.findViewById(R.id.btnUDPrd);
        txtUDCate = view.findViewById(R.id.txtUDCate);
        spnUDCate =  view.findViewById(R.id.spnUDCate);
        loadCategories();
        spnUDCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Lấy ID sản phẩm được chọn
                String selectedProductId = (String) parentView.getItemAtPosition(position);

                // Lưu ID sản phẩm vào một text khác, ví dụ là một TextView
                txtUDCate.setText(selectedProductId); // Lưu ID sản phẩm vào TextView
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }
        });
        // Gán dữ liệu
        edtUDNameP.setText(productDTO.getProductName());
        edtUDMTP.setText(productDTO.getDescription());
        edtUDPriceP.setText(String.valueOf(productDTO.getPrice()));
        btnUDPrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDTO.setProductName(edtUDNameP.getText().toString());
                productDTO.setDescription(edtUDMTP.getText().toString());
                productDTO.setCateID(txtUDCate.getText().toString());
                productDTO.setPrice(Integer.parseInt(edtUDPriceP.getText().toString()));

                listProduct.clear();
                listProduct.addAll(listProduct);
                updateProduct(productDTO);
                Toast.makeText(context, "update thanh cong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void  DeleteProduct (String id){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductInterface productInterface = retrofit.create(ProductInterface.class);

        // goi phuong thuc xoa tu interface

        Call<Void> objCall = productInterface.deleteProduct(id);

        objCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    getProduct();
                }else {
                    Log.e(TAG,"xoa thanh cong " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.e(TAG, "Lỗi khi xóa: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
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
                    listProduct.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    listProduct.addAll(response.body());
                    notifyDataSetChanged();
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, cateNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnUDCate.setAdapter(adapter);
                } else {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
