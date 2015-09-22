
package com.makcsv.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

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
    
    /**
     * Записывает текстовую строку в файл (если файл не существует - он будет создан,
     * если существует - будет перезаписан)
     * 
     * @param path Полный путь к файлу
     * @param content Строка для записи в файл
     * @throws java.lang.Exception
     */
    public static void writeFile(String path, String content) throws Exception {

        File file = new File(path);
        
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        out.write(content);
            
    }
    
    /**
     * Записывает текстовую строку в файл (если файл не существует - он будет создан,
     * если существует - будет перезаписан)
     * 
     * @param path Полный путь к файлу
     * @param content Поток данных для записи в файл
     * @throws java.lang.Exception
     */
    public static void writeFile(String path, InputStream content) throws Exception {
        
        File file = new File(path);
        
        OutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[content.available()];
        content.read(buffer);
        out.write(buffer);
        
    }
    
}
