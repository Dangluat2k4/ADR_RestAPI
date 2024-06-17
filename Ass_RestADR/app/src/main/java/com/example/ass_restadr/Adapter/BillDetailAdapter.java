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

import com.example.ass_restadr.Interface.BillDetailInterface;
import com.example.ass_restadr.Interface.BillInterface;
import com.example.ass_restadr.R;
import com.example.ass_restadr.db.BillDTO;
import com.example.ass_restadr.db.BillDetailDTO;
import com.example.ass_restadr.db.thongKeDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.ViewHolder> {
    private final Context context;
    private final List<BillDetailDTO> list;
    static String BASE_URL = "http://10.0.2.2:3000/";
    String TAG = "zzzzzzzzzzzz";

    public BillDetailAdapter(Context context, List<BillDetailDTO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BillDetailDTO billDetailDTO = list.get(position);
        holder.txtSoLuong.setText("Số lượng  :" +billDetailDTO.getQuantity());
        holder.btnDeleteBDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteBillDetail(billDetailDTO.get_id());
                Toast.makeText(context, "xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public  class  ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSoLuong;
        Button btnDeleteBDT,btnUpdateBDT;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuong);
            btnDeleteBDT = itemView.findViewById(R.id.btnDeleteBDT);
            btnUpdateBDT = itemView.findViewById(R.id.btnUpdateBDT);
        }
    }

    public void DeleteBillDetail(String id) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BillDetailInterface billDetailInterface = retrofit.create(BillDetailInterface.class);

        // goi phuong thuc xoa tu interface

        Call<Void> objCall = billDetailInterface.deleteBill(id);

        objCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    getBillDetail();
                } else {
                    Log.e(TAG, "xoa thanh cong " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.e(TAG, "Lỗi khi xóa: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
    public void getBillDetail() {
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
                    list.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    list.addAll(response.body());
                    notifyDataSetChanged();
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
}
