package com.example.book.test;


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
    private static boolean needToRead = false;
    private static boolean needToSend = false;
    private static boolean sendIsCompleted = false;
    private static boolean readIsCompleted = false;
    private byte [] buffer;
    private int size;
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
                    GameActivity.connected = true;
                    break;
                }
                while (mClient.isConnected()){
                    if (needToSend){
                        outputStream.write(buffer);
                        outputStream.flush();
                        needToSend = false;
                        sendIsCompleted = true;
                    }
                    if (needToRead){
                        buffer = new byte [512];
                        size = inputStream.read(buffer);
                        needToRead = false;
                        readIsCompleted = true;
                    }
                }
                outputStream.close();
                inputStream.close();
            }
            catch (InterruptedException e) {}
            catch (IOException e) {}
        }

    }

    public synchronized boolean dataWrite(byte [] send){
        if(mClient.isConnected()){
            sendIsCompleted = false;
            buffer = new byte [512];
            buffer = send;
            needToSend = true;
            while(!sendIsCompleted){}
            return true;
        }
        else {
            System.out.println("Connection is closed.");
            return false;
        }
    }

    public synchronized int dataRead(byte [] read){
        if(mClient.isConnected()){
            readIsCompleted = false;
            needToRead = true;
            while(!readIsCompleted){}
            int len = size;
            for (int i = 0;i < len;++i){
                read[i]=buffer[i];
            }
            return len;
        }
        else{
            System.out.println("Connection is closed.");
            return -1;
        }
    }

}
