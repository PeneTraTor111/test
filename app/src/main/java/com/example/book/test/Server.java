package com.example.book.test;

/**
 * Created by BOOK on 2018/5/2.
 */

import java.net.*;
import java.io.*;

public class Server extends Thread {
    public ServerSocket mServer;
    public Socket mSocket = null;
    private boolean flag = true;
    @Override
    public void run(){
        BufferedOutputStream outputStream;
        BufferedInputStream inputStream;

        try {
            mServer = new ServerSocket(8888);
        } catch (Exception e) {flag = false;System.out.println("ServerSocket Error!");}

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
                if (!flag)
                    continue;
                NetworkActivity.connected = true;
                mSocket = mServer.accept();
                if (mSocket == null)
                    continue;

                byte[] buff = new byte[256];
                int len;
                outputStream = new BufferedOutputStream(mSocket.getOutputStream());
                inputStream = new BufferedInputStream(mSocket.getInputStream());
                /*Connecting*/
                if (!NetworkActivity.connected){
                    while (!Thread.currentThread().isInterrupted()&&(len=inputStream.read(buff)) != -1) {
                        if (!(len>0)){
                            continue;
                        }
                        NetworkActivity.connected = true;
                        break;
                    }
                    outputStream.close();
                    inputStream.close();
                    continue;
                }
                else{
                    //transort data

                }
                outputStream.close();
                inputStream.close();
            }
            catch (InterruptedException e) {}
            catch (IOException e) {}
        }
    }
}
