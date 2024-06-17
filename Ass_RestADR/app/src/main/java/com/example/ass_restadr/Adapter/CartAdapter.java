package com.example.ass_restadr.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass_restadr.Interface.CartInterface;
import com.example.ass_restadr.Interface.CategoryInterface;
import com.example.ass_restadr.R;
import com.example.ass_restadr.db.CartDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final Context context;
    private final List<CartDTO> listCart;
    static String BASE_URL = "http://10.0.2.2:3000/";
    String TAG = "oooooooooooo";
    public CartAdapter(Context context, List<CartDTO> listCart) {
        this.context = context;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartDTO cartDTO = listCart.get(position);
        holder.txtName.setText("Tên sản phẩm :"+cartDTO.getName());
        holder.txtPrice.setText("Giá :"+(String.valueOf(cartDTO.getPrice())));
        holder.txtDEC.setText("Miêu tả :"+cartDTO.getDescription());
        holder.txtSoLuong.setText("Số lương :"+String.valueOf(cartDTO.getQuantity()));
        holder.btnDeleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCart(cartDTO.getId());
                Toast.makeText(context, "Bạn đã xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtPrice,txtDEC,txtSoLuong;
        Button btnDeleteCart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDEC = itemView.findViewById(R.id.txtDEC);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuong);
            btnDeleteCart = itemView.findViewById(R.id.btnDeleteCart);
        }
    }

    public void  DeleteCart (String id){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartInterface cartInterface  = retrofit.create(CartInterface.class);

        // goi phuong thuc xoa tu interface

        Call<Void> objCall = cartInterface.deleteCart(id);

        objCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    getCart();
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
                    notifyDataSetChanged();
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
}
