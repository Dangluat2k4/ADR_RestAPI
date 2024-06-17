package com.example.ass_restadr.Interface;

import com.example.ass_restadr.db.CartDTO;
import com.example.ass_restadr.db.ProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartInterface {
    @GET("apiCart/cart")
    Call<List<CartDTO>> lay_danh_sach();

    // them danh sach
    @POST("apiCart/addToCart")
    Call<CartDTO> them_danh_sach(@Body CartDTO objCart);

    @DELETE("apiCart/deleteCart/{id}")
    Call<Void> deleteCart(@Path("id")String id);

    @GET("apiCart/timKiem")
    Call<List<CartDTO>> timKiem(@Query("keyword")String keyword);
}
