package com.wyp.seewifipwd;

/**
 * Created by WYP on 2015/1/21.
 */
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author water3
 * 解析字符串
 */
public class Parser {

  //static Pattern NETWORK = Pattern.compile("network\\=\\{\\s+ssid\\=(.+?)(\\s+psk\\=\"(.+?)\")?");
    static Pattern NETWORK = Pattern.compile("network\\=\\{\\s+ssid\\=\"(.+?)\"(\\s+psk\\=\"(.+?)\")?");

    public final static String wpaString="config_methods=physical_display virtual_push_button keypad" +
            "network={    ssid=\"CMCC-EDU\" key_mgmt=NONE}" +
            "network={   ssid=homechen psk=\"bl45666gogo\"  key_mgmt=WPA-PSK    priority=3  disabled=1}" +
            "network={   ssid=\"zhutou1\"   psk=\"5461\" key_mgmt=WPA-PSK}" +
            "network={   ssid=\"zhongmai\" psk=\"545611\"    proto=WPA   key_mgmt=WPA-PSK    group=CCMP TKIP    priority=4}";

    /**
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        List<Network> wifis=getNetworks(new String(wpaString.getBytes("ISO-8859-1"),"UTF-8"));
        for (Network network : wifis) {
            System.out.println(network.getSsid()+" "+network.getPsk());
        }

    }

    public static List<Network> getNetworks(String wpaString){
        Matcher matcher=null;
        try {
            matcher = NETWORK.matcher(new String(wpaString.getBytes("ISO-8859-1"),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<Network> netList = new ArrayList<Network>();
        while (true)
        {
            if (!matcher.find())
            {
                Collections.sort(netList);
                return netList;
            }
            Network network = new Network();
            network.setSsid(matcher.group(1));
            network.setPsk(matcher.group(3));

            netList.add(network);
        }
    }



}