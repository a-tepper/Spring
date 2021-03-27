package org.example.dto;

public class User {
    private String id;
    private String usr;
    private String pwd;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", usr='" + usr + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
