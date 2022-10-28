package com.nurtivillage.java.geonixApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class DownloadImageFromSpecifiedURL {
	public static void main(String[] args) throws IOException {
        
        //Specify URL from which file will be downloaded.
        URL url = new URL(
                     "https://lh4.googleusercontent.com/"
           + "cjh5t_mnSYuW_RnVI922M9kjxW8leJGp8nCwWdBjOQxz_RzNkuvVIHaxzDrZ-neDiFt71jyRKAQ4_"
           + "2DTiv6R_vlZX0uUBGxLWmDR6Wa2gqqPJhgF7UzfqwYQJLTnDH0fKPt7wUc");

        String downloadFileLocation = "C:\\Users\\anurag.pundir\\OneDrive - IHS Markit\\Documents\\download.jpg";
        
        //Read image from specified URL using InputStream
        InputStream is = url.openStream();
        
        //Write image to file using FileOutputStream
        OutputStream fos = new FileOutputStream(downloadFileLocation);

        int ch;
        while ((ch = is.read()) != -1) { //read till end of file
               fos.write(ch);
        }
        
        System.out.println("Image from specified URL has been downloaded at "
                     +downloadFileLocation);
        is.close();
        fos.close();
 }
}
