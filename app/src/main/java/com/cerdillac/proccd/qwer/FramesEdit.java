package com.cerdillac.proccd.qwer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import java.io.IOException;
import java.io.OutputStream;

public class FramesEdit extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frames_edit);
        PaintView paintView = findViewById(R.id.paint_small);
        //接受传输的数据
        Intent intent = getIntent();
        String data = intent.getDataString();
        LinearLayout layout1 = findViewById(R.id.ll_shape);
        //返回
        ImageView ed_back = findViewById(R.id.ed_back);
        ed_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //加载背景图片
        PaintView ed_frame = findViewById(R.id.ed_frame);
        ed_frame.setBackgroundResource(Integer.parseInt(data));
        //图形选择栏
        RelativeLayout pull_push = findViewById(R.id.pull_push);//形状选择框
        ImageView ed_shape = findViewById(R.id.ed_shape);
        ed_shape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.VISIBLE);
            }
        });
        ImageView img_cha = findViewById(R.id.img_cha);
        img_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.GONE);
            }
        });
        ImageView img_gou = findViewById(R.id.img_gou);
        img_gou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.GONE);
            }
        });
        //图形变换
        RadioButton rb_yuan = findViewById(R.id.img_yuan);
        RadioButton rb_tao = findViewById(R.id.img_tao);
        RadioButton rb_xin = findViewById(R.id.img_xin);
        RadioButton rb_fan = findViewById(R.id.img_fang);
        Changableview chang = findViewById(R.id.add_photo);
        pull_push.setBackground(getApplicationContext().getDrawable(R.drawable.photo_shape_circle));
        rb_yuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chang.setShape_type("round");
                pull_push.setBackground(getDrawable(R.drawable.photo_shape_circle));
            }
        });
        rb_tao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chang.setShape_type("heart");
                pull_push.setBackground(getDrawable(R.drawable.photo_shape_heart));
            }
        });
        rb_xin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chang.setShape_type("star");
                pull_push.setBackground(getDrawable(R.drawable.photo_shape_star));
            }
        });
        rb_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chang.setShape_type("square");
                pull_push.setBackground(getDrawable(R.drawable.photo_shape_square));
            }
        });
        GestureDetector gestureDetector = new GestureDetector(this, new MyListener());
        pull_push.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
        //调色盘系列
        int colors[] = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        VerticalSeekBar verticalSeekBar = findViewById(R.id.color);
        verticalSeekBar.setProgressDrawable(gradientDrawable);
        verticalSeekBar.setMax(100);
        ImageView imageView = findViewById(R.id.img_getcolor);
        imageView.setImageResource(R.drawable.red);
        //改变画笔的颜色
        verticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int pro = verticalSeekBar.getProgress();
                if(pro>0&&pro<13){
                    imageView.setImageResource(R.drawable.red);
                    paintView.setPenColor(Color.RED);
                    ed_frame.setPenColor(Color.RED);
                } else if (pro>13&&pro<20) {
                    imageView.setImageResource(R.drawable.orange);
                    paintView.setPenColor(Color.YELLOW);
                    ed_frame.setPenColor(Color.YELLOW);
                } else if (pro>20&&pro<25) {
                    imageView.setImageResource(R.drawable.yel);
                    paintView.setPenColor(Color.YELLOW);
                    ed_frame.setPenColor(Color.YELLOW);
                } else if (pro>25&&pro<50) {
                    imageView.setImageResource(R.drawable.green);
                    paintView.setPenColor(Color.GREEN);
                    ed_frame.setPenColor(Color.GREEN);
                } else if (pro>50&&pro<55) {
                    imageView.setImageResource(R.drawable.litgre);
                    paintView.setPenColor(Color.BLUE);
                    ed_frame.setPenColor(Color.BLUE);
                } else if (pro>55&&pro<70) {
                    imageView.setImageResource(R.drawable.blue);
                    paintView.setPenColor(Color.BLUE);
                    ed_frame.setPenColor(Color.BLUE);
                } else if (pro>75&&pro<80) {
                    imageView.setImageResource(R.drawable.purple);
                    paintView.setPenColor(Color.MAGENTA);
                    ed_frame.setPenColor(Color.MAGENTA);
                } else if (pro>80&&pro<85) {
                    imageView.setImageResource(R.drawable.pink);
                    paintView.setPenColor(Color.MAGENTA);
                    ed_frame.setPenColor(Color.MAGENTA);
                }else if(pro>85){
                    imageView.setImageResource(R.drawable.red);
                    paintView.setPenColor(Color.RED);
                    ed_frame.setPenColor(Color.RED);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //画笔系列
       SeekBar seek_width = findViewById(R.id.bar_width);
        seek_width.setMax(100);
        TextView txt_start = findViewById(R.id.txt_begin);
        //改变画笔的粗细
       seek_width.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               float end = seek_width.getProgress()/10;
               paintView.change_wideth(end);
               ed_frame.change_wideth(end);
               String  start = String.valueOf(seek_width.getProgress());
               txt_start.setText(start);
           }
           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {}
           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {}
       });
       //绘画编辑区
        ImageView ed_draw = findViewById(R.id.ed_draw);
        ImageView ed_cha = findViewById(R.id.draw_cha);
        ImageView ed_gou = findViewById(R.id.draw_gou);
        LinearLayout linearLayout = findViewById(R.id.draw_area);
        ed_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
        ed_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
            }
        });
        ed_gou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
            }
        });
        //保存图片
        ImageView img_save = findViewById(R.id.ed_save);
        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginSaveImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Changableview chang = findViewById(R.id.add_photo);
        RelativeLayout pull_push = findViewById(R.id.pull_push);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // 获取用户选择的图片的Uri
            Uri uri = data.getData();

            try {
//                 将Uri转换成Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                if (bitmap != null)
                {
                    chang.setImageBitmap(bitmap);
                    pull_push.setBackground(null);
                }
                // TODO: 处理用户选择的图片
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class MyListener implements GestureDetector.OnGestureListener {
        private float lastX; // 上一次触摸的 X 坐标
        private float lastY; // 上一次触摸的 Y 坐标

        @Override
        public boolean onDown(@NonNull MotionEvent motionEvent) {
            lastX = motionEvent.getX();
            lastY = motionEvent.getY();
            return false;
        }

        @Override
        public void onShowPress(@NonNull MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
// 启动相册，并等待用户选择图片
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            return false;
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
            float curX = motionEvent1.getX();
            float curY = motionEvent1.getY();

            // 计算手指在 X、Y 方向上滑动的距离
            float dx = curX - lastX;
            float dy = curY - lastY;

            // 移动控件的位置
            View view = findViewById(R.id.pull_push);
            int left = view.getLeft() + (int) dx;
            int top = view.getTop() + (int) dy;
            int right = view.getRight() + (int) dx;
            int bottom = view.getBottom() + (int) dy;
            view.layout(left, top, right, bottom);
            lastX = curX;
            lastY = curY;
            return false;
        }
        @Override
        public void onLongPress(@NonNull MotionEvent e) {
        }
        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
    private void beginSaveImage() {
        View view = findViewById(R.id.frame_chang);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());

        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "file_name.png");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        }
        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        try {
            OutputStream outputStream = resolver.openOutputStream(imageUri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
// 最后，发送广播通知系统更新相册
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
        }

        Intent intent = new Intent(FramesEdit.this, SavePhoto.class);
        intent.setData(imageUri);
        startActivity(intent);
    }

}