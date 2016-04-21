package sp2lc.application.moodmaker;

/**
 * Created by Ryu on 2015/09/14.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

public class TabMain extends FragmentActivity  implements TabHost.OnTabChangeListener {

    private TabHost mTabHost;

    private String mLastTabId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabmain);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // Tab1 設定
        TabHost.TabSpec tab1 = mTabHost.newTabSpec("tab1");
        View view = View.inflate(getApplication(),R.layout.modelayout,null);
        tab1.setIndicator(view);
        tab1.setContent(new DummyTabFactory(this));
        mTabHost.addTab(tab1);

        // Tab2 設定
        TabHost.TabSpec tab2 = mTabHost.newTabSpec("tab2");
        View view2 = View.inflate(getApplication(),R.layout.rightinglayout,null);
        tab2.setIndicator(view2);
        tab2.setContent(new DummyTabFactory(this));
        mTabHost.addTab(tab2);

        // Tab3 設定
        TabHost.TabSpec tab3 = mTabHost.newTabSpec("tab3");
        View view3 = View.inflate(getApplication(), R.layout.settinglayout, null);
        tab3.setIndicator(view3);
        tab3.setContent(new DummyTabFactory(this));
        mTabHost.addTab(tab3);

        // タブ変更時イベントハンドラ
        mTabHost.setOnTabChangedListener(this);

        // 初期タブ選択
        onTabChanged("tab1");
    }

    //タブの選択が変わったときに呼び出される
    public void onTabChanged(String tabId) {
        if(mLastTabId != tabId){
            FragmentTransaction fragmentTransaction
                    = getSupportFragmentManager().beginTransaction();
            if("tab1" == tabId){
                if(mLastTabId == "tab2" || mLastTabId == "tab3") {
                    fragmentTransaction.setCustomAnimations(
                            R.anim.fragment_slide_left_enter,
                            R.anim.fragment_slide_left_exit);
                }
                fragmentTransaction
                        .replace(R.id.realtabcontent, new ModeTab());
            }else if("tab2" == tabId){
                if(mLastTabId == "tab1") {
                    fragmentTransaction.setCustomAnimations(
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_right_exit);
                }else if(mLastTabId == "tab3"){
                    fragmentTransaction.setCustomAnimations(
                            R.anim.fragment_slide_left_enter,
                            R.anim.fragment_slide_left_exit);
                }
                fragmentTransaction
                        .replace(R.id.realtabcontent, new LightingTab());
            }else if("tab3" == tabId){
                fragmentTransaction.setCustomAnimations(
                        R.anim.fragment_slide_right_enter,
                        R.anim.fragment_slide_right_exit);
                fragmentTransaction
                        .replace(R.id.realtabcontent, new SettingTab());
            }
            mLastTabId = tabId;
            fragmentTransaction.commit();
        }
    }

    private static class DummyTabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            return v;
        }
    }
}


