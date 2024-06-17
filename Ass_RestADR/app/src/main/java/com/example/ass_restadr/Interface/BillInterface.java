package com.example.ass_restadr.Interface;


import com.example.ass_restadr.db.BillDTO;
import com.example.ass_restadr.db.ProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BillInterface {
    @GET("apiBill/bill")
    Call<List<BillDTO>> lay_danh_sach();

    @POST("apiBill/addBill")
    Call<BillDTO> them_danh_sach(@Body BillDTO objBill);

    @PUT("apiBill/updateBill/{id}")
    Call<BillDTO> update_bill(@Path("id")String id, @Body BillDTO billDTO);

    @DELETE("apiBill/deleteBill/{id}")
    Call<Void> deleteBill(@Path("id")String id);
    @GET("apiBill/timKiem")
    Call<List<BillDTO>> timKiem(@Query("keyword")String keyword);
}
