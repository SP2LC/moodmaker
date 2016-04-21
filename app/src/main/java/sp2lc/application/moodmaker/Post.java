package sp2lc.application.moodmaker;

/**
 * Created by Ryu on 2015/09/14.
 */

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Post extends IntentService {
    Grobal grobal;

    public Post() {
        // TODO 自動生成されたコンストラクター・スタブ
        super("Post");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO 自動生成されたメソッド・スタブ
        //非同期処理
        String r = intent.getStringExtra("r");
        String g = intent.getStringExtra("g");
        String b = intent.getStringExtra("b");
        String mode = intent.getStringExtra("mode");
        grobal = (Grobal) this.getApplication();
        String ip = grobal.Read_IP();
            switch (Integer.parseInt(mode)){
                case 0:
                    String id = intent.getStringExtra("id");
                    String response = DoPost(ip, r, g, b, mode, id);
                    if (response != null && response.length() > 0){
                        grobal.response = response;
                    }
                    else{
                        grobal.response="error";
                    }
                    break;
                case  1:
                    if(grobal.isOpen("id.txt") && grobal.isOpen("ip.txt")) {
                        ArrayList<String> id_list = grobal.Read_ID();
                        for (int i = 0; i < id_list.size(); i++) {
                            DoPost(ip, r, g, b, mode, id_list.get(i));
                        }
                    }
                    else if(grobal.isOpen("ip.txt") && !grobal.isOpen("id.txt")){
                        grobal.ErrorDialog("SETTINGタブの照明選択にて照明を選択してください");
                    }
                    else {
                        grobal.ErrorDialog("SETTINGタブの照明選択にて照明を選択してください\nSETTINGタブの中継器設定にてIPアドレスを設定してください");
                    }
                    break;
                case  2:
                    String id2 = intent.getStringExtra("id");
                    DoPost(ip, r, g, b, "1", id2);
                    break;
            }
    }

    //RGB値をポスト
    private String DoPost(String target_url, String r, String g, String b, String mode, String id) {
        StringBuilder uri = new StringBuilder();
        uri.append("r=");
        uri.append(r);
        uri.append("&g=");
        uri.append(g);
        uri.append("&b=");
        uri.append(b);
        uri.append("&mode=");
        uri.append(mode);
        uri.append("&id=");
        uri.append(id);
        String postdata = new String(uri);
        String body = "";
        StringBuilder str = new StringBuilder();

        try {
            URL url = new URL(target_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            BufferedWriter bw = new BufferedWriter(out);
            bw.write(postdata);
            bw.close();
            out.close();
            if (connection.getResponseCode() ==HttpURLConnection.HTTP_OK) {
                InputStreamReader in = new InputStreamReader(connection.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String line;
                while ((line = br.readLine()) != null) {
                    str.append(line);
                }
                br.close();
                in.close();
            }
            body=str.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}