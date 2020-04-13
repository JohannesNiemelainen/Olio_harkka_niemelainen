package com.example.pytkirja;

import java.io.Serializable;

public class Agenda extends Document implements Serializable {

    public Agenda () {
        docType = "Esityslista";
    }

    public void fillSection () {


    }


    public String createAgendaText() {
        String fullAgenda = getDocType() + "\n" +
                getType() + "\n" +
                "Yhdistys: " + Association.getInstance().getName() + "\n" +
                "Osoite: " + Association.getInstance().getAddress() + "\n" +
                "Kokousaika: " + getDate() + " " + getTime() + "\n";

        int lenght = sections.size();
        for (int i = 0; i < lenght; i++) {
            fullAgenda = fullAgenda + "\n" + sections.get(i).getRunningNumber() + ". " +
                    sections.get(i).getIntroduction() + "\n" +
                    "Päätösehdotus: " + sections.get(i).getProposal() + "\n";
        }
        System.out.print(fullAgenda);
        return fullAgenda;
    }



}
