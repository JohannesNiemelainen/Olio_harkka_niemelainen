package com.example.pytkirja;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;

import java.util.ArrayList;
import java.io.FileOutputStream;
import java.util.Date;

public abstract class Document extends Meeting {
    protected int sectionCount;
    protected String docType;
    protected ArrayList<Section> sections = null;


    public Document () {
        sectionCount = 0;
        docType = "";
        this.sections = new ArrayList<Section>();
    }


    public int getSectionCount() {
        return sectionCount;
    }

    public void setSectionCount(int sections) {
        this.sectionCount = sections;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    //NOT HAD TIME TO DO THESE, AND
    //ONE CAN SHARE A PDF IF ONE IS CREATED
    public void shareDocument () {
    //TODO create pdf sharing via email
    }

    //NOT HAD TIME TO DO THESE, AND
    //ONE CAN SHARE A PDF IF ONE IS CREATED
    public void printDocument () {
        //TODO create pdfPrinting via email
    }
}
