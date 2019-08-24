package com.example.cegap;

public class Singeltondata {

int user_id,reciever_id;
String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private static Singeltondata sd=new Singeltondata();

    public int getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(int reciever_id) {
        this.reciever_id = reciever_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public static Singeltondata getInstance(){
        return sd;
    }


}
