package com.example.yyz.news121.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yyz.news121.R;
import com.example.yyz.news121.bean.PlayerBean;
import com.example.yyz.news121.utils.BitmapUtils;
import com.example.yyz.news121.utils.CacheUtils;
import com.example.yyz.news121.utils.Constants;
import com.example.yyz.news121.utils.NetCacheUtils;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class TukuActivity extends AppCompatActivity implements OnMenuItemClickListener{
    public RecyclerView recyclerView;
    private List<PlayerBean> list;
    private SimpleAdapter adapter;
    private StaggeredAdapter staadapter;
    private SwipeRefreshLayout refreshLayout;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuku);
        initData();
        initView();
        initSwipeRefreshLayout();
        fragmentManager = getSupportFragmentManager();
        //initToolbar();
        initMenuFragment();
        adapter=new SimpleAdapter(this,list);
        staadapter=new StaggeredAdapter(this,list);
        recyclerView.setAdapter(adapter);



        //设置布局管理
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.setItemAnimator(new DefaultItemAnimator());

     /*adapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(TukuActivity.this,position+"duan",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });*/
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }
    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("ListView");
        send.setResource(R.drawable.icn_1);

        MenuObject like = new MenuObject("Gradview");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icn_2);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("Hor_gradview");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.icn_3));
        addFr.setDrawable(bd);

        MenuObject addFav = new MenuObject("瀑布流");
        addFav.setResource(R.drawable.icn_4);

        MenuObject block = new MenuObject("关闭");
        block.setResource(R.drawable.icn_5);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        menuObjects.add(block);
        return menuObjects;
    }
   /* private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationIcon(R.drawable.btn_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarTextView.setText("Samantha");
    }*/

    private void initSwipeRefreshLayout() {
        //设置刷新图标颜色（单个或多个）
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_red_dark);

        //设置刷新控件的背景
       // refreshLayout.setProgressBackgroundColorSchemeResource(getResources().getColor(android.R.color.holo_orange_light));

        //设置刷新的拖动距离

        refreshLayout.setDistanceToTriggerSync(500);
        //设置刷新空间的类型：默认和大
       // refreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //下拉刷新的监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      getDataFromNet();
                      refreshLayout.setRefreshing(false);
                      recyclerView.scrollToPosition(0);
                  }
              },3000);
            }
        });

    }
    private void initView() {
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.refreshLayout);


    }

    private void initData() {
        list=new ArrayList<PlayerBean>();
        getDataFromNet();
        //得到缓存
        String saveJson = CacheUtils.getString(this, Constants.player_url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
    }
    private void getDataFromNet() {
        OkHttpUtils
                .get()
                .url(Constants.player_url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        CacheUtils.putString(TukuActivity.this, Constants.player_url, response);
                        processData(response);
                    }
                });
    }

    private void processData(String result) {
        list.clear();
        try {
            JSONArray array=new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object=array.getJSONObject(i);
                String id=object.getString("id");
                String playericonurl=object.getString("playericonurl");
                String name=object.getString("name");
                String weizhi=object.getString("weizhi");
                String qiuduiid=object.getString("qiuduiid");
                String shengao=object.getString("shengao");
                String tizhong=object.getString("tizhong");
                String shengti=object.getString("shengti");
                String gongzi=object.getString("gongzi");
                list.add(new PlayerBean(id,playericonurl,name,weizhi,qiuduiid,shengao,tizhong,shengti,gongzi));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
           /* case R.id.action_listview:
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.action_gradview:
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(this,3));
                break;
            case R.id.action_hor_gradview:
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.HORIZONTAL));
                break;
            case R.id.action_staggered:
                // Intent intent =new Intent(this,StaggeredActivity.class);
                // startActivity(intent);
                recyclerView.setAdapter(staadapter);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
                break;
            case R.id.action_add:
                adapter.addData(1);
                break;
            case R.id.action_delete:
                adapter.deleteData(1);
                break;
*/
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
 /*   @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            finish();
        }
    }*/
    @Override
    public void onMenuItemClick(View clickedView, int position) {
        //Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
       switch (position){
           case 1:
               recyclerView.setAdapter(adapter);
               recyclerView.setLayoutManager(new LinearLayoutManager(this));
               break;
           case 2:
               recyclerView.setAdapter(adapter);
               recyclerView.setLayoutManager(new GridLayoutManager(this,3));
               break;
           case 3:
               recyclerView.setAdapter(adapter);
               recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.HORIZONTAL));
               break;
           case 4:
               recyclerView.setAdapter(staadapter);
               recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
               break;
       }
    }


    class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.MyViewHolder> {
        private LayoutInflater minflater;
        private Context mcontext;
        private  List<PlayerBean> list;
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
                        if(recyclerView!=null&&recyclerView.isShown()){
                            ImageView imageView= (ImageView) recyclerView.findViewWithTag(position);
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


     /* public interface  OnItemClickListener{
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }
        private OnItemClickListener onItemClickListener;
        public void  setOnItemClickListener(OnItemClickListener listener){
            this.onItemClickListener=listener;
        }*/
        public  SimpleAdapter(Context context, List<PlayerBean> list){
            this.mcontext=context;
            this.list=list;
            minflater=LayoutInflater.from(context);
            bitmapUtils=new BitmapUtils(handler);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view=minflater.inflate(R.layout.item_simple_adapter,viewGroup,false);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
            PlayerBean playerBean=list.get(position);
            String url= (String) playerBean.getPlayericonurl();

            myViewHolder.tv.setTag(position);
            Bitmap bitmap=bitmapUtils.getBitmap(url,position);
            if(bitmap!=null){
                //图片来自内存或者本地
                myViewHolder.tv.setImageBitmap(bitmap);
            }
            ViewGroup.LayoutParams lp=myViewHolder.itemView.getLayoutParams();
            lp.height=200;
            myViewHolder.itemView.setLayoutParams(lp);
        }
        public void addData(int position){
            notifyItemInserted(position);
        }
        public void deleteData(int position){
            list.remove(position);
            notifyItemRemoved(position);
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView tv;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv= (ImageView) itemView.findViewById(R.id.tv);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlayerBean playerBean=list.get(getLayoutPosition());
                        Intent intent=new Intent(TukuActivity.this,TukuDetailActivity.class);
                        intent.putExtra("name",playerBean.getName());
                        intent.putExtra("shengao",playerBean.getShengao());
                        intent.putExtra("tizhong",playerBean.getTizhong());
                        intent.putExtra("weizhi",playerBean.getWeizhi());
                        intent.putExtra("playerurl",playerBean.getPlayericonurl());
                        intent.putExtra("qiuduiid",playerBean.getQiuduiid());
                        intent.putExtra("shengri",playerBean.getShengti());
                        intent.putExtra("gongzi",playerBean.getGongzi());
                        mcontext.startActivity(intent);
                    }
                });
            }

        }
    }
    public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.MyViewHolder> {
        private LayoutInflater minflater;
        private Context mcontext;
        private List<PlayerBean> list;
        private BitmapUtils bitmapUtils;
        private Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case NetCacheUtils.SUCCESS:
                        Bitmap bitmap= (Bitmap) msg.obj;
                        int position=msg.arg1;
                      //  Log.e("TAG","图片请求成功"+position);
                        if(recyclerView!=null&&recyclerView.isShown()){
                            ImageView imageView= (ImageView) recyclerView.findViewWithTag(position);
                            if(imageView!=null&&bitmap!=null){
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                        break;
                    case NetCacheUtils.FAIL:
                        position=msg.arg1;
                       // Log.e("TAG","图片请求失败"+position);
                        break;
                }
            }
        };
        private  List<Integer> height;
        public  StaggeredAdapter(Context context, List<PlayerBean> list){
            this.mcontext=context;
            this.list=list;
            minflater=LayoutInflater.from(context);
            bitmapUtils=new BitmapUtils(handler);

            height=new ArrayList<Integer>();
            for(int i=0;i<list.size();i++){
                height.add((int) (200+Math.random()*300));
            }
        }


        @Override
        public int getItemCount() {
            return list.size();
        }
        @Override
        public StaggeredAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view=minflater.inflate(R.layout.item_simple_adapter,viewGroup,false);
            StaggeredAdapter.MyViewHolder myViewHolder=new StaggeredAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final StaggeredAdapter.MyViewHolder myViewHolder, int position) {
            ViewGroup.LayoutParams lp=myViewHolder.itemView.getLayoutParams();
            lp.height=height.get(position);
            myViewHolder.itemView.setLayoutParams(lp);
            PlayerBean playerBean=list.get(position);

            String url= (String) playerBean.getPlayericonurl();
            myViewHolder.tv.setTag(position);
            Bitmap bitmap=bitmapUtils.getBitmap(url,position);
            if(bitmap!=null){
                //图片来自内存或者本地
                myViewHolder.tv.setImageBitmap(bitmap);
            }
       /* Glide
                .with(mcontext)
                .load(playerBean.getPlayericonurl())
                .centerCrop()
                .crossFade()
                .into(myViewHolder.tv);*/
           /* if(onItemClickListener!=null){
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int layoutPosition = myViewHolder.getLayoutPosition();
                        onItemClickListener.onItemClick(myViewHolder.itemView,layoutPosition);
                    }
                });

                //longclick
                myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int layoutPosition = myViewHolder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(myViewHolder.itemView,layoutPosition);
                        return false;
                    }
                });
            }*/
        }

         class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView tv;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv= (ImageView) itemView.findViewById(R.id.tv);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       PlayerBean playerBean=list.get(getLayoutPosition());
                        Intent intent=new Intent(TukuActivity.this,TukuDetailActivity.class);
                        intent.putExtra("name",playerBean.getName());
                        intent.putExtra("shengao",playerBean.getShengao());
                        intent.putExtra("tizhong",playerBean.getTizhong());
                        intent.putExtra("weizhi",playerBean.getWeizhi());
                        intent.putExtra("playerurl",playerBean.getPlayericonurl());
                        intent.putExtra("qiuduiid",playerBean.getQiuduiid());
                        intent.putExtra("shengri",playerBean.getShengti());
                        intent.putExtra("gongzi",playerBean.getGongzi());
                        mcontext.startActivity(intent);
                    }
                });
            }

        }
    }
}
