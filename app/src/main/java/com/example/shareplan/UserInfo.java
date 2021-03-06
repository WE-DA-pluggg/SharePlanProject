package com.example.shareplan;

public class UserInfo {
    String Email; // 이메일
    String Id; // 아이디
    String Password; // 비밀번호
    String Name; // 이름
    String Stunum; // 학번
    String Address;
    Boolean Authority;
    String PhoneNumber;

    public UserInfo() { }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }


    public String getStunum() {
        return Stunum;
    }

    public void setStunum(String stunum) {
        Stunum = stunum;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getAuthority() {
        return Authority;
    }

    public void setAuthority(Boolean authority) {
        Authority = authority;
    }

    public String getAddress() { return Address; }

    public void setAddress(String address) { Address = address; }
}
