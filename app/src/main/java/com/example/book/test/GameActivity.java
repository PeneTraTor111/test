package com.example.book.test;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class GameActivity extends AppCompatActivity {
    public static boolean connected = false;
    private Server serverThread;
    private Client clientThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        connect();

        Button sendButton = findViewById(R.id.send);
        Button receive = findViewById(R.id.receive);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte [] send = {1,2,3,4,5};
                boolean tmp = false;
                if (NetworkActivity.isClient) tmp=clientThread.dataWrite(send);
            }
        });
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte [] read = new byte[512];
                String msg = "9";
                int len = 0;
                if (NetworkActivity.isServer) len = serverThread.dataRead(read);
                msg=new String(read,0,len);
                System.out.println(msg);
            }
        });

    }

    private void connect(){
        if(MainActivity.isOffline){
            return;
        }
        if(NetworkActivity.isServer){
            serverThread = new Server ();
            serverThread.start();
        }
        if (NetworkActivity.isClient){
            clientThread = new Client ();
            clientThread.start();
        }
        new Thread(new Runnable() {
            private Handler handler = new Handler();
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(connected){
                            Toast.makeText(GameActivity.this, "连接成功", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(GameActivity.this, "等待超时,请重新尝试建立服务器端", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    //Confirm to exit, jump back to mainactivity
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(GameActivity.this,"再按一次回到登录界面",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


}
