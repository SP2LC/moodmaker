package sp2lc.application.moodmaker;
/**
 * Created by Ryu on 2015/09/14.
 */

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


public class Long_Activity extends Activity implements View.OnClickListener {

    //メディアプレイヤー型の変数を宣言
    private boolean flag;
    private ReadFile readFile;
    private Chronometer mChronometer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_activity);

        //ボタンをOnClickListenerにセット
        Button button = (Button)findViewById(R.id.button4);
        button.setOnClickListener(this);
        //タイマーをセット＆スタート
        mChronometer = (Chronometer)findViewById(R.id.time1);
        mChronometer.start();
        flag = false;
        try {
            InputStream inputStream = this.getAssets().open("long_color.txt");
            readFile = new ReadFile(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        StartService();
        SleepPost();
    }

    //RGB値をセット
    private  void SleepPost(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Data> list = readFile.Read();
                int i = 0;
                while (!flag) {
                        Intent intent = new Intent(getBaseContext(), Post.class);
                        intent.putExtra("r", list.get(i).r);
                        intent.putExtra("g", list.get(i).g);
                        intent.putExtra("b", list.get(i).b);
                        intent.putExtra("mode", "1");
                        startService(intent);

                        try {
                            Thread.sleep(Integer.parseInt(list.get(i).time)*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    i++;
                    if(i == list.size()) {
                        i = 0;
                    }
                }
            }
        }).start();
    }

    //音楽再生を強制終了、ひとつ前の画面に遷移
    public void onClick(View v){
        finish();
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    //スマートフォンのバック
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void StartService(){
        Intent intent = new Intent(this,Music.class);
        intent.putExtra("mode","long");
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

