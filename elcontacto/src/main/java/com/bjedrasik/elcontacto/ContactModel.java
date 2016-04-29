package com.bjedrasik.elcontacto;

public class ContactModel {
    public  String Id;
    public String Name;
    public String Surname;
    public String Phone;
    public String Email;
    public String PhotoURL;

    public ContactModel(String id, String name, String surname, String phone, String email, String photoUrl) {
        Id = id;
        Name = name;
        Surname = surname;
        Email = email;
        Phone = phone;
        PhotoURL = photoUrl;
    }

}
