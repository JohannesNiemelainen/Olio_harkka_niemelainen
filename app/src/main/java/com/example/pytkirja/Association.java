package com.example.pytkirja;

import java.util.ArrayList;

public class Association {
    private static Association association = new Association();

    protected String name;
    protected String address;
    protected int boardSize;
    protected ArrayList<BoardMember> boardMembers = null;
    protected ArrayList<Meeting> meetings = null;

    public Association () {
        name = "";
        address = "";
        boardSize = 0;
        this.boardMembers = new ArrayList<BoardMember>();
        this.meetings = new ArrayList<Meeting>();


    }

    //SINGLETON principle
    public Association (String n, String a, int bS) {
        name = n;
        address = a;
        boardSize = bS;
        this.boardMembers = new ArrayList<BoardMember>();
        this.meetings = new ArrayList<Meeting>();
    }

    public static Association getInstance() {return association;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public String setInfo () {
        String s = ("Nimi: " + name + "\nOsoite: " + address +
                "\nHallituksen jäseniä: " + boardSize);
        return s;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    //OBSOLETE, USED IT AT SOME POINT BUT LEFT IT OUT AS
    //THE LAYOUT DID NOT LET ME USE IT
    public String readMembers(){
        String membersAll = "Hallituksen jäsenet:";
        for (int i = 0; i<boardSize; i++) {
            membersAll = membersAll + "\n" + (i + 1) + ". Nimi: " + boardMembers.get(i).getName() + "\tAsema: " + boardMembers.get(i).getStatus();
        }

        return membersAll;
    }
}
