package sp2lc.application.moodmaker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Groups extends Activity implements View.OnClickListener{
    private static ArrayAdapter<String> adapter;
    private static ListView listView;
    private static final ArrayList<String>data= new ArrayList<>();
    private static ProgressDialog progress;
    private static Grobal grobal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);

        grobal=(Grobal)this.getApplication();

        progress= new ProgressDialog(new ContextThemeWrapper(this, R.style.DialogTheme));
        progress.setTitle("更新中");
        progress.setMessage("しばらくお待ちください。");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(true);

        adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice);
        // アダプターを設定します
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        ReadList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView = (ListView) parent;
                Boolean isCheaked = false;
                String item = adapter.getItem(position);
                for (int i = 0; i < data.size(); i++) {
                    if (item.equals(data.get(i))) {
                        isCheaked = true;
                        DoIntent("1024", "1024", "1024", "2", item);
                        data.remove(i);
                    }
                }
                if (!isCheaked) {
                    data.add(item);
                    DoIntent("1024", "0", "0", "2", item);
                }
            }
        });

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.button3);
        button2.setOnClickListener(this);

    }

    private void ReadList() {
        FileInputStream fileInput;
        try {
            fileInput = openFileInput("id.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
            int num=0;
            String str;
            while ((str = br.readLine()) != null) {
                adapter.add(str);
                listView.setItemChecked(num, true);
                data.add(str);
                num++;
            }
            br.close();
            fileInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void DoIntent(String r,String g,String b,String mode,String id){
        Intent intent = new Intent(this, Post.class);
        intent.putExtra("r",r);
        intent.putExtra("g", g);
        intent.putExtra("b", b);
        intent.putExtra("id",id);
        intent.putExtra("mode", mode);
        startService(intent);
    }

    private void SaveFile() {
        if (data.size() > 0) {
            try {
                FileOutputStream fos = openFileOutput("id.txt", MODE_PRIVATE);
                if(data.size()==adapter.getCount()){
                    fos.write("78".getBytes());
                }
                else {
                    for (int i = 0; i < data.size(); i++) {
                        String str = data.get(i);
                        str = str + System.getProperty("line.separator");
                        fos.write(str.getBytes());
                    }
                }
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }
        else{
            grobal.ErrorDialog("一つ以上照明を選択してください");
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:
                SaveFile();
                DoIntent("0","0","0","1","78");
               // PiconPicon();
                break;
            case R.id.button3:
                if(grobal.isOpen("ip.txt")) {
                    progress.show();
                    DoIntent("1024", "1024", "1024", "0", "78");
                    GetReponse();
                }
                else{
                    grobal.ErrorDialog("SETTINGタブの中継器設定にてIPアドレスを設定してください");
                }
                break;
        }
    }
      static final Handler handler = new Handler() {
        public  void handleMessage(Message msg) {
            super.handleMessage(msg);
                String id = msg.getData().getString("id");
                if (id != null && id.length() > 0) {
                    for (int i=0;i<adapter.getCount();i++){
                       listView.setItemChecked(i,false);
                    }
                    adapter.clear();
                    data.clear();
                    String temp[] = id.split(",", 0);
                    for (int i = 0; i < temp.length; i++) {
                        adapter.add(temp[i]);
                    }
                }
            String err = msg.getData().getString("error");
            if (err != null && err.length() > 0) {
                for (int i=0;i<adapter.getCount();i++){
                    listView.setItemChecked(i,false);
                }
                adapter.clear();
                data.clear();
            }
                progress.dismiss();
        }

    };
    private static void GetReponse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (grobal.response == null || grobal.response.length() == 0);
                handler.sendEmptyMessage(0);
                if(!grobal.response.equals("error")) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("id", grobal.response);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                }
                else {
                    grobal.ErrorDialog("通信環境を確認してください");
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("error", "error");
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                }
                grobal.response = "";
                }
        }).start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
