package com.script.muhelp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.script.muhelp.R;
import com.script.muhelp.util.SnackbatUtil;
import com.script.muhelp.util.String2File;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.HashMap;

import static com.script.muhelp.VarPool.*;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener{

    private View container;
    private Activity context = this;
    private EditText title;
    private EditText content;
    private ImageView imageview;
    private Button publish;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setLisener();

    }

    private void showPopueWindow(){
        View popView = View.inflate(this,R.layout.popupwindow_camera_need,null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                popupWindow.dismiss();

            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);

    }

    private void setLisener(){
        publish = findViewById(R.id.publish);
        publish.setOnClickListener(this);
        title = findViewById(R.id.title);
        imageview = findViewById(R.id.imageview);
        imageview.setOnClickListener(this);
        content = findViewById(R.id.content);
        container = findViewById(R.id.container);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                container.setFocusable(true);
                container.setFocusableInTouchMode(true);
                container.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish:
                publishShare();
                break;
            case R.id.back:
                setResult(CANLE);
                finish();
                break;
            case R.id.imageview:

                if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                        &&ActivityCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},PICPER);
                }else{
                    showPopueWindow();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK&&data!=null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            File file = new File(picturePath);
            try  {
                FileInputStream inputStream = new FileInputStream(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] bytes = new byte[2048];
                int ch = inputStream.read(bytes);
                while(ch!=-1){
                    outputStream.write(bytes,0,ch);
                    ch = inputStream.read(bytes);
                }
                bytes = outputStream.toByteArray();
                image = bytes;
                imageview.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));
            }catch (Exception e){
                SnackbatUtil.showErrorLong(container,"获取图片失败");
            }
        }
    }

    private void publishShare(){
        HashMap map = new HashMap();
        map.put("title",title.getText().toString());
        map.put("content",content.getText().toString());
        String data = String2File.getString(image);
        map.put("image",data);
        Intent intent= new Intent();
        intent.putExtra("map",map);
        setResult(OK,intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PICPER && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
            if(!isTip){
                SnackbatUtil.showErrorLong(container, "请允许开启权限，否则无法使用上传图片功能\n请进入设置->应用->权限进行设置");
            }
            return;
        }else if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            showPopueWindow();
            return;
        }
    }
}
