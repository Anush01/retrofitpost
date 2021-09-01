package com.example.anushmp.posttoimgur;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {






    @Headers("Authorization: Client-ID 2f8628c4fded59c")
    @POST("3/image")
    @Multipart
    Call<Data> uploadimage(@Part MultipartBody.Part image);

    @Headers("Authorization: Client-ID 2f8628c4fded59c")
    @POST("3/upload")
    @Multipart
    Call<Data> uploadVideo(@Part MultipartBody.Part video);




}
