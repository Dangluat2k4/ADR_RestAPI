package com.example.ass_restadr.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass_restadr.Interface.CategoryInterface;
import com.example.ass_restadr.Interface.ProductInterface;
import com.example.ass_restadr.R;
import com.example.ass_restadr.db.CategoryDTO;
import com.example.ass_restadr.db.ProductDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final Context context;
    private final List<CategoryDTO> listCategorty;

    static String BASE_URL = "http://10.0.2.2:3000/";
    String TAG = "zzzzzzzzzzzz";
    EditText edtUDcategory;
    Button btnUDCTG;
    public CategoryAdapter(Context context, List<CategoryDTO> listCategorty) {
        this.context = context;
        this.listCategorty = listCategorty;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryDTO categoryDTO = listCategorty.get(position);
        holder.txtCate.setText("Cate :" +categoryDTO.getCateName());
        holder.btnUpdateCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogUD(categoryDTO);
            }
        });
        holder.btnDeleteCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteProduct(categoryDTO.get_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCategorty.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCate;
        Button btnDeleteCate,btnUpdateCate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCate = itemView.findViewById(R.id.txtCate);
            btnDeleteCate = itemView.findViewById(R.id.btnDeleteCate);
            btnUpdateCate = itemView.findViewById(R.id.btnUpdateCate);
        }
    }
    void updateCateGory(CategoryDTO categoryDTO) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CategoryInterface categoryInterface = retrofit.create(CategoryInterface.class);
        Call<CategoryDTO> objCall = categoryInterface.update_danh_sach(categoryDTO.get_id(), categoryDTO);
        objCall.enqueue(new Callback<CategoryDTO>() {
            @Override
            public void onResponse(Call<CategoryDTO> call, Response<CategoryDTO> response) {
                if (response.isSuccessful()) {
                    int index = listCategorty.indexOf(categoryDTO);
                    if (index != -1) {
                        listCategorty.set(index, response.body());
                        // Cập nhật giao diện người dùng sau khi cập nhật dữ liệu
                        notifyDataSetChanged(); // Đảm bảo bạn gọi đúng adapter.notifyDataSetChanged()
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryDTO> call, Throwable throwable) {
                Log.e("zzzzzzzzzzzzzz", "onFailure: loi" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

    public void openDiaLogUD(CategoryDTO categoryDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_update_category, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        // Ánh xạ
        edtUDcategory = view.findViewById(R.id.edtUDcategory);
        btnUDCTG = view.findViewById(R.id.btnUDCTG);

        // Gán dữ liệu
        edtUDcategory.setText(categoryDTO.getCateName());
        btnUDCTG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDTO.setCateName(edtUDcategory.getText().toString());
                listCategorty.clear();
                listCategorty.addAll(listCategorty);
                updateCateGory(categoryDTO);
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
        CategoryInterface categoryInterface  = retrofit.create(CategoryInterface.class);

        // goi phuong thuc xoa tu interface

        Call<Void> objCall = categoryInterface.deleteCategory(id);

        objCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    getCategory();
                    Toast.makeText(context, "Bạn đã xóa thành công", Toast.LENGTH_SHORT).show();
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
    public void getCategory() {
        // tạo coverter
        Gson gson = new GsonBuilder().setLenient().create();


        // Khởi tạo Retrofit Client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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
                    listCategorty.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    listCategorty.addAll(response.body());
                    notifyDataSetChanged();
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
}
