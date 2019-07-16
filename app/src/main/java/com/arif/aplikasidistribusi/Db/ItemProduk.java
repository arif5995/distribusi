package com.arif.aplikasidistribusi.Db;

/**
 * Created by Angga Kristiono on 17/06/2019.
 */

public class ItemProduk {
    private String Nama, Harga, Image, mKey;

    public ItemProduk(){

    }

    public ItemProduk(String nama, String harga, String image) {
        Nama = nama;
        Harga = harga;
        Image = image;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getHarga() {
        return Harga;
    }

    public void setHarga(String harga) {
        Harga = harga;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

}
