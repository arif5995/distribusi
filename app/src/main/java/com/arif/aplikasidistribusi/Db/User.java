package com.arif.aplikasidistribusi.Db;

/**
 * Created by Angga Kristiono on 15/06/2019.
 */

public class User {
    private String Fullname, Email, Password, Alamat, User;

    public User(){

    }

    public User(String fullname, String email, String password, String alamat, String user) {
        Fullname = fullname;
        Email = email;
        Password = password;
        Alamat = alamat;
        User = user;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }


}
