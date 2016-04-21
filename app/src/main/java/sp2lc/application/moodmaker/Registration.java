package sp2lc.application.moodmaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Registration extends Activity{

    ArrayAdapter<String> adapter = null;
    ArrayList<String> data=new ArrayList<>();
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        //ListViewオブジェクトの取得
        listView = (ListView) findViewById(R.id.list_view);
        //ArrayAdapterオブジェクト生成
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        //Buttonオブジェクト取得
        Button btn = (Button) findViewById(R.id.btn);
        Fileread();
        Nameread();
        //クリックイベントの通知先指定
        btn.setOnClickListener(new OnClickListener() {

            //クリックイベント
            @Override
            public void onClick(View v) {
                //要素追加
                addStringData();
            }
        });
        //Adapterのセット
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //要素タップ
                listView = (ListView) parent;
                String selectedItem = data.get(position);
                String[] parm = new String[3];
                String tmp[] = selectedItem.split("&", 0);
                for (int i = 0; i < tmp.length; i++) {
                    parm[i] = tmp[i];
                }
                System.out.println(parm[0]+parm[1]+parm[2]);
                DoIntent(parm[0], parm[1], parm[2]);
            }
        });

        listView.setOnItemLongClickListener
                (new AdapterView.OnItemLongClickListener() {

                    //要素長押し
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, int position, long id) {
                        ListView list = (ListView) parent;
                        String selectedItem = (String) list
                                .getItemAtPosition(position);

                        showDialogFragment(selectedItem,position);
                        return true;
                    }
                });
    }

    //RGB値をポスト
    public  void DoIntent(String R ,String G ,String B){
        Intent intent = new Intent(this, Post.class);
        intent.putExtra("r", R);
        intent.putExtra("g", G);
        intent.putExtra("b", B);
        intent.putExtra("mode", "1");
        startService(intent);
    }

    private void showDialogFragment(String selectedItem,int position) {
        FragmentManager manager = getFragmentManager();
        DeleteDialog dialog = new DeleteDialog();
        dialog.setSelectedItem(selectedItem,position);

        dialog.show(manager, "dialog");
    }

    //要素追加処理
    private void addStringData() {

        AlertDialog.Builder builder = new AlertDialog.Builder
                (new ContextThemeWrapper(this, R.style.DialogTheme));
        final EditText edittext = new EditText(this);
        builder.setView(edittext);
        builder.setCancelable(false);
        builder.setTitle("名前を入力してください。");
        edittext.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String getname = edittext.getText().toString();
                Intent intent = getIntent();
                String r = intent.getStringExtra("r");
                String g = intent.getStringExtra("g");
                String b = intent.getStringExtra("b");
                adapter.add(getname);
                data.add(r + "&" + g + "&" + b);
            }
        });
        builder.setNegativeButton("Cancel", null) ;
        builder.show();
    }

    //読み込みの設定
    private void Fileread() {
        try {
            FileInputStream fileInput=openFileInput("rgb.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
            String str;
            while ((str = br.readLine()) != null) {
                data.add(str);
            }
            br.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        try {
            FileInputStream fileInput=openFileInput("name.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
            String str;
            while ((str = br.readLine()) != null) {
                adapter.add(str);
            }
            br.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
        }
    }

    private void Nameread(){

    }

    //画面遷移時登録情報保存
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            FileOutputStream outputStream = openFileOutput("rgb.txt", MODE_PRIVATE );
            for (int i = 0; i < data.size(); i++) {
                String str = data.get(i);
                str = str + System.getProperty("line.separator");
                outputStream.write(str.getBytes());
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream outputStream = openFileOutput("name.txt",MODE_PRIVATE);
            for (int i = 0; i < adapter.getCount(); i++) {
                String str = adapter.getItem(i);
                str = str + System.getProperty("line.separator");
                outputStream.write(str.getBytes());
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public static class DeleteDialog extends DialogFragment {

    // 選択したListViewアイテム
    private String selectedItem = null;
    private int position = 0;

    // 削除ダイアログの作成．
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder
                (new ContextThemeWrapper(getActivity(), R.style.DialogTheme));
        builder.setTitle("削除");
        builder.setMessage("削除しますがよろしいですか？");

        // positiveを選択した場合の処理．
        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {

            // 外部クラスのインスタンスを直接参照することができないため，
            // Activity#getActivity()でActivityのインスタンスを取得する
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Registration activity = (Registration) getActivity();
                activity.removeItem(selectedItem,position);
            }
        });
        builder.setNegativeButton("いいえ", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    // 選択したアイテムをセットする．
    public void setSelectedItem(String selectedItem,int position) {
        this.selectedItem = selectedItem;
        this.position = position;
    }

}

    // 選択したアイテムを削除する．
    protected void removeItem(String selectedItem,int position) {
        adapter.remove(selectedItem);
        data.remove(position);
    }
}

