package com.wyp.seewifipwd;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final boolean TOOLBAR_IS_STICKY = false;
    private SystemBarTintManager tintManager;//=new SystemBarTintManager(this);
    private View mToolbar;
    private View mImageView;
    private View mOverlayView;
    private View mListBackgroundView;
    private TextView mTitleView;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private int mToolbarColor;
    private boolean mFabIsShown;

    private View mHeaderBar;
 //   private TextView mTitle;
    private FrameLayout framelayout;
    private int mColorPrimary;
    private boolean mHeaderColorChangedToBottom;

    private long mExitTime;

    final String cmd="cat /data/misc/wifi/*.conf";
    //    final String cmd="cat /data/misc/wifi/wpa_supplicant.conf";
    final int READ_COMPLELTE=1;
    String out = null;
//    TextView tv;
    public static List<Network> dataList;
    private List<Network> noPwdList;
    private NetworkAdapter networkAdapter;
    ObservableListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize();
        mToolbarColor = getResources().getColor(R.color.primary);

        mToolbar = findViewById(R.id.toolbar);
        if (!TOOLBAR_IS_STICKY) {
            mToolbar.setBackgroundColor(Color.TRANSPARENT);
        }
        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);
        mOverlayView.setVisibility(View.INVISIBLE);

        listView = (ObservableListView) findViewById(R.id.list);
        listView.setScrollViewCallbacks(this);

//        tv=(TextView) findViewById(R.id.tv);
 //       mHeaderBar = findViewById(R.id.header_bar);
 //       mTitle = (TextView) findViewById(R.id.title1);
        framelayout=(FrameLayout)findViewById(R.id.header);
 //       framelayout.setVisibility(View.GONE);

        // Set padding view for ListView. This is the flexible space.
        View paddingView = new View(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                mFlexibleSpaceImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);

        listView.addHeaderView(paddingView);


//        setDummyData(listView);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(R.string.app_name_image);
        setTitle(null);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });

        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        // mListBackgroundView makes ListView's background except header view.
        mListBackgroundView = findViewById(R.id.list_background);
        final View contentView = getWindow().getDecorView().findViewById(android.R.id.content);
        contentView.post(new Runnable() {
            @Override
            public void run() {
                // mListBackgroundView's should fill its parent vertically
                // but the height of the content view is 0 on 'onCreate'.
                // So we should get it with post().
                mListBackgroundView.getLayoutParams().height = contentView.getHeight();
            }
        });


        mColorPrimary = getResources().getColor(R.color.primary);

        //填充状态栏
        setStatusStyle();


        dataList=new ArrayList<Network>();
        noPwdList=new ArrayList<Network>();

        WifiPsd();
    }

    private void WifiPsd() {
       MyApplication roottextmode=(MyApplication)getApplication();

        //获取root并得到wifi列表
        boolean flag;
        //首先取得root权限
//        flag=RootCmd.haveRoot();
//        flag=RootCmd.getRootAhth();
        flag=RootCmd.isRootSystem();
        //然后读取/data/misc/wifi/wpa_supplicant.conf文件
        if(flag){
//        new Thread(){
//            @SuppressWarnings("deprecation")
//            public void run()
//            {
            try {
                StringBuffer sb=new StringBuffer();
                DataInputStream dis=RootCmd.execRootCmd(cmd);
                if(dis.read()==-1){
                //    tv.setVisibility(View.VISIBLE);
                 //   tv.setText(R.string.mustauth);
                    roottextmode.setTxtMode(1);
                    setDummyData(listView);
                    return;
                }
                String temp=null;
                while((temp=dis.readLine())!=null)
                    sb.append(temp);

                out=sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
//                myHandler.sendEmptyMessage(READ_COMPLELTE);
            if(out==null)
            {
                //tv.setVisibility(View.VISIBLE);//break;
                setDummyData(listView);
            }
            dataList=Parser.getNetworks(out);
            for (Network network : dataList) {
                if(network.getPsk()==null)
                    noPwdList.add(network);//把无密码的网络保存起来
            }
            networkAdapter=new NetworkAdapter(this,dataList);
            listView.setAdapter(networkAdapter);

        }
//        }.run();
        else {
            //tv.setVisibility(View.VISIBLE);
           // tv.setText(R.string.mustroot);
            roottextmode.setTxtMode(2);
            setDummyData(listView);
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        ViewHelper.setAlpha(framelayout, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        if (TOOLBAR_IS_STICKY) {
            titleTranslationY = Math.max(0, titleTranslationY);
        }
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
        ViewHelper.setTranslationY(mFab, fabTranslationY);

        // Show/hide FAB
        if (ViewHelper.getTranslationY(mFab) < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }

        if (TOOLBAR_IS_STICKY) {
            // Change alpha of toolbar background
            if (-scrollY + mFlexibleSpaceImageHeight <= mActionBarSize) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, mToolbarColor));
            } else {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolbarColor));
            }
        } else {
            // Translate Toolbar
            if (scrollY < mFlexibleSpaceImageHeight) {
               ViewHelper.setTranslationY(mToolbar, 0);
            } else {
                 ViewHelper.setTranslationY(mToolbar, -scrollY);
            }
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }


//以下是填充状态栏
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void setStatusStyle(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

         tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintAlpha(0);
//		tintManager.setStatusBarTintResource(R.color.primary);
//        tintManager.setStatusBarTintDrawable(getResources().getDrawable(R.drawable.homeimage));
    }

    /**
     * 按两次返回退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                mExitTime = System.currentTimeMillis();

                //ad(Config.WHEN_CLICK_BACK);
                Toast.makeText(this, getString(R.string.twicequit), Toast.LENGTH_SHORT).show();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}