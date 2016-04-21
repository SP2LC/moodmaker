package sp2lc.application.moodmaker;

import android.app.Application;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Grobal extends Application {
    public String response="";

    public boolean isOpen(String filename){
        FileInputStream fileInput;
        Boolean flag=true;
        try {
            fileInput= openFileInput(filename);
            fileInput.close();
        } catch (FileNotFoundException e) {
            flag=false;
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
    public ArrayList<String> Read_ID() {
        FileInputStream fileInput;
        ArrayList<String> id = new ArrayList<>();
        try {
            fileInput = openFileInput("id.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
            String str;
            while ((str = br.readLine()) != null) {
                id.add(str);
            }
            br.close();
            fileInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  id;
    }
    public int[] Read_RGB() {
        FileInputStream fileInput;
      int[] rgb=new int[3];
        try {
            fileInput = openFileInput("rgb_seekbar.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
            String str;
           str=br.readLine();
            String temp[]= str.split(",",0);
             for(int i=0;i<temp.length;i++){
                 rgb[i]=Integer.parseInt(temp[i]);
             }

            br.close();
            fileInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  rgb;
    }
    public String Read_IP(){
        FileInputStream fileInput;
        String str="";
        try {
            fileInput = openFileInput("ip.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
            str = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    public void Save_RGB(String str){
        try {
            FileOutputStream outputStream = openFileOutput("rgb_seekbar.txt",MODE_PRIVATE);
            outputStream.write(str.getBytes());
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void ErrorDialog(String msg){
        Intent intent = new Intent(this,ErrorDialog.class);
        intent.putExtra("msg", msg);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
