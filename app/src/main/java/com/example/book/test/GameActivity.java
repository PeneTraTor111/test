package com.example.book.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.*;


public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

       /* byte [] send = {1,2,3,4,5};
        byte [] read = null;
        if (NetworkActivity.isClient) NetworkActivity.clientThread.dataWrite(send);
        if (NetworkActivity.isServer) read = NetworkActivity.serverThread.dataRead();*/
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
