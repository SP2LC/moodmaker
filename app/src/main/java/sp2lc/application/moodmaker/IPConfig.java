package sp2lc.application.moodmaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPConfig extends Activity implements OnClickListener {
    public String ipstring;
    public TextView tv;
    Grobal grobal;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipconfig);
        grobal=(Grobal)this.getApplication();
        //textセット
        tv=(TextView)findViewById(R.id.textView1);
        //Buttonセット
         button= (Button)findViewById(R.id.button1);
        button.setOnClickListener(this);

        //ButtonとTextviewに文字を表示
        try {
            FileInputStream fileInput=openFileInput("ip.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
            ipstring = br.readLine();
            br.close();
            if( ipstring != null || ipstring.length() != 0 ){
                button.setText("中継器の変更");
                tv.setVisibility(View.VISIBLE);
                tv.setText(ipstring);
            }
            else{
                button.setText("中継器を設定してください");
                tv.setText("ipアドレスを指定してください");
            }
        }catch(FileNotFoundException e){
            button.setText("中継器を設定してください");
            tv.setText("ipアドレスを指定してください");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void Save(String address){
        try {
            FileOutputStream outputStream = openFileOutput("ip.txt",MODE_PRIVATE);
            outputStream.write(address.getBytes());
            outputStream.close();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

    }

    //IPアドレス入力ダイアログ
    public void Dialogs(){
        AlertDialog.Builder builder = new AlertDialog.Builder
                (new ContextThemeWrapper(this, R.style.DialogTheme));
        final EditText edittext = new EditText(this);
        builder.setView(edittext);
        builder.setCancelable(false);
        builder.setTitle("IPアドレスを入力して下さい");
        edittext.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String getip = edittext.getText().toString();
                Pattern pattern = Pattern.compile("\\.[\\d{3}\\d{2}\\d{1}]+");
                Matcher matcher = pattern.matcher(getip);
                int i = 0;
                while (matcher.find()) {
                    i++;
                }
                if (i == 3) {
                    if (!getip.startsWith("http://")) {
                        StringBuilder sb = new StringBuilder();
                        if(isPort(getip)){
                            sb.append("http://");
                            sb.append(getip);
                        }
                        else {
                            sb.append("http://");
                            sb.append(getip);
                            sb.append(":8080");
                        }
                        String url = new String(sb);
                        tv.setText(url);
                        button.setText("中継器の変更");
                        Save(url);
                    } else {
                        if(isPort(getip)){
                            Save(getip);
                        }
                        else{
                            StringBuilder sb=new StringBuilder();
                            sb.append(getip);
                            sb.append(":8080");
                            Save(sb.toString());
                        }
                        tv.setText(getip);
                        button.setText("中継器の変更");
                    }
                    ;
                } else {
                    grobal.ErrorDialog("IPアドレスを確認してください");
                }
            }
        });
        builder.setNegativeButton("Cancel", null) ;
        builder.show();
    }
    private boolean isPort(String str){
        Pattern pattern = Pattern.compile(":\\d+");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return  true;
        }
        else{
            return  false;
        }

    }

    @Override
    public void onClick(View v) {
        Dialogs();
    }
}


