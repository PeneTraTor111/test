package com.example.book.test;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.Intent;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;

public class MainActivity extends AppCompatActivity {

    public static boolean connected = false;
    public static String IP;
    public static String inputIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv=findViewById(R.id.showYourIpAddress);
        tv.setText(IP=getIpAddress());
        tv.setGravity(Gravity.CENTER);

        EditText input=findViewById(R.id.ipInput);
        inputIP = input.getText().toString();
        /*
        int ipAddress;
        ipAddress = Integer.parseInt(input.getText().toString());
        inputIP= String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        */
        //connect((Button)findViewById(R.id.startButton));
        Button client = findViewById(R.id.startButton);
        Button server = findViewById(R.id.serverButton);

        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBeAServer();
            }
        });
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBeAClient();
            }
        });

        }
    /*
    connect as a server and jump to gameactivity
    */
    private void toBeAServer(){
        Server serverThread = new Server ();
        serverThread.start();
        Toast.makeText(MainActivity.this, "等待连接", Toast.LENGTH_SHORT).show();

        try {
            Thread.currentThread().sleep(5000);//阻断5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(connected){
            Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,GameActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this, "等待超时,请重新尝试建立服务器端", Toast.LENGTH_SHORT).show();
            serverThread.interrupt();
        }
    }
    /*
    connect as a client and jump to gameActivity
    */
    private void toBeAClient(){
        Client clientThread = new Client ();
        clientThread.start();
        Toast.makeText(MainActivity.this, "等待连接", Toast.LENGTH_SHORT).show();

        try {
            Thread.currentThread().sleep(500);//阻断0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(connected){
            //if succeed
            Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,GameActivity.class);
            startActivity(intent);
        }
        else {
            AlertDialog.Builder connenctFailDialog=new AlertDialog.Builder(MainActivity.this);
            connenctFailDialog.setMessage("Connect fail!");
            connenctFailDialog.setPositiveButton("try again",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            connenctFailDialog.show();
            clientThread.interrupt();
        }
    }
    //connect as a client and jump to gameactivity
    /*
    private void connect(final Button bt){
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText input=findViewById(R.id.ipInput);
                if((input.getText().toString()).equals("123")){
                    //if succeed
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,GameActivity.class);
                    startActivity(intent);
                }
                else {
                    //if fail
                    AlertDialog.Builder connenctFailDialog=new AlertDialog.Builder(MainActivity.this);
                    connenctFailDialog.setMessage("Connect fail!");
                    connenctFailDialog.setPositiveButton("try again",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    connenctFailDialog.show();
                    connect(bt);
                }

            }
        });
    }
    */
    /*Get IP*/
    private String getIpAddress(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        System.out.println("This is what wifimanager gets");
        System.out.println(ipAddress);
        String ipAddressFormatted = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return ipAddressFormatted;
    }
}

