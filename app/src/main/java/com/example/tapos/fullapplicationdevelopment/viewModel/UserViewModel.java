package com.example.tapos.fullapplicationdevelopment.viewModel;

/**
 * Created by tapos on 9/26/17.
 */

public class UserViewModel {
    private String name;
    private String email;
    private String password;
    private String re_password;
    private String phoneNumber;

    public UserViewModel(String name, String email, String password, String re_password, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.re_password = re_password;
        this.phoneNumber = phoneNumber;
    }

    public UserViewModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRe_password() {
        return re_password;
    }

    public void setRe_password(String re_password) {
        this.re_password = re_password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
