package com.wyp.seewifipwd;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import me.drakeet.materialdialog.MaterialDialog;


public class OpenSourceActivity extends ActionBarActivity  implements View.OnClickListener{
    private LinearLayout seepwd,aos,fab,sbt,md,thisapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);
        seepwd=(LinearLayout)findViewById(R.id.seepwd);
        aos=(LinearLayout)findViewById(R.id.aos);
        fab=(LinearLayout)findViewById(R.id.fab);
        sbt=(LinearLayout)findViewById(R.id.sbt);
        md=(LinearLayout)findViewById(R.id.md);
        thisapp=(LinearLayout)findViewById(R.id.thisapp);

        seepwd.setOnClickListener(this);
        aos.setOnClickListener(this);
        fab.setOnClickListener(this);
        sbt.setOnClickListener(this);
        md.setOnClickListener(this);
        thisapp.setOnClickListener(this);


        //填充状态栏
        setStatusStyle();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seepwd:
                Uri uri_seepwd = Uri.parse(""+getString(R.string.seepwd_site));
                Intent intent_seepwd = new Intent();
                intent_seepwd.setAction(Intent.ACTION_VIEW);
                intent_seepwd.setData(uri_seepwd);
                startActivity(intent_seepwd);
                break;
            case R.id.aos:
                Uri uri_aos = Uri.parse(""+getString(R.string.aos_site));
                Intent intent_aos = new Intent();
                intent_aos.setAction(Intent.ACTION_VIEW);
                intent_aos.setData(uri_aos);
                startActivity(intent_aos);
                break;
            case R.id.fab:
                Uri uri_fab = Uri.parse(""+getString(R.string.fab_site));
                Intent intent_fab = new Intent();
                intent_fab.setAction(Intent.ACTION_VIEW);
                intent_fab.setData(uri_fab);
                startActivity(intent_fab);
                break;
            case R.id.sbt:
                Uri uri_sbt = Uri.parse(""+getString(R.string.sbt_site));
                Intent intent_sbt = new Intent();
                intent_sbt.setAction(Intent.ACTION_VIEW);
                intent_sbt.setData(uri_sbt);
                startActivity(intent_sbt);
                break;
            case R.id.md:
                Uri uri_md = Uri.parse(""+getString(R.string.md_site));
                Intent intent_md = new Intent();
                intent_md.setAction(Intent.ACTION_VIEW);
                intent_md.setData(uri_md);
                startActivity(intent_md);
                break;
            case R.id.thisapp:
                Uri uri_thisapp = Uri.parse(""+getString(R.string.thisapp_site));
                Intent intent_thisapp = new Intent();
                intent_thisapp.setAction(Intent.ACTION_VIEW);
                intent_thisapp.setData(uri_thisapp);
                startActivity(intent_thisapp);
                break;

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

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.primary);
//        tintManager.setStatusBarTintDrawable(getResources().getDrawable(R.drawable.homeimage));
    }

}
