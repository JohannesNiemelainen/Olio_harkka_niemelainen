package com.example.pytkirja;

public class BoardMember {
    private String name;
    private String status;

    public BoardMember () {
        name = "";
        status = "";

    }

    public BoardMember (String n, String s) {
        name = n;
        status = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String info = this.name + " " + this.status;
        return info;
    }
}
