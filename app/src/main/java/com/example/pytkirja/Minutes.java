package com.example.pytkirja;

import java.io.Serializable;

public class Minutes extends Agenda implements Serializable {
    private int signerCount;
    private String docType;

    public Minutes () {
        signerCount = 0;
        docType = "Pöytäkirja";
    }

    public int getSignerCount() {
        return signerCount;
    }

    public void setSignerCount(int signerCount) {
        this.signerCount = signerCount;
    }



    @Override
    public String getDocType() {
        return docType;
    }

    @Override
    public void setDocType (String type) {
        this.docType = type;
    }

    //Here we will create the Full Minutes text to pass it on to FileWriter
    public String createMinutesText() {
        String fullMinutes = getDocType() + "\n" +
                getType() + "\n" +
                "Yhdistys: " + Association.getInstance().getName() + "\n" +
                "Osoite: " + Association.getInstance().getAddress() + "\n" +
                "Kokousaika: " + getDate() + " " + getTime() + "\n";

        int lenght = sections.size();
        for (int i = 0; i < lenght; i++) {
            fullMinutes = fullMinutes + "\n" + sections.get(i).getRunningNumber() + ". " +
                    sections.get(i).getIntroduction() + "\n" +
                    "Päätösehdotus: " + sections.get(i).getProposal() + "\n" +
                    "Päätös: " + sections.get(i).getDecision() + "\n";
        }
        System.out.print(fullMinutes);
        return fullMinutes;
    }

}
