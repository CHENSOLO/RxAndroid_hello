package com.example.chensolo.rxandroid_hello;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.chensolo.rxandroid_hello.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Butterknife 专门为Android View设计的绑定注解，专业解决各种findViewId
 *recyclerView 设置布局管理器方法
 * 在竖值滑动时想要刷新页面可以用SwipeRefreshLayout来实现,通过设置OnRefreshListener来监听界面的滑动从而实现刷新
 */
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.pull_down_srl)
    SwipeRefreshLayout mPullDownSRL;
    @Bind(R.id.app_list_rv)
    RecyclerView mAppListRV;
    private List<AppInfo> mAppInfoList; //存放数据组
    private AppListAdapter mAppListAdapter;//数据适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //使用线性布局管理器参数为MainActivity环境
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mAppListRV.setLayoutManager(linearLayoutManager);
        //新建一个数组
        mAppInfoList = new ArrayList<>();
        //新建一个适配器里面存放数组数据
        mAppListAdapter = new AppListAdapter(mAppInfoList);
        //绑定数组
        mAppListRV.setAdapter(mAppListAdapter);
       //设置监听
        mPullDownSRL.setOnRefreshListener(this);

        //设置post请求
        mPullDownSRL.post(new Runnable() {
            @Override
            public void run() {
                mPullDownSRL.setRefreshing(true);//展开刷新动画
                onRefresh();
            }
        });
    }

//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }

    @Override
    public void onRefresh() {
        if ( mAppInfoList != null ) {
            mAppInfoList.clear();
            mAppListAdapter.notifyDataSetChanged();
        }
        loadApp();
    }

    private void loadApp() {
        //获得已安装的应用程序可以通过getPackageManager
        final PackageManager pm = MainActivity.this.getPackageManager();
           //创建一个Observable对象
        Observable.create(new Observable.OnSubscribe<ApplicationInfo>() {

            @Override
            public void call(Subscriber<? super ApplicationInfo> subscriber) {
                //得到后放到数组
                List<ApplicationInfo> infoList = getApplicationInfoList(pm);
                for (ApplicationInfo info : infoList) {
                    //依次排列
                    subscriber.onNext(info);
                }
                subscriber.onCompleted();
            }
            //过滤操作符过滤系统掉app
        }).filter(new Func1<ApplicationInfo, Boolean>() {
            @Override
            //call的返回值为Bool类型决定了是否将结果输出给订阅者
            public Boolean call(ApplicationInfo applicationInfo) {
                return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)<=0;
            }
            //ApplicationInfo并不是所需要的类型，因此通过map操作符将其转换为AppInfo
        }).map(new Func1<ApplicationInfo, AppInfo>() {

            @Override
            //由于获取ApplicationInfo、过滤数据、转换数据相对比较耗时，因此需要通过subscribeOn操作符将这一系列操作放到子线程中来处理；
            public AppInfo call(ApplicationInfo applicationInfo) {
             AppInfo info = new AppInfo();
             info.setAppIcon(applicationInfo.loadIcon(pm));
             info.setAppName(applicationInfo.loadLabel(pm).toString());

                return info;
            }
            //因此需要通过observeOn操作符将onNext、onCompleted、onError调到主线程
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppInfo>() {
                    @Override
                    public void onCompleted() {
                     mAppListAdapter.notifyDataSetChanged();
                     mPullDownSRL.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e)  {
                      mPullDownSRL.setRefreshing(false);
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                         mAppInfoList.add(appInfo);
                    }

                });

    }

    private List<ApplicationInfo> getApplicationInfoList(final PackageManager pm) {
        return pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
