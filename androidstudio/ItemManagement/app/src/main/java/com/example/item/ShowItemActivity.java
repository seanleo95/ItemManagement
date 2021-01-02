package com.example.item;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import Item.Item;
import TableContanst.TableContanst;
public class ShowItemActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);
        Intent intent = getIntent();
        Item itm = (Item) intent.getSerializableExtra(TableContanst.ITEM_TABLE);

        ((TextView)findViewById(R.id.tv_info_id)).setText(itm.getId()+"");
        ((TextView)findViewById(R.id.tv_info_name)).setText(itm.getName());
        ((TextView)findViewById(R.id.tv_info_age)).setText(itm.getAge()+"");
        ((TextView)findViewById(R.id.tv_info_sex)).setText(itm.getSex());
        ((TextView)findViewById(R.id.tv_info_usetime)).setText(itm.getLike());
        ((TextView)findViewById(R.id.tv_info_buy_date)).setText(itm.getTrainDate());
        ((TextView)findViewById(R.id.tv_info_limit)).setText(itm.getLimitTime());

    }
    public void goBack(View view) {
        finish();
    }

    public void createNotification(View view){
        Intent intent1 = getIntent();
        Item itm = (Item) intent1.getSerializableExtra(TableContanst.ITEM_TABLE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Prepare intent which is triggerd if the notification is selected
        Intent intent=new Intent(this,NotificationResultActivity.class);
        PendingIntent pIntent=PendingIntent.getActivity(this,0,intent,0);

        //Build notification
        //Actions are just fake
        Notification.Builder builder=new Notification.Builder(this)
                .setContentTitle(itm.getName())
                .setContentText("保质期快到期限了")
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(PendingIntent.getActivity(this,1,new Intent(this,NotificationResultActivity.class),PendingIntent.FLAG_CANCEL_CURRENT));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001","my_channel",NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId("001");
        }

        Notification n = builder.build();
        notificationManager.notify(0,n);
    }
}
