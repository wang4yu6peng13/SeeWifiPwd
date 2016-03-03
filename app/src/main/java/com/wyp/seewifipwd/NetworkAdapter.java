package com.wyp.seewifipwd;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by WYP on 2015/1/21.
 */
public class NetworkAdapter extends BaseAdapter{

    private List<Network> dataList;
    //    private List<Network> mFilteredArrayList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String connectingSsid;

    private int GREY=0xFF888888;
    private int BLUE=0xFF03A9F4;

    public NetworkAdapter(Context context,List<Network> arrayList) {
        dataList = arrayList;
        mLayoutInflater=LayoutInflater.from(context);
        mContext=context;
//        mFilteredArrayList=new ArrayList<Network>();
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //使用convertView和ViewHolder提升listview性能
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.list_item, null);
        }
        else
        {
            holder  = (ViewHolder) convertView.getTag() ;
        }
        if(holder==null){
            holder = new ViewHolder();
            holder.ssidTv=(TextView) convertView.findViewById(R.id.ssidTv);
            holder.pskTv=(TextView) convertView.findViewById(R.id.pskTv);
//             holder.shareBtn=(Button) convertView.findViewById(R.id.shareBtn);
//             holder.divider=convertView.findViewById(R.id.divider);

            convertView.setTag(holder) ;
        }

//         holder.shareBtn.setTag(position);//记录当前位置，分享按钮那里取出来
        holder.ssidTv.setText(dataList.get(position).getSsid());
        //如果密码为空
        if(dataList.get(position).getPsk()==null)
        {
            holder.pskTv.setText(mContext.getResources().getString(R.string.nopwd));
            holder.pskTv.setTextColor(GREY);
//             holder.shareBtn.setVisibility(View.GONE);
//             holder.divider.setVisibility(View.GONE);

        }
        else {
            holder.pskTv.setText(dataList.get(position).getPsk());
            holder.pskTv.setTextColor(Color.BLACK);
//             holder.shareBtn.setVisibility(View.VISIBLE);
//             holder.divider.setVisibility(View.VISIBLE);
        }

        //如果当前位置为正在连接的

        if(position==0 &&dataList.get(position).getSsid().equals(connectingSsid))
        {
            //            holder.shareBtn.setTextColor(Color.WHITE);
            //            holder.shareBtn.setBackgroundResource(R.drawable.button_connecting);
            convertView.setBackgroundColor(Color.parseColor("#E7F4FA"));
            holder.pskTv.setTextColor(BLUE);
        }else
        {
//             holder.shareBtn.setTextColor(BLUE);
            //            holder.shareBtn.setBackgroundResource(R.drawable.button_share);
            convertView.setBackgroundColor(Color.WHITE);
        }

 /*
         holder.shareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int currentPos=(Integer) v.getTag();
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");  //分享的数据类型
                intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.share_wifi_psk));  //主题
                intent.putExtra(Intent.EXTRA_TEXT,
                        dataList.get(currentPos).getSsid()+mContext.getResources().getString(R.string.wifi_psk_is)+dataList.get(currentPos).getPsk());  //内容
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(Intent.createChooser(intent, mContext.getResources().getString(R.string.share_wifi_psk)));  //目标应用选择对话框的标题
//               Toast.makeText(mContext, currentPos+"", Toast.LENGTH_SHORT).show();
            }
        });
        */
        return convertView;
    }

    public String getConnectingSsid() {
        return connectingSsid;
    }
    public void setConnectingSsid(String connectingSsid) {
        this.connectingSsid = connectingSsid;
    }

}
class ViewHolder{
    TextView ssidTv,pskTv;
 //   Button shareBtn;
//    View divider;
}



