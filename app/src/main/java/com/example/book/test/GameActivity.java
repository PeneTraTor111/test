package com.example.book.test;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button sendButton = findViewById(R.id.send);
        Button receive = findViewById(R.id.receive);

        System.out.println("ffffffffffffffffffffffffff");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte [] send = {1,2,3,4,5};
                if (NetworkActivity.isClient) NetworkActivity.clientThread.dataWrite(send);
            }
        });
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte [] read = new byte[512];
                String msg = "9";
                if (NetworkActivity.isServer) read = NetworkActivity.serverThread.dataRead();
                msg=new String(read);
                System.out.println(msg);
            }
        });

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
