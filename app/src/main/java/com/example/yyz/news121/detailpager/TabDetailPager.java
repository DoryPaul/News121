package com.example.yyz.news121.detailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.PlayerDetailActivity;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.AllBean;
import com.example.yyz.news121.bean.TabDetailBean;
import com.example.yyz.news121.utils.BitmapUtils;
import com.example.yyz.news121.utils.CacheUtils;
import com.example.yyz.news121.utils.MemoryCacheUtils;
import com.example.yyz.news121.utils.NetCacheUtils;
import com.example.yyz.news121.view.HorizontalScrollViewPager;
import com.example.yyz.news121.view.RefreshListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/2/4.
 */
public class TabDetailPager extends DetailBasePager {

    public static final String READ_ARRAY_ID = "read_array_id";
    private HorizontalScrollViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private RefreshListView listview;
    private int prePoint;
    private InteracHandler handler;

    TabDetailBean tab;
    private TabDetailAdapter adapter;
    public List<TabDetailBean> tabdata = new ArrayList<>();

    //页签页面数
    public List<AllBean>  alldetailpagerdata=new ArrayList<>();


    private int po;
    //按页面得到url
    public String url[]=new String[5];;
   // private boolean isLoadMore=false;
    private  boolean isDragging=false;


    public TabDetailPager(Context context, List<AllBean> chuanruData) {
        super(context);
        this.alldetailpagerdata=chuanruData;
    }


    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.tab_detail_pager, null);
        listview = (RefreshListView) view.findViewById(R.id.listview);
        View topView = View.inflate(mcontext, R.layout.top_view, null);
        listview.setDivider(mcontext.getDrawable(R.drawable.list_item));
        //显示头部出现分割线
        listview.setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        listview.setFooterDividersEnabled(false);

        viewpager = (HorizontalScrollViewPager) topView.findViewById(R.id.tabViewpager);
        tv_title = (TextView) topView.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) topView.findViewById(R.id.ll_point_group);

        //监听页面的变化
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        //把顶部的模块以头的方式加载到listview中
        // listview.addHeaderView(topView);
        listview.addTopNews(topView);
        //监听控件刷新
        listview.setOnRefreshListener(new MyOnRefreshListener());
        //设置监听某一条item
        listview.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    public void setPosition(int position) {
        this.po=position;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            int realPosition=i-1;
            TabDetailBean all=tabdata.get(realPosition);

            String realid=CacheUtils.getString(mcontext, READ_ARRAY_ID);
             if(!realid.contains(all.getId()+"")){
                 //保持
                 CacheUtils.putString(mcontext,READ_ARRAY_ID,realid+all.getId()+",");
                 //更新适配器
                 adapter.notifyDataSetChanged();
             }
            Intent intent=new Intent(mcontext,PlayerDetailActivity.class);
            String url1="http://192.168.133.2/NBA/new.php/";
                    intent.putExtra("url",url1);
            mcontext.startActivity(intent);
        }
    }
    class MyOnRefreshListener implements RefreshListView.OnRefreshListener{

        @Override
        public void OnPullDownRefresh() {

            getDataFromNet();
        }

    /*    @Override
        public void onLoadMore() {
            String more="";
            if(TextUtils.isEmpty(more)){
                //没有更多数据
                Toast.makeText(mcontext,"没有更多数据",Toast.LENGTH_SHORT).show();
                isLoadMore=false;
                listview.onRefreshFinish(false);
            }else{
                getDataFromNet();
            }
        }*/
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //吧之前的点设置位false
            ll_point_group.getChildAt(prePoint).setEnabled(false);
            //吧现在的点设置高亮
            ll_point_group.getChildAt(position).setEnabled(true);
            prePoint = position;
        }

        @Override
        public void onPageSelected(int position) {
            //设置文本
            tv_title.setText(tabdata.get(position).getName());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state==ViewPager.SCROLL_STATE_DRAGGING){
                handler.removeCallbacksAndMessages(null);
                isDragging=true;
            }else if(state==ViewPager.SCROLL_STATE_IDLE){
                isDragging=false;
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunnable(),4000);
            }else if(state==ViewPager.SCROLL_STATE_SETTLING&&isDragging){
                isDragging=false;
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunnable(),4000);
            }

        }
    }

    public void initData() {
        super.initData();

for (int i=0;i<alldetailpagerdata.size();i++) {
    url[i]=alldetailpagerdata.get(i).getDataurl();
}
        //得到缓存
        String saveJson = CacheUtils.getString(mcontext,url[po]);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();
    }

    private void getDataFromNet() {

            RequestParams params = new RequestParams(url[po]);
            params.setConnectTimeout(4000);//设置超时

            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {

                    //LogUtil.e("请求数据成功" + result);
                    //缓存
                    CacheUtils.putString(mcontext, url[po], result);
                    processData(result);
                    listview.onRefreshFinish(true);

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                   // LogUtil.e("请求数据失败" + ex.getMessage());
                    listview.onRefreshFinish(false);
                }

                @Override
                public void onCancelled(CancelledException cex) {
                   // LogUtil.e("请求数据取消" + cex.getMessage());
                }

                @Override
                public void onFinished() {
                    //LogUtil.e("请求数据结束");
                }
            });

    }

    private void processData(String result) {
        tabdata.clear();
        try {
            JSONArray jsonArr = new JSONArray(result);
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject object = jsonArr.getJSONObject(i);
                String id=object.getString("id");
               String blengqiudui=object.getString("blengqiudui");
                String weizhi = object.getString("weizhi");
                String name = object.getString("name");
                String playericonurl = object.getString("qiuyuanpicurl");
                String data = object.getString("data");
                tabdata.add(new TabDetailBean(id,blengqiudui,weizhi,name,playericonurl,data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //设置顶部数据
        viewpager.setAdapter(new TabDetailPagerAdapter());
        ll_point_group.removeAllViews();//移除所有的点

        //根据页面个数设置点的个数
        for (int i = 0; i < tabdata.size(); i++) {
            ImageView point = new ImageView(mcontext);
            point.setBackgroundResource(R.drawable.point_select);

            //设置点间距
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(5), DensityUtil.dip2px(5));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(10);
            }
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            point.setLayoutParams(params);

            //添加红点
            ll_point_group.addView(point);
                tv_title.setText(tabdata.get(prePoint).getName());

            //设置listview的数据（适配器）
            adapter = new TabDetailAdapter(mcontext,tabdata);
            listview.setAdapter(adapter);
        }
        //每隔四秒循环一次
        if(handler==null){
            handler=new InteracHandler();
        }
        handler.removeCallbacksAndMessages(null);//移除所有的消息和回掉
        handler.postDelayed(new MyRunnable(),4000);

    }

    class InteracHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int item=(viewpager.getCurrentItem()+1)%tabdata.size();
            viewpager.setCurrentItem(item);

            handler.postDelayed(new MyRunnable(),4000);
        }
    }
    class MyRunnable implements   Runnable{
        @Override
        public void run() {

            handler.sendEmptyMessage(0);
        }
    }

    class TabDetailAdapter extends BaseAdapter {
        private LayoutInflater minflater;
        private Context mcontext;
        private  List<TabDetailBean> list;
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
        public TabDetailAdapter(Context context, List<TabDetailBean> list){
            this.mcontext=context;
            this.list=list;
            minflater=LayoutInflater.from(context);
            bitmapUtils=new BitmapUtils(handler);
        }
        @Override
        public int getCount() {
            return tabdata.size();
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
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            String text[]={"场均得分 ","场均助攻 ","场均盖帽 ","场均篮板 ","场均抢断 "};
            if (convertview == null) {
                convertview = View.inflate(mcontext, R.layout.item_tabdatailpager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertview.findViewById(R.id.iv_icon);
                viewHolder.player_name = (TextView) convertview.findViewById(R.id.player_name);
                viewHolder.player_weizhi = (TextView) convertview.findViewById(R.id.player_weizhi);
                viewHolder.player_team = (TextView) convertview.findViewById(R.id.player_team);
                viewHolder.tabdata= (TextView) convertview.findViewById(R.id.tabdata);
                convertview.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertview.getTag();
            }
            //根据位置去对应的数据
            for (int j=0;j<4;j++) {
                if(j==0) {
                  tab = tabdata.get(i);
                    String playerurl = tab.getPlayericonurl();
                    viewHolder.iv_icon.setTag(i);
                    Bitmap bitmap=bitmapUtils.getBitmap(playerurl,i);
                    if(bitmap!=null){
                        //图片来自内存或者本地
                        viewHolder.iv_icon.setImageBitmap(bitmap);
                    }
                   /*Glide
                            .with(mcontext)
                            .load(playerurl)
                            .centerCrop()
                            .crossFade()
                            .into(viewHolder.iv_icon);*/
                    viewHolder.player_name.setText(tab.getName());
                    viewHolder.player_team.setText(tab.getBlengqiudui());
                    viewHolder.player_weizhi.setText(tab.getWeizhi());
                    viewHolder.tabdata.setText(text[po]+tab.getData());
                }

            }
            String realid=CacheUtils.getString(mcontext, READ_ARRAY_ID);
            if(realid.contains(tab.getId()+"")){
                //变成灰色
                viewHolder.player_name.setTextColor(Color.GRAY);
            }else {
                //默认黑色
                viewHolder.player_name.setTextColor(Color.BLACK);
            }

            return convertview;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView player_name, player_weizhi, player_team,tabdata;
    }

    class TabDetailPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return tabdata.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mcontext);

                String picurl = tabdata.get(position).getPlayericonurl();
                Glide
                        .with(mcontext)
                        .load(picurl)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .crossFade()
                        .into(imageView);
                container.addView(imageView);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                   switch (motionEvent.getAction()){
                       case  MotionEvent.ACTION_DOWN://按下
                           handler.removeCallbacksAndMessages(null);
                           break;
                       case MotionEvent.ACTION_MOVE://移动

                           break;
                       case MotionEvent.ACTION_UP://离开
                           handler.postDelayed(new MyRunnable(),4000);
                           break;
                   }
                    return true;//若要设置点击事件则设置为false
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
