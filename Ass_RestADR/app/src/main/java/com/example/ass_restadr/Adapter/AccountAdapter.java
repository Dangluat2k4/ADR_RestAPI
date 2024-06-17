package com.example.ass_restadr.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass_restadr.Interface.AccountInterface;
import com.example.ass_restadr.Interface.BillInterface;
import com.example.ass_restadr.Interface.ProductInterface;
import com.example.ass_restadr.R;
import com.example.ass_restadr.db.AccountDTO;
import com.example.ass_restadr.db.ProductDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private final Context context;
    private final List<AccountDTO> listAccCount;

    EditText edtUDPassAC,edtUDFullNameAC;
    Button btnUpdateAccount;
    static String BASE_URL = "http://10.0.2.2:3000/";
    String TAG = "zzzzzzzzzzzz";

    public AccountAdapter(Context context, List<AccountDTO> listAccCount) {
        this.context = context;
        this.listAccCount = listAccCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccountDTO accountDTO = listAccCount.get(position);
        holder.txtPassword.setText(Html.fromHtml("<font color=\"#FF0000\">PassWord :</font>" + accountDTO.getPassWord()));
        holder.txtFullName.setText(Html.fromHtml("<font color=\"#FF0048FF\">Full Name :</font>" + accountDTO.getFullName()));
        holder.btnUpdateA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogUD(accountDTO);
            }
        });
        holder.btnDeleteA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccout(accountDTO.get_id());
            }
        });

    }
    @Override
    public int getItemCount() {
        return listAccCount.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPassword, txtFullName;
        Button btnUpdateA, btnDeleteA;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPassword = itemView.findViewById(R.id.txtPassword);;
            txtFullName = itemView.findViewById(R.id.txtFullName);;
            btnUpdateA = itemView.findViewById(R.id.btnUpdateA);;
            btnDeleteA = itemView.findViewById(R.id.btnDeteleA);;
        }
    }

    void updateProduct(AccountDTO accountDTO) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        AccountInterface accountInterface = retrofit.create(AccountInterface.class);
        Call<AccountDTO> objCall = accountInterface.update_Accout(accountDTO.get_id(),accountDTO);
        objCall.enqueue(new Callback<AccountDTO>() {
            @Override
            public void onResponse(Call<AccountDTO> call, Response<AccountDTO> response) {
                if (response.isSuccessful()) {
                    int index = listAccCount.indexOf(accountDTO);
                    if (index != -1) {
                        listAccCount.set(index, response.body());
                        // Cập nhật giao diện người dùng sau khi cập nhật dữ liệu
                        notifyDataSetChanged(); // Đảm bảo bạn gọi đúng adapter.notifyDataSetChanged()
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountDTO> call, Throwable throwable) {
                Log.e("zzzzzzzzzzzzzz", "onFailure: loi" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

    public void openDiaLogUD(AccountDTO accountDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_update_account, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        // Ánh xạ
        edtUDFullNameAC = view.findViewById(R.id.edtUDFullNameAC);
        edtUDPassAC = view.findViewById(R.id.edtUDPassAC);
        btnUpdateAccount = view.findViewById(R.id.btnUpdateAccount);
        // Gán dữ liệu
        edtUDFullNameAC.setText(accountDTO.getFullName());
        edtUDPassAC.setText(accountDTO.getPassWord());
        btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountDTO.setFullName(edtUDFullNameAC.getText().toString());
                accountDTO.setPassWord(edtUDPassAC.getText().toString());
                listAccCount.clear();
                listAccCount.addAll(listAccCount);
                updateProduct(accountDTO);
                Toast.makeText(context, "update thanh cong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void DeleteAccout(String id) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AccountInterface accountInterface = retrofit.create(AccountInterface.class);

        // goi phuong thuc xoa tu interface

        Call<Void> objCall = accountInterface.deleteAccount(id);

        objCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    getAccount();
                    Toast.makeText(context, "Bạn đã xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "xoa thanh that bai " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.e(TAG, "Lỗi khi xóa: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
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
                    listAccCount.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    listAccCount.addAll(response.body());
                    notifyDataSetChanged();
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
}
