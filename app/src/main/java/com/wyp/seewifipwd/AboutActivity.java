package com.wyp.seewifipwd;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import me.drakeet.materialdialog.MaterialDialog;


public class AboutActivity extends ActionBarActivity implements View.OnClickListener {
    private LinearLayout update;
    private LinearLayout app_explain;
    private LinearLayout open_source;
    private LinearLayout contribute;
    private LinearLayout evaluate;
    private LinearLayout share;
    private LinearLayout weibo;
    private LinearLayout email;
    MaterialDialog mMaterialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        update = (LinearLayout) findViewById(R.id.update);
        app_explain = (LinearLayout) findViewById(R.id.app_explain);
        open_source = (LinearLayout) findViewById(R.id.open_source);
        contribute = (LinearLayout) findViewById(R.id.contribute);
        evaluate = (LinearLayout) findViewById(R.id.evaluate);
        share = (LinearLayout) findViewById(R.id.share_app);
        weibo = (LinearLayout) findViewById(R.id.weibo);
        email = (LinearLayout) findViewById(R.id.email);
        update.setOnClickListener(this);
        app_explain.setOnClickListener(this);
        open_source.setOnClickListener(this);
        contribute.setOnClickListener(this);
        evaluate.setOnClickListener(this);
        share.setOnClickListener(this);
        weibo.setOnClickListener(this);
        email.setOnClickListener(this);

        TextView app_version = (TextView) findViewById(R.id.app_version);
        app_version.setText(getString(R.string.this_version) + ":" + getVersionName());

        //填充状态栏
        setStatusStyle();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                Uri uri_update = Uri.parse("" + getString(R.string.download_site));
                Intent intent_update = new Intent();
                intent_update.setAction(Intent.ACTION_VIEW);
                intent_update.setData(uri_update);
                startActivity(intent_update);
                break;
            case R.id.app_explain:
                Intent intent_explain = new Intent();
                intent_explain.setClass(this, ExplainActivity.class);
                startActivity(intent_explain);
                break;
            case R.id.open_source:
                Intent intent_open = new Intent();
                intent_open.setClass(this, OpenSourceActivity.class);
                startActivity(intent_open);
                break;
            case R.id.contribute:
                copyAlipay();
                mMaterialDialog = new MaterialDialog(this)
                        .setTitle("" + getString(R.string.contribute))
                        .setMessage("" + getString(R.string.contribute_text))
                        .setPositiveButton("" + getString(R.string.open_alipay), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                                openAlipay();

                            }
                        })
                        .setNegativeButton("" + getString(R.string.cancel), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();

                            }
                        });

                mMaterialDialog.show();
                break;
            case R.id.evaluate:
                String mAddress = "market://details?id=" + getPackageName();
                Intent marketIntent = new Intent("android.intent.action.VIEW");
                marketIntent.setData(Uri.parse(mAddress));
                startActivity(marketIntent);
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName() )));
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q="+getPackageName() )));
                break;
            case R.id.share_app:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                // shareIntent.putExtra("android.intent.extra.SUBJECT", "分享");
                shareIntent.setType("text/plain");
                // 需要指定意图的数据类型
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "我向你推荐一个可以查看手机Wifi密码的应用^_^# 下载地址：" + getString(R.string.download_site));
                shareIntent = Intent.createChooser(shareIntent, "分享");
                startActivity(shareIntent);
                break;
            case R.id.weibo:
                Uri uri_weibo = Uri.parse("" + getString(R.string.weibo_site));
                Intent intent_weibo = new Intent();
                intent_weibo.setAction(Intent.ACTION_VIEW);
                intent_weibo.setData(uri_weibo);
                startActivity(intent_weibo);
                break;
            case R.id.email:
                Intent intent_email = new Intent(Intent.ACTION_SENDTO);
                intent_email.setData(Uri.parse("mailto:" + getString(R.string.send_email_site)));
                intent_email.putExtra(Intent.EXTRA_SUBJECT, "Wifi密码查看反馈");
                intent_email.putExtra(Intent.EXTRA_TEXT, "请填写邮件内容");
                startActivity(intent_email);
                break;
        }
    }

    //通过包名检测系统中是否安装某个应用程序
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    private void openAlipay() {
        if (checkApkExist(this, "com.eg.android.AlipayGphone")) {
            Intent intent_alipay = getPackageManager()
                    .getLaunchIntentForPackage("com.eg.android.AlipayGphone");
            startActivity(intent_alipay);
        } else {

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.eg.android.AlipayGphone")));
        }
    }

    private void copyAlipay() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "wang4yu6peng13@qq.com"));
        if (clipboardManager.hasPrimaryClip()) {
            clipboardManager.getPrimaryClip().getItemAt(0).getText();
        }
    }

    private String getVersionName() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = null;

            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
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

    private void setStatusStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.primary);
//        tintManager.setStatusBarTintDrawable(getResources().getDrawable(R.drawable.homeimage));
    }
}
