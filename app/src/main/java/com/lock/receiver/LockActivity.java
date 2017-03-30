package com.lock.receiver;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lock.receiver.view.view.GestureLockViewGroup;
import com.lock.receiver.view.view.GestureLockViewGroup.OnGestureLockViewListener;
import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static com.lock.receiver.R.layout.lock_screen;

/**
 * Created by 奕旸 on 2017/3/21.
 */

public class LockActivity extends Activity {
    private Button exitBtn;
    private GestureLockViewGroup mGestureLockViewGroup;
    private EditText nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen);
        SharedPreferences pref = LockActivity.this.getSharedPreferences("lockSTATE", Context.MODE_PRIVATE);
        boolean locked = pref.getBoolean("lockState",false);
        SharedPreferences.Editor editor = pref.edit();
        //第二个参数为默认值
        Log.i("1.获取的锁屏状态为",locked+"");

//        if(!locked){
//            finish();
//
//        }
        //解锁操作

        mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
        mGestureLockViewGroup.setAnswer(new int[] { 1, 2 });
        mGestureLockViewGroup
                .setOnGestureLockViewListener(new OnGestureLockViewListener()
                {

                    @Override
                    public void onUnmatchedExceedBoundary()
                    {
                        Toast.makeText(LockActivity.this, "错误5次...",
                                Toast.LENGTH_SHORT).show();
                        mGestureLockViewGroup.setUnMatchExceedBoundary(5);
                    }

                    @Override
                    public void onGestureEvent(boolean matched)
                    {
                        SharedPreferences pref = LockActivity.this.getSharedPreferences("lockSTATE", Context.MODE_PRIVATE);
                        boolean locked = pref.getBoolean("lockState",false);

                        if(matched){
                            setLock();

                            finish();

                        }else{
                            Toast.makeText(LockActivity.this,"false" ,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onBlockSelected(int cId)
                    {
                    }
                });

        //查看进入该activity的方式
        List<String> pkgNamesT = new ArrayList<String>();
        List<String> actNamesT = new ArrayList<String>();
        Intent intent = getIntent();
        Context context =getApplicationContext();
        List<ResolveInfo>resolveInfos=context.getPackageManager().queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
        Log.i("resolveInfos="," "+resolveInfos.size());
        for (int i = 0; i < resolveInfos.size(); i++) {
            String string = resolveInfos.get(i).activityInfo.packageName;
            Log.i("获取的STRING名字=",string);
            if (!string.equals(context.getPackageName())) {//自己的launcher不要
                Log.i("获取的launcher", resolveInfos.get(i).activityInfo.packageName);
                pkgNamesT.add(string);
                string = resolveInfos.get(i).activityInfo.name;
                actNamesT.add(string);
            }
        }
        Log.e("启动的方法",getIntent().getCategories()+"");
        //android.intent.category.LAUNCHER：点击图标启动
        //android.intent.category.HOME：点击Home启动
//        Set catg = getIntent().getCategories();
//        if (catg!=null){
//            Log.e("catg=",catg+"");
//            finish();
//        }




        ActivityManager manager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        name.equals(LockActivity.class.getName());
        Log.e("  true在栈顶false不在栈顶",name.equals(LockActivity.class.getName())+"");



     exitBtn = (Button) findViewById(R.id.exit);
     exitBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
     });


        }

    void setLock(){
        SharedPreferences pref = LockActivity.this.getSharedPreferences("lockSTATE", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("lockState",false);

        editor.commit();
        boolean locked = pref.getBoolean("lockState",false);
        Toast.makeText(LockActivity.this,"lockedstate="+locked+"" ,
                Toast.LENGTH_SHORT).show();
        Log.i("获取的锁屏状态为",locked+"");
    }
    }

