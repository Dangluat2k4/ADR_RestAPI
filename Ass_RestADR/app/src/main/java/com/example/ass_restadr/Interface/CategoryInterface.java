package com.example.ass_restadr.Interface;

import com.example.ass_restadr.db.CategoryDTO;
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

public interface CategoryInterface {
    @GET("apiCategory/category")
    Call<List<CategoryDTO>> lay_danh_sach();

    @POST("apiCategory/addCTG")
    Call<CategoryDTO> them_danh_sach(@Body CategoryDTO objCategory);

    @PUT("apiCategory/updateCTG/{id}")
    Call<CategoryDTO> update_danh_sach(@Path("id")String id, @Body CategoryDTO categoryDTO);

    @DELETE("apiCategory/deleteCTG/{id}")
    Call<Void> deleteCategory(@Path("id")String id);

    @GET("timKiem")
    Call<List<CategoryDTO>> timKiem(@Query("keyword")String keyword);
}
