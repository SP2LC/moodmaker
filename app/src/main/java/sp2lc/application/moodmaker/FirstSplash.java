package sp2lc.application.moodmaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Created by Youta on 2015/10/11.
 */
public class FirstSplash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(getApplication(), TabMain.class);
                    startActivity(intent);
                    finish();
            }
        },1000);
    }
}
