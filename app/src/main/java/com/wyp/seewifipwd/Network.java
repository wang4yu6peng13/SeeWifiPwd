package com.wyp.seewifipwd;

/**
 * Created by WYP on 2015/1/21.
 */
public class Network implements Comparable<Network>{

    private String ssid;
    private String psk;
    private String security;
    public String getSsid() {
        return ssid;
    }
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
    public String getPsk() {
        return psk;
    }
    public void setPsk(String psk) {
        this.psk = psk;
    }
    public String getSecurity() {
        return security;
    }
    public void setSecurity(String security) {
        this.security = security;
    }
    @Override
    public int compareTo(Network another) {
        if(psk!=null&&another.psk==null)
            return -1;
        if(psk==null&&another.psk!=null)
            return 1;
        return 0;
    }



}
