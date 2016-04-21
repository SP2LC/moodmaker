package sp2lc.application.moodmaker;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ModeTab extends Fragment {
    private Grobal grobal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modetab, container, false);

        setRetainInstance(true);
        grobal=(Grobal) this.getActivity().getApplication();
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        //ボタンをOnClickListenerにセット
        getActivity().findViewById(R.id.study).setOnClickListener(button1ClickListener);
        getActivity().findViewById(R.id.work).setOnClickListener(button2ClickListener);
        getActivity().findViewById(R.id.relax).setOnClickListener(button3ClickListener);
    }

    //モード選択用
    private final View.OnClickListener button1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(grobal.isOpen("id.txt") && grobal.isOpen("ip.txt")) {
                Intent intent = new Intent(getActivity(), Work_Activity.class);//作業モード画面へ遷移
                getActivity().startActivity(intent);
            }
            else if(grobal.isOpen("ip.txt") && !grobal.isOpen("id.txt")){
                grobal.ErrorDialog("SETTINGタブの照明選択にて照明を選択してください");
            }
            else {
                grobal.ErrorDialog("SETTINGタブの照明選択にて照明を選択してください\nSETTINGタブの中継器設定にてIPアドレスを設定してください");
            }
        }
    };
    private final View.OnClickListener button2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(grobal.isOpen("id.txt") && grobal.isOpen("ip.txt")) {
                Intent intent = new Intent(getActivity(), Relax_Activity.class);//リラックスモード画面へ遷移
                getActivity().startActivity(intent);
            }
            else if(grobal.isOpen("ip.txt") && !grobal.isOpen("id.txt")){
                grobal.ErrorDialog("SETTINGタブの照明選択にて照明を選択してください");
            }

            else {
                grobal.ErrorDialog("SETTINGタブの照明選択にて照明を選択してください\nSETTINGタブの中継器設定にてIPアドレスを設定してください");
            }
        }
    };
    private final View.OnClickListener button3ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(grobal.isOpen("id.txt") && grobal.isOpen("ip.txt")) {
                Intent intent = new Intent(getActivity(), Long_Activity.class);//長時間モード画面へ遷移
                getActivity().startActivity(intent);
            }
            else if(grobal.isOpen("ip.txt") && !grobal.isOpen("id.txt")){
                grobal.ErrorDialog("SETTINGタブの照明選択にて照明を選択してください");
            }
            else {
                grobal.ErrorDialog("SETTINGタブの照明選択にて照明を選択してください\nSETTINGタブの中継器設定にてIPアドレスを設定してください");
            }
        }
    };

}
