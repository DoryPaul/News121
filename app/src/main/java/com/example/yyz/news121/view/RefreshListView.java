package com.example.yyz.news121.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.yyz.news121.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yyz on 2017/2/10.
 * 自定义下拉刷新控件
 */
public class RefreshListView extends ListView {
    private ImageView iv_red_arrow;
    private ProgressBar pb_status;
    private TextView tv_status, tv_time;
    private Animation upAnimation;
    private Animation downAnimation;
    //下拉状态
    private static final int PULL_DOWN_REFRESH = 1;
    //松手状态
    private static final int RELEASW_REFRESH = 2;
    //正在刷新状态
    private static final int REFRESHING = 3;
    //当前状态
    private int currentState = PULL_DOWN_REFRESH;

    private int headViewHeight;//下拉刷新控件的高
    //下拉刷新的控件
    private View ll_pull_down;
    //整个head
    private LinearLayout headView;
    //顶部轮播图部分
    private View topView;
    //listview在Y轴的坐标
    private int listViewOnScreenY = -1;
    //加载更多布局
    private View footView;
    //上拉加载控件高度
    private int footViewHeight;
    //是否加载更多
    private boolean isLoadMore = false;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        footView = View.inflate(context, R.layout.refresh_foot, null);
        footView.measure(0, 0);
        footViewHeight = footView.getMeasuredHeight();
        /**
         *
         View.setPadding(0,-控件高，0,0）；//完成隐藏
         View.setPadding(0,0，0,0）；//完成显示
         View.setPadding(0,控件高，0,0）；//两倍完全显示
         */
        footView.setPadding(0, -footViewHeight, 0, 0);

        addFooterView(footView);


        //监听滑动到ListView的最后一个可见的item
        //setOnScrollListener(new MyOnScrollListener());
    }

    class MyOnScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //静止，或者惯性滚动，并且是最后一个可见的时候
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                //到最后一个可见
                if (getLastVisiblePosition() == getAdapter().getCount() - 1 && !isLoadMore) {

                    //显示加载更多控件
                    footView.setPadding(10, 10, 10, 10);

                    //设置状态
                    isLoadMore = true;

                    //回调接口
                   /* if (mOnRefreshListener != null) {
                        mOnRefreshListener.onLoadMore();
                    }*/
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        }
    }

    //初始化动画
    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        headView = (LinearLayout) View.inflate(context, R.layout.refresh, null);
        ll_pull_down = headView.findViewById(R.id.ll_pull_down);
        iv_red_arrow = (ImageView) headView.findViewById(R.id.iv_red_arrow);
        pb_status = (ProgressBar) headView.findViewById(R.id.pb_status);
        tv_status = (TextView) headView.findViewById(R.id.tv_status);
        tv_time = (TextView) headView.findViewById(R.id.tv_time);
        ll_pull_down.measure(0, 0);//测量
        headViewHeight = ll_pull_down.getMeasuredHeight();

        ll_pull_down.setPadding(0, -headViewHeight, 0, 0);
        addHeaderView(headView);//以头的方式添加到listview中
    }

    private float startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录起始坐标
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == 0) {
                    startY = ev.getY();
                }
                //判断顶部轮播图书否完全显示
                boolean isDisplayTopView = isDisplayTopView();//完全显示就是下拉刷新
                if (!isDisplayTopView) {
                    //上拉刷新
                    break;
                }
                //记录结束坐标
                float endY = ev.getY();
                //计算偏移量
                float distantY = endY - startY;

                if (distantY > 0) {
                    //向下滑动
                    int paddingTop = (int) (-headViewHeight + distantY);
                    if (paddingTop < 0 && currentState != PULL_DOWN_REFRESH) {
                        //下拉刷新
                        currentState = PULL_DOWN_REFRESH;
                        //更新状态
                        refreshStatus();
                    } else if (paddingTop > 0 && currentState != RELEASW_REFRESH) {
                        //手势刷新
                        currentState = RELEASW_REFRESH;
                        //更新状态
                        refreshStatus();
                    }
                    ll_pull_down.setPadding(0, paddingTop, 0, 0);

                }
                break;
            case MotionEvent.ACTION_UP:
                startY = 0;
                if (currentState == PULL_DOWN_REFRESH) {
                    ll_pull_down.setPadding(0, -headViewHeight, 0, 0);
                } else if (currentState == RELEASW_REFRESH) {
                    currentState = REFRESHING;

                    ll_pull_down.setPadding(0, 0, 0, 0);
                    refreshStatus();
                    //回调接口
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.OnPullDownRefresh();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshStatus() {
        switch (currentState) {
            case PULL_DOWN_REFRESH://下拉刷新
                tv_status.setText("下拉刷新");
                iv_red_arrow.startAnimation(downAnimation);
                break;
            case RELEASW_REFRESH://松手刷新
                tv_status.setText("松手刷新。。。");
                iv_red_arrow.startAnimation(upAnimation);
                break;
            case REFRESHING://正在刷新
                pb_status.setVisibility(VISIBLE);
                iv_red_arrow.setVisibility(GONE);
                iv_red_arrow.clearAnimation();
                tv_status.setText("正在刷新。。。");
                break;
        }
    }

    //判断顶部轮播图是否完全显示
    private boolean isDisplayTopView() {
        if (topView != null) {
            //得到listview在屏幕上Y轴的坐标
            int[] location = new int[2];
            if (listViewOnScreenY == -1) {
                this.getLocationOnScreen(location);
                listViewOnScreenY = location[1];
            }
            //得到顶部轮播图在Y轴的坐标
            topView.getLocationOnScreen(location);
            int topViewOnScreenY = location[1];

            return listViewOnScreenY <= topViewOnScreenY;
        } else {
            return true;
        }
    }

    //顶部轮播图部分
    public void addTopNews(View topView) {
        this.topView = topView;
        if (topView != null && headView != null) {
            headView.addView(topView);//添加顶部轮播图
        }
    }

    //把下拉刷新状态恢复成初始状态
    public void onRefreshFinish(boolean success) {
        iv_red_arrow.clearAnimation();
        iv_red_arrow.setVisibility(VISIBLE);
        pb_status.setVisibility(GONE);
        ll_pull_down.setPadding(0, -headViewHeight, 0, 0);
        tv_status.setText("下拉刷新");
        currentState = PULL_DOWN_REFRESH;
        if (success) {
            tv_time.setText("更新时间为:" + getSystemTime());
        }
    }

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }


    //视图刷新监听
    public interface OnRefreshListener {
        public void OnPullDownRefresh();

        //加载更多时候回掉的方法
       // public void onLoadMore();
    }

    private OnRefreshListener mOnRefreshListener;

    /**
     * 监听视图刷新
     */
    public void setOnRefreshListener(OnRefreshListener l) {
        this.mOnRefreshListener = l;
    }
}
