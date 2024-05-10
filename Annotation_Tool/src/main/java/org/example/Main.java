package org.example;

import org.example.exceptions.PDFException;
import org.example.services.ParsingService;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        ParsingService ps = new ParsingService();

        //testing
        File file = new File("C:\\Users\\stefa\\Desktop\\testSP\\sample3.pdf");

        try {
            System.out.println(ps.parsePDF(file));
        } catch (PDFException e) {
            System.out.println("Invalid PDF File: " + e.getMessage());
        }

    }
}