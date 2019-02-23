package com.vionis.unityfastrunner;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.util.ui.UIUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class Markdown {

    public static boolean IsCorrectDirectory(String path){
        try{
            final File file = new File(path);
            return file.exists() && file.isDirectory();
        }catch (Exception e){
            return false;
        }
    }

    public static boolean IsCorrectFile(String path){
        try{
            final File file = new File(path);
            return file.exists() && file.isFile();
        }catch (Exception e){
            return false;
        }
    }

    public static String ConvertIS(InputStream inputStream, Charset charset) throws IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    public static void RunToCorrectThread(Runnable runnable){
        UIUtil.invokeLaterIfNeeded(runnable);
    }


    public static void CopyDllFiles(File sourceLocation , File targetLocation, ProgressIndicator progressIndicator)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            File[] files = sourceLocation.listFiles();

            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                if (!file.getName().split("\\.")[1].equals("dll") && !file.getName().split("\\.")[1].equals("pdb"))
                    continue;

                InputStream in = new FileInputStream(file);
                OutputStream out = new FileOutputStream(targetLocation+"/"+file.getName());

                // Copy the bits from input stream to output stream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                progressIndicator.setFraction(i / (double)files.length * 0.9);
            }
        }
    }


}
