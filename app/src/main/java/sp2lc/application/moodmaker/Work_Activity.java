package sp2lc.application.moodmaker;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class Work_Activity extends Activity implements View.OnClickListener{

    //変数を宣言

    Chronometer mChronometer;
    boolean flag;
    ReadFile readFile;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.work_activity);
        //ボタンをOnClickListenerにセット
        Button finish = (Button)findViewById(R.id.button4);
        finish.setOnClickListener(this);
        //タイマーセット＆スタート
        mChronometer = (Chronometer)findViewById(R.id.time3);
        mChronometer.start();
        flag=false;
        //RGB値読み込み
        try {
            InputStream inputStream = this.getAssets().open("work_color.txt");
            readFile = new ReadFile(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        StartService();
        SleepPost();
    }

    //読み込んだ値をポスト
    private  void SleepPost(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Data> list = readFile.Read();
                int i = 0;
                boolean type =true;
                while (!flag == true) {
                    Intent intent = new Intent(getBaseContext(), Post.class);
                    intent.putExtra("r", list.get(i).r);
                    intent.putExtra("g", list.get(i).g);
                    intent.putExtra("b", list.get(i).b);
                    intent.putExtra("mode", "1");
                    startService(intent);
                    System.out.println(list.get(i).r+":"+list.get(i).g+":"+list.get(i).b);
                    try {
                        Thread.sleep(Integer.parseInt(list.get(i).time)*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(type) {
                        i++;
                    }else{
                        i--;
                    }
                    if(i == list.size()) {
                        type = false;
                    }else if(i == 0){
                        type = true;
                    }
                }
            }
        }).start();
    }

    //音楽再生を強制終了、ひとつ前の画面に遷移
    @Override
    public void onClick(View v){
        finish();
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    //スマートフォンのバックキーを使用不可に
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void StartService(){
        Intent intent = new Intent(this,Music.class);
        intent.putExtra("mode","work");
        startService(intent);
    }

    //アクティビティーが破棄された時
    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = true;
        stopService(new Intent(this, Music.class));
    }
}



