
package com.makcsv.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author Max Soloviov <makcsv@bigmir.net>
 */
public class FileHelper {
    
    public static String getFileContentAsString(File cssFile) throws Exception {
        
        BufferedReader br = new BufferedReader(new FileReader(cssFile));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }

        return sb.toString();
        
    }
    
    public static String getFileContentAsString(String filePath) throws Exception {
        
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }

        return sb.toString();
        
    }
    
}
