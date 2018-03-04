package com.script.muhelp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.script.muhelp.ResultCode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by hongl on 2018/2/27.
 */

public class String2File {
    private static String filePath;

    public static void setFilePath(String filePath) {
        String2File.filePath = filePath;
    }

    public static void setImage(ImageView imageView,String path){
        File file = new File(filePath,path);
        if(file.exists()) {
            try {
                FileInputStream in = new FileInputStream(file);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] bytes = new byte[2048];
                int ch = in.read(bytes);
                while (ch != -1) {
                    out.write(bytes, 0, ch);
                    ch = in.read(bytes);
                }
                out.flush();
                bytes = out.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void toFile(File file, String data){
        if(data == null || "".equals(data)){
            return ;
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes("ISO-8859-1"));
            GZIPInputStream gzip = new GZIPInputStream(inputStream);
            byte[] buffer = new byte[2048];
            int ch = gzip.read(buffer);
            while(ch!=-1){
                outputStream.write(buffer,0,ch);
                ch = gzip.read(buffer);
            }
            buffer = outputStream.toByteArray();
            FileOutputStream out = new FileOutputStream(file);
            out.write(buffer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getString(byte[] data){
        String finalData=null;
        //压缩图片

        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bout);
            gzip.write(data);
            gzip.finish();
            gzip.close();
            finalData = bout.toString("ISO-8859-1");
            bout.close();
        } catch (Exception e) {
            return null;
        }

        return finalData;
    }
}
