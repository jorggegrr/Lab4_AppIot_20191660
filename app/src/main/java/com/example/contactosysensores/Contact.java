package com.example.contactosysensores;

public class Contact {
    private String photoUrl;
    private String name;
    private String gender;
    private String city;
    private String country;
    private String email;
    private String phone;


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Contact(String photoUrl, String name, String gender, String city, String country, String email, String phone) {
        this.photoUrl = photoUrl;
        this.name = name;
        this.gender = gender;
        this.city = city;
        this.country = country;
        this.email = email;
        this.phone = phone;
    }



}
