package sp2lc.application.moodmaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;


public class ErrorDialog extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        Error_Dialog(msg);
        }


    public void Error_Dialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder
                (new ContextThemeWrapper(this, R.style.DialogTheme));
        builder.setTitle("エラー");
        builder.setMessage(msg);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setOnCancelListener( new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO 自動生成されたメソッド・スタブ
                finish();
            }
        });
        builder.show();
    }
}
