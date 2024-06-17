package com.example.ass_restadr.Adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass_restadr.BillDetail;
import com.example.ass_restadr.Interface.BillInterface;
import com.example.ass_restadr.Interface.ProductInterface;
import com.example.ass_restadr.R;
import com.example.ass_restadr.db.BillDTO;
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

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHlder> {
    private final Context context;
    private final List<BillDTO> dtoList;
    EditText edtUpdateB;
    TextView txtEmail;
    Spinner spnUDMailB;
    Button btnUDBill;
    static String BASE_URL = "http://10.0.2.2:3000/";
    String TAG = "zzzzzzzzzzzz";

    public BillAdapter(Context context, List<BillDTO> dtoList) {
        this.context = context;
        this.dtoList = dtoList;
    }

    @NonNull
    @Override
    public ViewHlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new ViewHlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHlder holder, int position) {
        BillDTO billDTO = dtoList.get(position);

        holder.txtDateB.setText("Ngày :"+billDTO.getDate());
        holder.txtMailB.setText("Email :"+billDTO.getEmail());
        holder.btnUpdateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogUD(billDTO);
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDeleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteBill(billDTO.get_id());
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
        holder.txtChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, new BillDetail());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dtoList.size();
    }

    public class ViewHlder extends RecyclerView.ViewHolder {
        TextView txtDateB, txtMailB, txtChiTiet;
        Button btnDeleteB, btnUpdateB;

        public ViewHlder(@NonNull View itemView) {
            super(itemView);
            txtDateB = itemView.findViewById(R.id.txtDateB);
            txtMailB = itemView.findViewById(R.id.txtMailB);
            txtChiTiet = itemView.findViewById(R.id.txtChiTiet);
            btnDeleteB = itemView.findViewById(R.id.btnDeleteB);
            btnUpdateB = itemView.findViewById(R.id.btnUpdateB);
        }
    }

    void updateBill(BillDTO dto) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        BillInterface billInterface = retrofit.create(BillInterface.class);
        Call<BillDTO> objCall = billInterface.update_bill(dto.get_id(), dto);
        objCall.enqueue(new Callback<BillDTO>() {
            @Override
            public void onResponse(Call<BillDTO> call, Response<BillDTO> response) {
                if (response.isSuccessful()) {
                    int index = dtoList.indexOf(dto);
                    if (index != -1) {
                        dtoList.set(index, response.body());
                        // Cập nhật giao diện người dùng sau khi cập nhật dữ liệu
                        notifyDataSetChanged(); // Đảm bảo bạn gọi đúng adapter.notifyDataSetChanged()
                    }
                }
            }

            @Override
            public void onFailure(Call<BillDTO> call, Throwable throwable) {
                Log.e("zzzzzzzzzzzzzz", "onFailure: loi" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }


    public void openDiaLogUD(BillDTO billDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.iitem_update_bill, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        // Ánh xạ
        edtUpdateB = view.findViewById(R.id.edtUpdateB);
        spnUDMailB = view.findViewById(R.id.spnUDMailB);
        txtEmail = view.findViewById(R.id.txtMail);
        loadEmailBill();
        spnUDMailB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Lấy ID sản phẩm được chọn
                String selectedProductId = (String) parentView.getItemAtPosition(position);

                // Lưu ID sản phẩm vào một text khác, ví dụ là một TextView
                txtEmail.setText(selectedProductId); // Lưu ID sản phẩm vào TextView
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }
        });
        btnUDBill = view.findViewById(R.id.btnUDBill);


        // Gán dữ liệu
        edtUpdateB.setText(billDTO.getDate());
        btnUDBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billDTO.setDate(edtUpdateB.getText().toString());
                billDTO.setEmail(txtEmail.getText().toString());

                dtoList.clear();
                dtoList.addAll(dtoList);
                updateBill(billDTO);
                Toast.makeText(context, "update thanh cong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, _id);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnUDMailB.setAdapter(adapter);
                } else {
                    Toast.makeText(context, "Không thể tải danh sách danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BillDTO>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void DeleteBill(String id) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BillInterface billInterface = retrofit.create(BillInterface.class);

        // goi phuong thuc xoa tu interface

        Call<Void> objCall = billInterface.deleteBill(id);

        objCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    getBill();
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
                    dtoList.clear();
//                    arrayAdapter =  new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                    lv_chitieu()
                    // Thêm dữ liệu mới vào danh sách
                    dtoList.addAll(response.body());
                    notifyDataSetChanged();
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
}
