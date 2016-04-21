package sp2lc.application.moodmaker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import java.io.FileOutputStream;


public class LightingTab extends Fragment {
    private SeekBar r, g, b;
    private  boolean flag=false;
    private  boolean r_flag,g_flag,b_flag;
    private Grobal grobal;
    public LightingTab(){
        setRetainInstance(true);
    }
    //色登録画面へ遷移
    private final View.OnClickListener button1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String R =String.valueOf(r.getProgress());
            String G = String.valueOf(g.getProgress());
            String B = String.valueOf(b.getProgress());
            Intent intent = new Intent(getActivity(),Registration.class);
            intent.putExtra("r", R);
            intent.putExtra("g", G);
            intent.putExtra("b", B);
            getActivity().startActivity(intent);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lightingtab, container, false);


        //変数r,g,bにシークバーの値を代入
        r = (SeekBar) view.findViewById(R.id.seekBar);
        g = (SeekBar) view.findViewById(R.id.seekBar3);
        b = (SeekBar) view.findViewById(R.id.seekBar2);


        r.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                r_flag=true;
                if(!flag) {
                    flag = true;
                    DoIntent();
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                r_flag=false;
                if(!r_flag&&!g_flag&&!b_flag) {
                    flag = false;
                }

            }
        });

        g.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                g_flag = true;
                if (!flag) {
                    flag = true;
                    DoIntent();
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                g_flag = false;
                if (!r_flag && !g_flag && !b_flag) {
                    flag = false;
                }
            }
        });

        b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                b_flag = true;
                if (!flag) {
                    flag = true;
                    DoIntent();
                }
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                b_flag = false;
                if (!r_flag && !g_flag && !b_flag) {
                    flag = false;
                }

            }
        });
        grobal=(Grobal) this.getActivity().getApplication();
        if(grobal.isOpen("rgb_seekbar.txt")){
            int rgb[]=grobal.Read_RGB();
            r.setProgress(rgb[0]);
            g.setProgress(rgb[1]);
            b.setProgress(rgb[2]);
            if(grobal.isOpen("id.txt") && grobal.isOpen("ip.txt")) {
                Intent intent = new Intent(getActivity(), Post.class);
                intent.putExtra("r", String.valueOf(rgb[0]));
                intent.putExtra("g", String.valueOf(rgb[1]));
                intent.putExtra("b", String.valueOf(rgb[2]));
                intent.putExtra("mode", "1");
                getActivity().startService(intent);
            }
        }
        else{
            r.setProgress(512);
            g.setProgress(512);
            b.setProgress(512);
            if(grobal.isOpen("id.txt") && grobal.isOpen("ip.txt")) {
                Intent intent = new Intent(getActivity(), Post.class);
                intent.putExtra("r", String.valueOf("512"));
                intent.putExtra("g", String.valueOf("512"));
                intent.putExtra("b", String.valueOf("512"));
                intent.putExtra("mode", "1");
                getActivity().startService(intent);
            }
        }
        return view;
    }
    //値を送信
    private  void  DoIntent(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                 while(flag) {
                     String R = String.valueOf(r.getProgress());
                     String G = String.valueOf(g.getProgress());
                     String B = String.valueOf(b.getProgress());
                     Intent intent = new Intent(getActivity(), Post.class);
                     intent.putExtra("r", R);
                     intent.putExtra("g", G);
                     intent.putExtra("b", B);
                     intent.putExtra("mode", "1");
                     getActivity().startService(intent);
                     try {
                         Thread.sleep(250);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
            }
        }).start();
    }
    @Override
    public void onStart(){
        super.onStart();
        //ボタンをセット
        getActivity().findViewById(R.id.button6).setOnClickListener(button1ClickListener);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        StringBuilder sb= new StringBuilder();
        sb.append(String.valueOf(r.getProgress()));
        sb.append(",");
        sb.append(String.valueOf(g.getProgress()));
        sb.append(",");
        sb.append(String.valueOf(b.getProgress()));
        grobal.Save_RGB(sb.toString());
    }
}

