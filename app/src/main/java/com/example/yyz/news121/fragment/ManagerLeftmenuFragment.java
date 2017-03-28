package com.example.yyz.news121.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.ManagerMainActivity;
import com.example.yyz.news121.base.BaseFragment;
import com.example.yyz.news121.pager.ManagerPager;
import com.example.yyz.news121.utils.DensityUtil;

import java.util.List;

/**左侧菜单fragment
 * Created by yyz on 2017/1/27.
 */
public class ManagerLeftmenuFragment extends BaseFragment{
    private ListView listview;
    private List<String> managerdata;
    private LeftAdapter mAdapter;
    private View view;
    public   int selectposition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.left_menu,container,false);
        initView();
        initData();
      /*  mAdapter=new LeftAdapter();
        listview.setAdapter(mAdapter);*/

        return view;
    }

    @Override
    public View initView() {
        listview= (ListView) view.findViewById(R.id.listleft);
        /*listview=new ListView(context);*/
        listview.setCacheColorHint(Color.TRANSPARENT);
        listview.setDividerHeight(0);
        listview.setPadding(0, DensityUtil.dip2px(context,40),0,0);
        //屏蔽按下变色效果
        listview.setSelector(android.R.color.transparent);
        listview.setOnItemClickListener(new MyOnClickListener());
        return listview;
    }
    public void initData(){
        super.initData();
    }

    public void setData(List<String> managerdata) {
        this.managerdata=managerdata;
        mAdapter=new LeftAdapter();
        listview.setAdapter(mAdapter);

        //设置默认的页面
        setSwichePager(selectposition);
    }

    class LeftAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return managerdata.size();
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
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.left_item,null);
            }
            TextView title= (TextView) view.findViewById(R.id.menu_title);
             title.setText(managerdata.get(i));
            if(selectposition==i){
                title.setEnabled(true);
            }else {
                title.setEnabled(false);
            }

            return view;
        }
    }
class MyOnClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //记录位置
        selectposition=i;
        mAdapter.notifyDataSetChanged();

        //关闭左侧菜单
        ManagerMainActivity managerMainActivity= (ManagerMainActivity) context;
        managerMainActivity.getSlidingMenu().toggle();//关->开；

       setSwichePager(i);
    }
}

    //切换到不同的页面
    private void setSwichePager(int position) {
        ManagerMainActivity managerMainActivity= (ManagerMainActivity) context;
        //切换页面
        ManagerFragment managerFragment= managerMainActivity.getManagerFragment();
        ManagerPager managerPager =managerFragment.getManagerPager();
        //切换对应详情页面
        managerPager.switchPager(position);
    }
}
