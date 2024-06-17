package com.example.ass_restadr.Interface;

import com.example.ass_restadr.db.BillDetailDTO;
import com.example.ass_restadr.db.thongKeDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BillDetailInterface {
    @GET("apiBillDetail/billDetail")
    Call<List<BillDetailDTO>> lay_danh_sach();

    // them danh sach
    @POST("apiBillDetail/addBillDT")
    Call<BillDetailDTO> them_danh_sach(@Body BillDetailDTO objBillDT);

    @PUT("apiBillDetail/updateBillDT/{id}")
    Call<BillDetailDTO> update_billDT (@Path("id") String id , @Body BillDetailDTO billDetailDTO);

    @DELETE("apiBillDetail/deleteBillDT/{id}")
    Call<Void> deleteBill(@Path("id")String id);
    @GET("apiBillDetail/totalMoney")
    Call<thongKeDTO> thongKe();
}
