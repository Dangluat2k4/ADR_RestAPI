package com.example.ass_restadr.Interface;

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

public interface ProductInterface {
    @GET("products")
    Call<List<ProductDTO>> lay_danh_sach();

    @POST("add")
    Call<ProductDTO> them_danh_sach(@Body ProductDTO objProduct);

    @PUT("update/{id}")
    Call<ProductDTO> update_danh_sach(@Path("id")String id, @Body ProductDTO productDTO);

    @DELETE("delete/{id}")
    Call<Void> deleteProduct(@Path("id")String id);

    @GET("timKiem")
    Call<List<ProductDTO>> timKiem(@Query("keyword")String keyword);
}
