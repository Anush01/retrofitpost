package com.example.anushmp.posttoimgur;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("data")
    @Expose
    private Data__1 data;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Data__1 getData() {
        return data;
    }

    public void setData(Data__1 data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
