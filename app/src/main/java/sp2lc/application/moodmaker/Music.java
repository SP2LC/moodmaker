package sp2lc.application.moodmaker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Youta on 2015/10/08.
 */
public class Music extends Service implements  MediaPlayer.OnCompletionListener{
    PowerManager pm;
    PowerManager.WakeLock wl;
    MediaPlayer[] md;

    ArrayList<String>Work=new ArrayList<>();
    ArrayList<String>Relax=new ArrayList<>();
    ArrayList<String>Middle=new ArrayList<>();
    String mode;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        mode =intent.getStringExtra("mode");
        if(mode.equals("relax")){
            Relax=MusicRead(mode);
            Collections.shuffle(Relax);
            Play(Relax);
        }
        else if(mode.equals("work")){
            Work=MusicRead(mode);
            Collections.shuffle(Work);
            Play(Work);
        }
        else if(mode.equals("long")){
            Middle=MusicRead("middle");
            Collections.shuffle(Middle);
            Work=MusicRead("work");
            Collections.shuffle(Work);
            Relax=MusicRead("relax");
            Collections.shuffle(Relax);
            Play(MusicLength(Work, Middle, Relax));
            }
        return START_REDELIVER_INTENT;
    }
    private ArrayList<String> MusicRead(String mode) {
        InputStream is;
        BufferedReader br;
        String str;
        ArrayList<String> list =new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(mode);
        sb.append("music.txt");
        String filename =new String(sb);

        try {
            is= this.getAssets().open(filename);
            br = new BufferedReader(new InputStreamReader(is));
            while ((str = br.readLine()) != null ) {
              list.add(str);
                System.out.println(str);
            }
            is.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private  void  Play(ArrayList<String> musiclist) {
        int num = musiclist.size();

         md= new MediaPlayer[num];

            for (int i = 0; i < num; i++) {
                md[i]=SetData(musiclist.get(i));
            }
            md[0].start();
        MediaPlayer tmp;
        tmp = md[0];
            for (int i = 1; i < num; i++) {
                tmp.setNextMediaPlayer(md[i]);
                tmp = md[i];
            }
          md[num-1].setOnCompletionListener(this);
    }
    private MediaPlayer SetData(String filaneme){
        MediaPlayer mediaPlayer =new MediaPlayer();
        try {
            AssetFileDescriptor  afd = getAssets().openFd(filaneme);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
      return  mediaPlayer;
    }


    private  ArrayList<String>  MusicLength(ArrayList<String> worklist,ArrayList<String> middlelist,ArrayList<String> relaxlist) {
        MediaPlayer mediaPlayer;
        ArrayList<String> str = new ArrayList<>();
        int Length = 0;
        try {
            for (int i = 0; i < worklist.size(); i++) {
               mediaPlayer=SetData(worklist.get(i));
                Length += mediaPlayer.getDuration();
                if (Length > 840000 && Length < 960000) {
                    str.add(worklist.get(i));
                    break;
                } else if (Length > 960000) {
                    Length -=  mediaPlayer.getDuration();
                } else {
                    str.add(worklist.get(i));
                }
            }
            for (int i = 0; i < middlelist.size(); i++) {
                mediaPlayer=SetData(middlelist.get(i));
                Length += mediaPlayer.getDuration();
                if (Length > 1260000 && Length < 1380000) {
                    str.add(middlelist.get(i));
                    break;
                } else if (Length > 1380000) {
                    Length -=  mediaPlayer.getDuration();
                } else {
                    str.add(middlelist.get(i));
                }
            }
            for (int i = 0; i < relaxlist.size(); i++) {
                mediaPlayer=SetData(relaxlist.get(i));
                Length += mediaPlayer.getDuration();
                if (Length > 1800000 && Length < 1920000) {
                    str.add(relaxlist.get(i));
                    break;
                } else if (Length > 1920000) {
                    Length -=  mediaPlayer.getDuration();
                } else {
                    str.add(relaxlist.get(i));
                }
            }
            Collections.shuffle(middlelist);
            for (int i = 0; i < middlelist.size(); i++) {
                mediaPlayer=SetData(middlelist.get(i));
                Length += mediaPlayer.getDuration();
                if (Length > 1980000 && Length < 2100000) {
                    str.add(middlelist.get(i));
                    break;
                } else if (Length > 2100000) {
                    Length -=  mediaPlayer.getDuration();
                } else {
                    str.add(middlelist.get(i));
                }
            }
            System.out.println("len:"+Length);
        } catch (Exception e) {
            e.printStackTrace();
        }
return  str;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mode.equals("relax")){
            Collections.shuffle(Relax);
            Play(Relax);
        }
        else if(mode.equals("work")){
            Collections.shuffle(Work);
            Play(Work);
        }
        else if(mode.equals("long")){
            Collections.shuffle(Middle);
            Collections.shuffle(Work);
            Collections.shuffle(Relax);
            Play(MusicLength(Work, Middle, Relax));
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        for(int i=0;i<md.length;i++){
            md[i].release();
        }
    }
}
