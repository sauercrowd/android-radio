package com.example.android.radioapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by jotten on 14.11.17.
 */

public class RadioListAdapter extends RecyclerView.Adapter<RadioListAdapter.RadioListHolder> {

    private List<RadioItem> items;
    private ImageLoader mImageLoader;
    private MainActivity main;

    public RadioListAdapter(MainActivity a){
        items = new ArrayList<>();
        main=a;
    }

    @Override
    public RadioListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mImageLoader = ImageLoaderSingleton.getInstance(parent.getContext()).getImageLoader();

        Context c = parent.getContext();
        int layoutid = R.layout.radio_list_cv;
        LayoutInflater li = LayoutInflater.from(c);

        boolean shouldAttachImmediately = false;
        View v = li.inflate(layoutid,parent,shouldAttachImmediately);

        RadioListHolder rh = new RadioListHolder(v, main);
        return rh;
    }

    @Override
    public void onBindViewHolder(RadioListHolder holder, int position) {
        RadioItem item = items.get(position);
        String title = item.getTitle();
        String imageURL = item.getImageURL();

        holder.name.setText(title);
        holder.setItem(item);
        if(!imageURL.equals("")){ //if url non empty
            NetworkImageView v = holder.image;
            v.setImageUrl(imageURL,mImageLoader);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<RadioItem> l){
        items = l;
    }

    public class RadioListHolder extends ViewHolder{
        private TextView name;
        private NetworkImageView image;
        private RadioItem item;

        public RadioListHolder(View itemView, final MainActivity m) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.iv_radio_logo);
            name  = (TextView)  itemView.findViewById(R.id.tv_radio_name);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    m.changeRadio(item);
                    showNotification(m);

                }
            });
        }

        public void setItem(RadioItem item){
            this.item = item;
        }

        public void showNotification(Context context){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!");

            mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, PlayActivity.class),0));


            int mNotificationId = 001;
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId,mBuilder.build());

        }
    }
}
