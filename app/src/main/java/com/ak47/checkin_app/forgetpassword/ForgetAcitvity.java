package com.ak47.checkin_app.forgetpassword;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.ak47.checkin_app.R;
import com.zhy.autolayout.AutoLayoutActivity;

public class ForgetAcitvity extends AutoLayoutActivity {

    private RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_forget_conver,new ForgetByPhoneFragment())
                    .commit();
        }
        //ui初始化
        rg = (RadioGroup) findViewById(R.id.rgResect);
        rg.setOnCheckedChangeListener(checkedChangeListener);
    }
    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rbPhone:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_forget_conver,new ForgetByPhoneFragment()).commit();
                    break;

                case R.id.rbEmail:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_forget_conver,new ForgetByEmailFragment()).commit();
                    break;
            }
        }
    };
}
