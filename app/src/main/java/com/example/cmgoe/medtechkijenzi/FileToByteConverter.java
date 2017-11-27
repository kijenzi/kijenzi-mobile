package com.example.cmgoe.medtechkijenzi;

/**
 * Created by cmgoe on 11/24/2017.
 */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by cmgoe on 10/23/2017.
 */

public class FileToByteConverter {


    public static ArrayList<String> convertToLines(File fileToConvert){
        ArrayList<String> lines = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File(fileToConvert.getPath()));
            while (s.hasNextLine()){
                lines.add(s.nextLine());
            }
            s.close();
        } catch (Exception e){

        }

        return lines;
    }

    public static byte[] convertFile(File fileToConvert){
        Scanner scanner = null;
        ArrayList<String> response = null;

        try{
            scanner = new Scanner( new File(fileToConvert.getPath()), "ASCII" );
            response = new ArrayList<String>();
            String text = scanner.useDelimiter("\\A").next();
            response.add(text);


        } catch (Exception e){

        }scanner.close(); // Put this call in a finally block

        System.out.println("made it here");
        String responseText = "";

        for(int i = 0; i < response.size(); i++){

            responseText = responseText.concat(response.get(i));
        }

        //System.out.println(responseText);

        try{
            return responseText.getBytes(StandardCharsets.US_ASCII);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}