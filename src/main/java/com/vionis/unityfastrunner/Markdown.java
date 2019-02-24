package com.vionis.unityfastrunner;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.util.ui.UIUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
                progressIndicator.setFraction(i / (double)files.length * 0.9);
                File file = files[i];

                if (!file.getName().split("\\.")[1].equals("dll") && !file.getName().split("\\.")[1].equals("pdb"))
                    continue;

                InputStream in = new FileInputStream(file);
                String outPath = targetLocation+"/"+file.getName();
                OutputStream out = new FileOutputStream(outPath);

                if (sameContent(file.toPath(), new File(outPath).toPath()))
                    continue;

                // Copy the bits from input stream to output stream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }
    }

    static boolean sameContent(Path file1, Path file2) throws IOException {
        final long size = Files.size(file1);
        if (size != Files.size(file2))
            return false;

        if (size < 4096)
            return Arrays.equals(Files.readAllBytes(file1), Files.readAllBytes(file2));

        try (InputStream is1 = Files.newInputStream(file1);
             InputStream is2 = Files.newInputStream(file2)) {
            // Compare byte-by-byte.
            // Note that this can be sped up drastically by reading large chunks
            // (e.g. 16 KBs) but care must be taken as InputStream.read(byte[])
            // does not neccessarily read a whole array!
            int data;
            while ((data = is1.read()) != -1)
                if (data != is2.read())
                    return false;
        }

        return true;
    }


}
