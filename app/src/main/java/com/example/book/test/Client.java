package com.example.book.test;

/**
 * Created by BOOK on 2018/5/2.
 */




import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class Client extends Thread{
    public Socket mClient;
    private String ip = NetworkActivity.inputIP;
    private boolean flag = true;
    private static BufferedInputStream inputStream;
    private static BufferedOutputStream outputStream;
    @Override
    public void run() {
        super.run();

        while (!Thread.currentThread().isInterrupted()) {
            try{
                mClient = new Socket();
                mClient.connect(new InetSocketAddress(ip, 8888), 10000); // hard-code server address
            }catch(Exception e){flag = false;System.out.println("Connect fail!");}

            try {
                Thread.sleep(1000);
                if (!flag){
                    continue;
                }
                byte[] buff = new byte[4];
                int len = 0;
                String msg;
                inputStream = new BufferedInputStream(mClient.getInputStream());
                outputStream = new BufferedOutputStream(mClient.getOutputStream());
                outputStream.write(1);
                outputStream.flush();
                /*Connecting*/
                while (!Thread.currentThread().isInterrupted()&&(len = inputStream.read(buff)) != -1){
                    if (!(len>0)){
                        continue;
                    }
                    NetworkActivity.connected = true;
                    break;
                }
                while (!Thread.currentThread().isInterrupted()){
                    /*if (needToSend && !needToRecieve) {
                        outputStream.write(dataTosend);
                        outputStream.flush();
                        hasCompleted = true;
                    }
                    if (!needToSend && needToRecieve){
                        byte [] get =new byte [512];
                        inputStream.read(get);
                        dataToRecieve = get;
                        hasCompleted = true;
                    }*/
                }
                outputStream.close();
                inputStream.close();
            }
            catch (InterruptedException e) {}
            catch (IOException e) {}
        }

    }

    public synchronized boolean dataWrite(byte [] send){
        if(!Thread.currentThread().isInterrupted()){
            try {
                outputStream.write(send);
                outputStream.flush();
                return true;
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            System.out.println("Connection is interrupted.");
            return false;
        }
    }

    @Nullable
    public synchronized byte [] dataRead(){
        if(!Thread.currentThread().isInterrupted()){
            try {
                byte [] get =new byte [512];
                inputStream.read(get);
                return get;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        else{
            System.out.println("Connection is interrupted.");
            return null;
        }
    }

}
