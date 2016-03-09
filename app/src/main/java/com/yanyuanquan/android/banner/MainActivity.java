package com.yanyuanquan.android.banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yanyuanquan.android.bannerlibrary.AutoBannerLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    AutoBannerLayout bannerLayout;
    AutoBannerLayout bannerLayout2;
    AutoBannerLayout bannerLayout3;
    AutoBannerLayout bannerLayout4;
    private String url = "http://img2.imgtn.bdimg.com/it/u=3494980167,4088219007&fm=21&gp=0.jpg";
    String url2 = "http://img0.imgtn.bdimg.com/it/u=620452195,2622903875&fm=21&gp=0.jpg";
    String url3 = "http://img3.imgtn.bdimg.com/it/u=3346982507,3415122236&fm=21&gp=0.jpg";
    String url4 = "http://pic4.nipic.com/20090731/3063030_100323016_2.jpg";
    String url5 = "http://img5.imgtn.bdimg.com/it/u=160543398,1200048323&fm=21&gp=0.jpg";
    String url6 = "http://pic23.nipic.com/20120918/10031483_133215033311_2.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerLayout = (AutoBannerLayout) findViewById(R.id.banner);
        List<String> list = new ArrayList<>();
        list.add(url);
        bannerLayout.setDataUrls(list);


        bannerLayout2 = (AutoBannerLayout) findViewById(R.id.banner1);
        List<String> list2 = new ArrayList<>();
        list2.add(url5);
        list2.add(url2);
        bannerLayout2.setDataUrls(list2);



        bannerLayout3 = (AutoBannerLayout) findViewById(R.id.banner2);
        List<String> list3 = new ArrayList<>();
        list3.add(url4);
        list3.add(url2);
        list3.add(url3);
        bannerLayout3.setDataUrls(list3);


        bannerLayout4 = (AutoBannerLayout) findViewById(R.id.banner3);
        List<String> list4 = new ArrayList<>();
        list4.add(url4);
        list4.add(url2);
        list4.add(url3);
        list4.add(url5);
        list4.add(url6);
        bannerLayout4.setDataUrls(list4);
        //
        bannerLayout.setBannerItemClickListener(new AutoBannerLayout.onBannerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }
}
