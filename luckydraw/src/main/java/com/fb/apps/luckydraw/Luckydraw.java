package com.fb.apps.luckydraw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class Luckydraw extends ActionBarActivity {
    private Bitmap topImage;
    private  Bitmap bottomImage;
    private Bitmap blank;//新图片
    private  ImageView iv_top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luckydraw);
        init();
    }

    /**
     * 初始化控件及图片
     */
    private void init() {
        ImageView iv_bottom = (ImageView) findViewById(R.id.iv_bottom);//下面的图片
         iv_top = (ImageView) findViewById(R.id.iv_top);//上面的图片

        //加载图片到控件上
        BitmapFactory.Options opts = new BitmapFactory.Options();//图片加载器的配置对象，一般用于配置一些缩放比例，和像素单位
        opts.inSampleSize =2;//加载器把原图的宽高缩放到1/2的效果来展示
         topImage = BitmapFactory.decodeResource(getResources(), R.drawable.zheng, opts);
        bottomImage = BitmapFactory.decodeResource(getResources(),R.drawable.fan,opts);
        iv_bottom.setImageBitmap(bottomImage);//给上面的控件设置一张图片

        //创建一张空白图片
        blank = Bitmap.createBitmap(topImage.getWidth(),topImage.getHeight(), Bitmap.Config.ARGB_8888);
//        blank = topImage.copy(Bitmap.Config.ARGB_8888, true);
        int w = blank.getWidth();          int h = blank.getHeight();
        //把上面的图片花在空白图片上
        Canvas canvas = new Canvas(blank);//画画板对象，把blank指定用于绘制各种各样的形状和图片
        canvas.drawBitmap(topImage, 0, 0, null);//把topImage画在空白图片上
        //启用抗锯齿和使用设备的文本字距
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
         //字体的相关设置
        textPaint.setTextSize(20.0f);//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(Color.WHITE);//设置字体颜色
        textPaint.setShadowLayer(3f, 1, 1, getResources().getColor(android.R.color.background_dark));
        //图片上添加水印的位置，这里设置的是中下部3/4处
        canvas.drawText("谢谢参与", blank.getHeight()*1/4, blank.getWidth()*11/10, textPaint);
//        canvas.drawText("谢谢参与", w/2-50, (float) (h*0.75), textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        iv_top.setImageBitmap(blank);//给上面的控件设置一张图片

        //监听手指在屏幕上移动的位置,得到这个坐标点
        iv_top.setOnTouchListener(new MyOnTouchLister());
    }

    class MyOnTouchLister implements View.OnTouchListener {
        /**
         * 当上面的控件，手指触摸触发的方法
         * @param v 被触摸的控件
         * @param event 触摸的事件
         *        移动事件：      MotionEvent.ACTION_MOVE
         *        按下事件：      MotionEvent.ACTION_DOWN
         *        抬起事件：      MotionEvent.ACTION_UP
         * @return
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {

                //事件判断
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    //去除按下坐标
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    for (int i = x-10;i< x+10;i++){
                        for(int j = y -10;j< y+10;j++){
                            if(j>= 0 && j<blank.getHeight()&&i>=0 && i< blank.getWidth()){
                                blank.setPixel(i,j, Color.TRANSPARENT);//设置透明颜色
                            }
                        }
                    }
                    iv_top.setImageBitmap(blank);//把修改后的图片在控件上显示
                }

            return true; //if true 消耗此次触摸事件，自己来处理事件
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_luckydraw, menu);
        return true;
    }

}
