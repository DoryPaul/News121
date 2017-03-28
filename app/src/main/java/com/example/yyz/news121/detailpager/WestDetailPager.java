package com.example.yyz.news121.detailpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yyz.news121.R;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.EastBean;
import com.example.yyz.news121.utils.BitmapUtils;
import com.example.yyz.news121.utils.CacheUtils;
import com.example.yyz.news121.utils.Constants;
import com.example.yyz.news121.utils.NetCacheUtils;
import com.example.yyz.news121.view.RefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**西部详情页面
 * Created by yyz on 2017/2/4.
 */
public class WestDetailPager extends DetailBasePager {
    private List<EastBean> westdata=new ArrayList<>();
    private RefreshListView listview;
    private WestAdapter westAdapter;
    public WestDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        //LogUtil.e("西部被初始化");
        listview=new RefreshListView(mcontext);
        //显示头部出现分割线
        listview.setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        listview.setFooterDividersEnabled(false);

        //监听控件刷新
        listview.setOnRefreshListener(new MyOnRefreshListener());
        return listview;
    }

    public void initData(){
        super.initData();
      //  LogUtil.e("西部数据初始化");
        getDataFromNet();
        //得到缓存
        String saveJson = CacheUtils.getString(mcontext, Constants.west_url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
    }
    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void OnPullDownRefresh() {
            getDataFromNet();
        }
    }
    private void getDataFromNet() {
        RequestParams params=new RequestParams(Constants.west_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(mcontext, Constants.west_url, result);
                processData(result);
                listview.onRefreshFinish(true);
              //  LogUtil.e("联网成功");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //LogUtil.e("联网成功ex"+ex.getMessage());
                listview.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //LogUtil.e("联网成功cex"+cex.getMessage());
            }

            @Override
            public void onFinished() {

              //  LogUtil.e("联网成功finish");
            }
        });
    }
    private void processData(String result) {
        westdata.clear();
        try {
            JSONArray array=new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object=array.getJSONObject(i);
                String teamname=object.getString("teamname");
                String teamiconurl=object.getString("teamiconurl");
                String win=object.getString("win");
                String lost=object.getString("lost");
                String local=object.getString("local");
                westdata.add(new EastBean(teamname,teamiconurl,win,lost,local));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        westAdapter=new WestAdapter(mcontext,westdata);
        listview.setAdapter(westAdapter);
    }

    private class WestAdapter extends BaseAdapter {
        private Context mcontext;
        private  List<EastBean> list;
        private BitmapUtils bitmapUtils;
        private Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case NetCacheUtils.SUCCESS:
                        Bitmap bitmap= (Bitmap) msg.obj;
                        int position=msg.arg1;
                        //Log.e("TAG","图片请求成功"+position);
                        if(listview!=null&&listview.isShown()){
                            ImageView imageView= (ImageView) listview.findViewWithTag(position);
                            if(imageView!=null&&bitmap!=null){
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                        break;
                    case NetCacheUtils.FAIL:
                        position=msg.arg1;
                        //  Log.e("TAG","图片请求失败"+position);
                        break;
                }
            }
        };
        public WestAdapter(Context context, List<EastBean> list){
            this.mcontext=context;
            this.list=list;
            bitmapUtils=new BitmapUtils(handler);
        }
        @Override
        public int getCount() {
            return westdata.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(view==null){
                viewHolder=new ViewHolder();
                view = View.inflate(mcontext, R.layout.east_item, null);
                viewHolder.teamicon= (ImageView) view.findViewById(R.id.teamicon);
                viewHolder.win= (TextView) view.findViewById(R.id.win);
                viewHolder.lost= (TextView) view.findViewById(R.id.lost);
                viewHolder.teamname= (TextView) view.findViewById(R.id.teamname);
                view.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }
            EastBean eastBean=westdata.get(i);
            String url= (String) eastBean.getTeamiconurl();
            viewHolder.teamicon.setTag(i);
            Bitmap bitmap=bitmapUtils.getBitmap(url,i);
            if(bitmap!=null){
                //图片来自内存或者本地
                viewHolder.teamicon.setImageBitmap(bitmap);
            }
           /* Glide
                    .with(mcontext)
                    .load(eastBean.getTeamiconurl())
                    .centerCrop()
                    .crossFade()
                    .into(viewHolder.teamicon);*/
            viewHolder.teamname.setText(eastBean.getTeamname());
            viewHolder.win.setText(eastBean.getWin());
            viewHolder.lost.setText(eastBean.getLost());
            return view;
        }
    }
    static class ViewHolder{
        ImageView teamicon;
        TextView win, lost, teamname;
    }
}
