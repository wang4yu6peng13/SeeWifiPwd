package com.wyp.seewifipwd;

/**
 * Created by WYP on 2015/1/21.
 */
import android.app.Application;
public class MyApplication extends Application{
    private int root_textmode;
    public int getTxtMode(){
        return this.root_textmode;
    }
    public void setTxtMode(int txtmode){
        this.root_textmode=txtmode;
    }

    @Override
    public void onCreate(){
        root_textmode=0;
        super.onCreate();
    }

}
