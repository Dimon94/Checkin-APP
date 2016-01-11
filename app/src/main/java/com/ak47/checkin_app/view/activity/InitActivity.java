package com.ak47.checkin_app.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ak47.checkin_app.MainActivity;
import com.ak47.checkin_app.bean.MyUser;
import com.ak47.checkin_app.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        Bmob.initialize(this, "9d4fc7d5415a8eda63b164d99974602f");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startAty();
                finish();

            }
        }, 3000);//这里停留时间为1000=1s。
    }

    private void startAty() {
        /*判断有没有登陆
        * isLogin返回值：true：  已经登陆
        *               false: 没有登陆*/
        if (isLogin()) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
    private boolean isLogin(){

        MyUser myUser = BmobUser.getCurrentUser(InitActivity.this, MyUser.class);
        if(myUser != null){
            // 说明已经登录，允许用户使用应用
            Log.i("smile", "用户已经登陆，进入主界面。。。");
            return true;
        }else{
            //缓存用户对象为空时， 说明没有登陆，可打开用户登陆注册界面…
            Log.i("smile", "没有查询到缓存用户对象，留在登陆界面。。。");
            return false;
        }

    }
}

