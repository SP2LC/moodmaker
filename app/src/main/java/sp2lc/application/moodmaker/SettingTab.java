package sp2lc.application.moodmaker;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingTab extends Fragment implements View.OnClickListener{
    private static ArrayAdapter<String> adapter;
    private static ListView listView;
    private static final ArrayList<String> data= new ArrayList<>();
    private static ProgressDialog progress;
    private static Grobal grobal;
    public String ipstring;
    public TextView tv;
    Button button,button2,button3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settingtab, container, false);

        grobal=(Grobal)this.getActivity().getApplication();

        progress= new ProgressDialog(new ContextThemeWrapper(this.getActivity(), R.style.DialogTheme));
        progress.setTitle("更新中");
        progress.setMessage("しばらくお待ちください。");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(true);

        //textセット
        tv=(TextView)view.findViewById(R.id.textView1);
        //Buttonセット
        button = (Button) view.findViewById(R.id.button1);
        button.setOnClickListener(this);
        button2 = (Button) view.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = (Button) view.findViewById(R.id.button3);
        button3.setOnClickListener(this);

        adapter= new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_multiple_choice);
        // アダプターを設定します
        listView = (ListView) view.findViewById(R.id.listview);
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

        //ButtonとTextviewに文字を表示
        try {
            FileInputStream fileInput=getActivity().openFileInput("ip.txt");
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
        return view;
    }

    private void ReadList() {
        FileInputStream fileInput;
        try {
            fileInput = getActivity().openFileInput("id.txt");
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
        Intent intent = new Intent(this.getActivity(), Post.class);
        intent.putExtra("r",r);
        intent.putExtra("g", g);
        intent.putExtra("b", b);
        intent.putExtra("id",id);
        intent.putExtra("mode", mode);
        getActivity().startService(intent);
    }

    public void Save(String address){
        try {
            FileOutputStream outputStream = getActivity().openFileOutput("ip.txt",getActivity().MODE_PRIVATE);
            outputStream.write(address.getBytes());
            outputStream.close();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

    }

    private void SaveFile() {
        if (data.size() > 0) {
            try {
                FileOutputStream fos = getActivity().openFileOutput("id.txt", getActivity().MODE_PRIVATE);
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
           // finish();
        }
        else{
            grobal.ErrorDialog("一つ以上照明を選択してください");
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                Dialogs();
                break;
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

    //IPアドレス入力ダイアログ
    public void Dialogs(){
        AlertDialog.Builder builder = new AlertDialog.Builder
                (new ContextThemeWrapper(this.getActivity(),R.style.DialogTheme));
        final EditText edittext = new EditText(this.getActivity());
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
    public void onDestroy() {
        super.onDestroy();
    }
}

