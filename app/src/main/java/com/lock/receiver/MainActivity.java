package com.lock.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import static android.R.attr.onClick;

public class MainActivity extends AppCompatActivity {
    private Button sendStaticBtn;
    private Button sendDynamicBtn;
    private Button sendSystemBtn;
    private Button lockBtn;
    final int CODE = 123;
    private static final String STATICACTION = "com.byread.static";
    private static final String DYNAMICACTION = "com.byread.dynamic";
    // USB设备连接
    private static final String SYSTEMACTION = Intent.ACTION_POWER_CONNECTED;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 锁屏状态设定
        //设置Lock状态
        SharedPreferences sharedPreferences=getSharedPreferences("lockSTATE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences("lockSTATE",MODE_PRIVATE).edit();
        editor.putBoolean("lockState",true);//true home 不可用

        editor.commit();
        //按键功能
        sendStaticBtn = (Button) findViewById(R.id.send_static);
        sendDynamicBtn = (Button) findViewById(R.id.send_dynamic);
        sendSystemBtn = (Button) findViewById(R.id.send_system);
        lockBtn=(Button)findViewById(R.id.lock_btn);
        sendStaticBtn.setOnClickListener(new MyOnClickListener());
        sendDynamicBtn.setOnClickListener(new MyOnClickListener());
        sendSystemBtn.setOnClickListener(new MyOnClickListener());
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("lockBtn","启动lockActivity");
                Intent intent =new Intent();
                intent.setClass(MainActivity.this,LockActivity.class);
                startActivity(intent);
            }
        });
    }
    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 显示Locked
            if(v.getId() == R.id.send_static){
                SharedPreferences sharedPreferences=getSharedPreferences("lockSTATE", Context.MODE_PRIVATE);

                boolean locked = sharedPreferences.getBoolean("lockState",false);

                Toast.makeText(MainActivity.this,locked+"" ,
                        Toast.LENGTH_SHORT).show();

                Log.e("MainActivity", "发送自定义静态注册广播消息");
                Intent intent = new Intent();
                intent.setAction(STATICACTION);
                intent.putExtra("msg", "接收静态注册广播成功！");
                sendBroadcast(intent);
            }
            // 发送自定义动态注册广播消息
            else if(v.getId() == R.id.send_dynamic){
                Log.e("MainActivity", "发送自定义动态注册广播消息");
                Intent intent = new Intent();
                intent.setAction(DYNAMICACTION);
                intent.putExtra("msg", "接收动态注册广播成功！");
                sendBroadcast(intent);
            }
            // 测试修改lockstate
            else if(v.getId() == R.id.send_system){
                SharedPreferences sharedPreferences=getSharedPreferences("lockSTATE", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = getSharedPreferences("lockSTATE",MODE_PRIVATE).edit();
                boolean locked = sharedPreferences.getBoolean("lockState",false);
                editor.putBoolean("lockState",!locked);//true home 不可用

                editor.commit();

             locked = sharedPreferences.getBoolean("lockState",false);
                Toast.makeText(MainActivity.this,locked+"" ,
                        Toast.LENGTH_SHORT).show();
                //第二个参数为默认值
                Log.i("获取的锁屏状态为",locked+"");
            }
        }
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Toast.makeText(MainActivity.this, "按下了back键", Toast.LENGTH_SHORT).show();

        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;

            case KeyEvent.KEYCODE_MENU:
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
