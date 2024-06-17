package com.example.ass_restadr.Interface;

import com.example.ass_restadr.db.AccountDTO;
import com.example.ass_restadr.db.BillDetailDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AccountInterface {
    @GET("apiAC/account")
    Call<List<AccountDTO>> lay_danh_sach();

    // them danh sach
    @POST("apiAC/addAC")
    Call<AccountDTO> them_danh_sach(@Body AccountDTO objAC);

    @PUT("apiAC/updateAC/{id}")
    Call<AccountDTO> update_Accout (@Path("id") String id , @Body AccountDTO accountDTO);

    @DELETE("apiAC/deleteAC/{id}")
    Call<Void> deleteAccount(@Path("id")String id);
}
