package com.script.muhelp.util;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by hongl on 2018/2/21.
 */

public class HttpUtil {

    private static final String IP = "192.168.43.200";
    private static final String PORT = "8080";

    public static Map jsonRequset(String method, String path, Map map) {

        Map result = null;
        try {
            URL url = new URL("http://" + IP + ":" + PORT + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            // 设置维持长连接
            connection.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            connection.setRequestProperty("Charset", "UTF-8");
            //转换为字节数组
            String json = JSON.toJSONString(map);
            byte[] data = json.getBytes();
            // 设置文件长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(10000);
            connection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(json);
            writer.flush();
            writer.close();
            System.out.println(connection.getResponseCode());
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line;
            while((line = reader.readLine())!=null){
                buffer.append(line);
            }
            reader.close();
            connection.disconnect();
            result = JSONObject.parseObject(buffer.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Map uploadFile(String path, String fileName,byte[] file, Map<String, String> parameters){
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "---------------------------7e0dd540448";
        Map result = null;
        try{
            URL url = new URL("http://" + IP + ":" + PORT + path);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //发送post请求需要下面两行
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //设置请求参数
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            //获取请求内容输出流
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());

            //开始写表单格式内容
            //写参数
            Set<String> keys = parameters.keySet();
            for(String key : keys){
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; name=\"");
                ds.write(key.getBytes());
                ds.writeBytes("\"" + end);
                ds.writeBytes(end);
                ds.write(parameters.get(key).getBytes());
                ds.writeBytes(end);
            }
            //写文件
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\"file\"; " + "filename=\"");
            //防止中文乱码
            ds.write(fileName.getBytes());
            ds.writeBytes("\"" + end);
            ds.writeBytes("Content-Type: text/plain"  + end);
            ds.writeBytes(end);
            //根据路径读取文件
            ds.write(file);
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.writeBytes(end);
            ds.flush();
            try{
                //获取URL的响应
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                StringBuffer s = new StringBuffer();
                String temp = "";
                while((temp = reader.readLine()) != null){
                    s.append(temp);
                }
                result = JSONObject.parseObject(s.toString());
                reader.close();
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("No response get!!!");
            }
            ds.close();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Request failed!");
        }
        return result;
    }
}
