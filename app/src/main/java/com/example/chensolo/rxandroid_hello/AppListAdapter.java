package com.example.chensolo.rxandroid_hello;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chensolo.rxandroid_hello.model.AppInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/17.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder>{
    private  List<AppInfo> appInfoList;
    public AppListAdapter(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;



    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app_list,parent,false);

        return new ViewHolder(rootView);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setBackground(appInfoList.get(position).getAppIcon());
        holder.textView.setText(appInfoList.get(position).getAppName());
    }

    public int getItemCount() {
        return appInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView)
        TextView textView;
        @Bind(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this,itemView);
        }
    }
}
