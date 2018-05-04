package com.example.book.test;

/**
 * Created by BOOK on 2018/5/2.
 */


import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.LinkedList;

public class Client extends Thread{
    public Socket mClient;
    private String ip = NetworkActivity.inputIP;
    private boolean flag = true;
    @Override
    public void run() {
        super.run();
        //System.out.println("Server is waiting");
        ByteArrayOutputStream byteArray ;
        BufferedInputStream inputStream;
        BufferedOutputStream outputStream;

        while (!Thread.currentThread().isInterrupted()) {
            try{
                mClient = new Socket();
                mClient.connect(new InetSocketAddress(ip, 8888), 10000); // hard-code server address
            }catch(Exception e){flag = false;System.out.println("Connect fail!");}

            try {
                Thread.sleep(1000);
                if (!flag){
                    mClient.close();
                    continue;
                }
                NetworkActivity.connected = true;
                byte[] buff = new byte[256];
                int len ;
                String msg;
                inputStream = new BufferedInputStream(mClient.getInputStream());
                outputStream = new BufferedOutputStream(mClient.getOutputStream());

                /*Connecting*/
                while (!Thread.currentThread().isInterrupted()&&(len = inputStream.read(buff)) != -1){
                    System.out.println("***");
                    msg = new String(buff, 0, len);
                }
                outputStream.close();
                inputStream.close();
            }
            catch (InterruptedException e) {}
            catch (IOException e) {}
        }
        /*try {
            while (!Thread.currentThread().isInterrupted()) {
                if (byteArray != null)
                    byteArray.reset();
                else
                    byteArray = new ByteArrayOutputStream();
                System.out.println("______________________________________________________________________ydf:wt 1");
                socket = new java.net.Socket();
                Thread.sleep(1000);

                String ip = MainActivity.IP;
                socket.connect(new InetSocketAddress(ip, 8888), 10000); // hard-code server address

                inputStream = new BufferedInputStream(socket.getInputStream());
                outputStream = new BufferedOutputStream(socket.getOutputStream());
                byte[] buff = new byte[256];
                byte[] tmp = null;
                int len = 0;
                String msg ;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
                if (byteArray != null) {
                    byteArray.close();
                }
            } catch (Exception e) {
            }
        }*/
    }

}
