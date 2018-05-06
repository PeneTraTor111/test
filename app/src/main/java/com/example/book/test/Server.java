package com.example.book.test;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    public ServerSocket mServer;
    public Socket mSocket = null;
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
    public void run(){

        try {
            mServer = new ServerSocket(8888);
        } catch (Exception e) {flag = false;System.out.println("ServerSocket Error!");}

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
                if (!flag)
                    continue;
                mSocket = mServer.accept();
                if (mSocket == null)
                    continue;
                byte[] buff = new byte[256];
                int len = 0;
                outputStream = new BufferedOutputStream(mSocket.getOutputStream());
                inputStream = new BufferedInputStream(mSocket.getInputStream());
                /*Connecting*/
                if (!GameActivity.connected){
                    while (!Thread.currentThread().isInterrupted()&&(len=inputStream.read(buff)) != -1) {
                        if (!(len>0)){
                            continue;
                        }
                        GameActivity.connected = true;
                        outputStream.write(1);
                        outputStream.flush();
                        break;
                    }
                }
                while (mSocket.isConnected()){
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
        if(mSocket.isConnected()){
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
        if(mSocket.isConnected()){
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
