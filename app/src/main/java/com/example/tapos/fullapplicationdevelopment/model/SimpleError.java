
package com.example.tapos.fullapplicationdevelopment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleError {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("code")
    @Expose
    private Integer code;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
