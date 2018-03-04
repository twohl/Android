package com.script.muhelp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.script.muhelp.R;
import com.script.muhelp.adapter.MuChatRecyclerViewAdapter;
import com.script.muhelp.adapter.MuFindRecyclerViewAdapter;
import com.script.muhelp.adapter.MuNWRecyclerViewAdapter;
import com.script.muhelp.adapter.MuPagerAdapter;
import com.script.muhelp.adapter.MuPersonRecyclerViewAdapter;
import com.script.muhelp.binder.MuBinder;
import com.script.muhelp.entity.Chat;
import com.script.muhelp.entity.ChatId;
import com.script.muhelp.entity.NotWork;
import com.script.muhelp.entity.Share;
import com.script.muhelp.entity.User;
import com.script.muhelp.fragment.MuChatFragment;
import com.script.muhelp.fragment.MuFindFragment;
import com.script.muhelp.fragment.MuMineFragment;
import com.script.muhelp.fragment.MuNWFragment;
import com.script.muhelp.fragment.MuPersonFragment;
import com.script.muhelp.listener.OnItemClickListener;
import com.script.muhelp.listener.OnMyClickListener;
import com.script.muhelp.util.BottomNavigationViewUtil;
import com.script.muhelp.util.HttpUtil;
import com.script.muhelp.util.MenuIconUtil;
import com.script.muhelp.services.MuService;
import com.script.muhelp.util.PositionUtil;
import com.script.muhelp.util.SignUtil;
import com.script.muhelp.util.SnackbatUtil;
import com.script.muhelp.util.String2File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.script.muhelp.VarPool.*;


public class MainActivity extends AppCompatActivity {

    private Activity context = this;
    private ViewPager muViewPager;
    private MuPagerAdapter muPagerAdapter;
    private MuChatRecyclerViewAdapter chatAdapter;
    private MuPersonRecyclerViewAdapter personAdapter;
    private MuNWRecyclerViewAdapter nwAdapter;
    private MuFindRecyclerViewAdapter findAdapter;
    private Location location;
    private MuMineFragment mineFragment;
    private User user;
    private boolean canel = true;
    private String username;
    private String lastTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

    private MenuItem menuItem;
    private BottomNavigationView navigation;
    private MuBinder binder;
    private boolean editable = false;
    byte[] icon;
    private Handler handler = new MuHandler();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            //设置监听器，点击底部导航栏翻到对应fragment

            switch (item.getItemId()) {
                case R.id.chat:
                    muViewPager.setCurrentItem(0,false);
                    supportInvalidateOptionsMenu();
                    return true;
                case R.id.person:
                    muViewPager.setCurrentItem(1,false);
                    supportInvalidateOptionsMenu();
                    return true;
                case R.id.NW:
                    muViewPager.setCurrentItem(2,false);
                    supportInvalidateOptionsMenu();
                    return true;
                case R.id.find:
                    muViewPager.setCurrentItem(3,false);
                    supportInvalidateOptionsMenu();
                    return true;
                case R.id.mine:
                    muViewPager.setCurrentItem(4,false);
                    mineFragment.setData(user);
                    supportInvalidateOptionsMenu();
                    editable = false;
                    setEditable(false);
                    return true;
            }
            return false;
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MuBinder) service;
            Map map = new HashMap();
            binder.setLocation(location);
            binder.setHandler(handler);
            binder.setPath(context.getFilesDir());
            binder.getMainData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void setEditable(boolean flag){
        MuMineFragment fragment = (MuMineFragment) muPagerAdapter.getItem(4);
        fragment.setEditable(flag);
    }

    private Map getUserData(){
        MuMineFragment fragment = (MuMineFragment) muPagerAdapter.getItem(4);
        return fragment.getUserData();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (muViewPager.getCurrentItem()){
            case 0:
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(false);
                break;
            case 1:
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(false);
                break;
            case 2:
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(false);
                break;
            case 3:
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(true);
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(false);
                break;

            case 4:
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(false);
                if(editable){
                    menu.getItem(2).setVisible(false);
                    menu.getItem(3).setVisible(true);
                } else{
                    menu.getItem(2).setVisible(true);
                    menu.getItem(3).setVisible(false);
                }
                break;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDataS();
        setListener();
        setStyle();

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

    private void setStyle(){
        location = PositionUtil.tryLocation(context);
        if(location !=null){
            Intent intent = new Intent(context,MuService.class);
            bindService(intent,connection,BIND_AUTO_CREATE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置初始页
        muViewPager.setCurrentItem(2,false);
        //修改样式
        BottomNavigationViewUtil.disableShiftMode(navigation);
    }

    private void setDataS(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        lastTime = sdf.format(zero);
        username = getIntent().getStringExtra("username");
        muPagerAdapter = new MuPagerAdapter(getSupportFragmentManager());
        muViewPager = (ViewPager)findViewById(R.id.viewPager);
        muViewPager.setAdapter(muPagerAdapter);
    }

    private void setListener(){
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        PositionUtil.setLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                MainActivity.this.location.set(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        //设置监听器
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        //页面改变底部导航栏改变
        muViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageSelected(int position) {
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
                supportInvalidateOptionsMenu();
                if(position == 4) {
                    setEditable(false);
                    editable = false;
                    mineFragment.setData(user);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final MuChatFragment chatFragment = (MuChatFragment) muPagerAdapter.getItem(0);
        chatAdapter = chatFragment.getmAdapter();
        chatAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object position) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("my",user);
                intent.putExtra("username",username);
                intent.putExtra("isChatRoom",false);
                intent.putExtra("lastTime",lastTime);
                intent.putExtra("chat",(ChatId)position);
                TextView textView = view.findViewById(R.id.count);
                ((ChatId) position).setCount(0);
                textView.setVisibility(View.GONE);
                startActivityForResult(intent,CHAT);
            }
        });

        MuPersonFragment personFragment = (MuPersonFragment) muPagerAdapter.getItem(1);
        personAdapter = personFragment.getmAdapter();
        personAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object position) {
                Intent intent = new Intent(context,UserInfoActivity.class);
                intent.putExtra("my",user);
                intent.putExtra("user",(User)position);
                startActivity(intent);
            }
        });
        personFragment.setOnMyClickListener(new OnMyClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("my",user);
                intent.putExtra("username",username);
                intent.putExtra("isChatRoom",true);
                startActivity(intent);
            }
        });
        MuNWFragment nwFragment = (MuNWFragment) muPagerAdapter.getItem(2);
        nwAdapter = nwFragment.getmAdapter();
        nwAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object position) {
                Intent intent = new Intent(context,NWActivity.class);
                intent.putExtra("nw",(NotWork)position);
                intent.putExtra("my",user);
                if(((NotWork) position).getOrganiser().getId() == user.getId()){
                    startActivityForResult(intent,CANLE_NW);
                }else if(((NotWork) position).getAccepter() != null&&((NotWork) position).getAccepter().getId() == user.getId()){
                    startActivityForResult(intent,COMPLITE_NW);
                }else{
                    startActivityForResult(intent,ACC_NW);
                }
            }
        });
        nwFragment.setOnMyClickListener(new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                if(canel){
                    ((TextView)v).setText("关闭我的nw");
                }else{
                    ((TextView)v).setText("我的nw");
                }
                nwAdapter.updateData(null);
                canel = !canel;
            }
        });
        MuFindFragment findFragment = (MuFindFragment) muPagerAdapter.getItem(3);

        findAdapter =findFragment.getmAdapter();
        findAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object position) {

            }
        });
        mineFragment = (MuMineFragment) muPagerAdapter.getItem(4);
        mineFragment.setOnMyClickListener(new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                        &&ActivityCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},PICPER);
                }else{
                    showPopueWindow();
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, MuService.class);
        if(binder != null){
            unbindService(connection);
        }
        stopService(intent);

        File fileImage = new File(this.getFilesDir(),"//users//images//");
        File fileIcon = new File(this.getFilesDir(),"//users//icons//");
        File[] fileArray = fileImage.listFiles();
        for(int i=0;i<fileArray.length;i++){
            fileArray[i].delete();
        }
        fileArray = fileIcon.listFiles();
        for(int i=0;i<fileArray.length;i++){
            fileArray[i].delete();
        }
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options,menu);
        MenuIconUtil.MenuIconVisible(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.createNW:
                Intent intent = new Intent(context,AddNWActivity.class);
                startActivityForResult(intent,CREATE_NW);
                break;
            case R.id.createShare:
                Intent intent1 = new Intent(context,ShareActivity.class);
                startActivityForResult(intent1,CREATE_SHARE);
                break;
            case R.id.edit:
                editable = true;
                setEditable(true);
                supportInvalidateOptionsMenu();
                break;
            case R.id.complite:
                editable = false;
                setEditable(false);
                editUserInfo();
                supportInvalidateOptionsMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editUserInfo(){
        Map map = getUserData();
        String data = String2File.getString(icon);
        map.put("icon",data);
        binder.editUserData(map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CREATE_NW:
                if(resultCode == OK){
                    Map map = (Map) data.getSerializableExtra("map");
                    binder.publishNW(map);
                }
                break;
            case CREATE_SHARE:
                if(resultCode == OK){
                    Map map = (Map) data.getSerializableExtra("map");
                    binder.share(map);
                }
                break;
            case ACC_NW:
                if(resultCode == OK){
                    String nw_id = data.getStringExtra("nw_id");
                    binder.accNW(nw_id);
                }
                break;
            case CANLE_NW:
                if(resultCode == OK){
                    String nw_id = data.getStringExtra("nw_id");
                    binder.cancleNW(nw_id);
                }
                break;
            case COMPLITE_NW:
                if(resultCode ==OK){
                    String nw_id = data.getStringExtra("nw_id");
                    binder.compliteNW(nw_id);
                }
                break;

            case RESULT_LOAD_IMAGE:
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
                        icon = bytes;
                        ImageView image = mineFragment.getRootView().findViewById(R.id.imageview);
                        image.setImageBitmap(BitmapFactory.decodeByteArray(icon,0,icon.length));
                    }catch (Exception e){
                        SnackbatUtil.showErrorLong(muViewPager,"获取头像失败");
                    }
                }

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PICPER && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
            if(!isTip){
                SnackbatUtil.showErrorLong(muViewPager, "请允许开启权限，否则无法使用上传图片功能\n请进入设置->应用->权限进行设置");
            }
            return;
        }else if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            showPopueWindow();
            return;
        }
        for (int i = 0; i < grantResults.length; i++) {
            boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (isTip) {//表明用户没有彻底禁止弹出权限请求
                    ActivityCompat.requestPermissions(this,new String[]{permissions[i]},requestCode);
                }else{
                    SnackbatUtil.showErrorLong(muViewPager, "请允许定位权限，否则无法使用本软件\n请进入设置->应用->权限进行设置");
                }
                return;
            }else{
                while(location == null)
                    location = PositionUtil.getLocation();
                Intent intent = new Intent(context,MuService.class);
                bindService(intent,connection,BIND_AUTO_CREATE);
            }
        }
    }

    public class MuHandler extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOPOSITION:
                    break;

                case LOG_OUT:
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("from", ERROR_FROM);
                    File file = new File(context.getFilesDir(),"users//");
                    file.listFiles()[0].delete();
                    startActivity(intent);
                    MainActivity.this.finish();
                    break;

                case USER_LIST:
                    if(user == null){
                        SnackbatUtil.showErrorLong(muViewPager,"数据拉取失败，请稍后再试");
                        break;
                    }
                    ArrayList<User> list = (ArrayList<User>) msg.getData().getSerializable("userList");
                    ArrayList<User> list1 = new ArrayList<>();
                    for(int i=0;i<list.size();i++){
                        User u = list.get(i);
                        if(u.getId() != user.getId()){
                            list1.add(u);
                        }
                    }
                    personAdapter.updateData(list1);
                    break;

                case CHAT_ID:
                    ChatId chatId = (ChatId) msg.getData().getSerializable("chat_id");
                    chatAdapter.setMy(user);
                    chatAdapter.updateData(chatId);
                    break;

                case NW_LIST:
                    if(user == null){
                        SnackbatUtil.showErrorLong(muViewPager,"数据拉取失败，请稍后再试");
                        break;
                    }
                    ArrayList<NotWork> list2 = (ArrayList<NotWork>) msg.getData().getSerializable("nwList");
                    for(NotWork nw:list2){
                        if(nw.getOrganiser().getId() == user.getId()){
                            list2.remove(nw);
                        }
                    }
                    if(canel){
                        nwAdapter.updateData(list2);
                    }
                    break;

                case SHARE_LIST:
                    ArrayList<Share> list3 = (ArrayList<Share>) msg.getData().getSerializable("shareList");
                    findAdapter.updateData(list3);
                    break;


                case MY_NW:
                    ArrayList<NotWork> list4 = (ArrayList<NotWork>) msg.getData().getSerializable("mNWList");
                    if(!canel)
                        nwAdapter.updateData(list4);
                    break;

                case USER_INFO:
                user = (User) msg.getData().getSerializable("userInfo");
                if(user != null){
                    binder.start();
                }
                break;

                case EDITUSER:
                case ACC_NW:
                case PUBLISH_SHARE:
                case PUBLISH_NW:
                    SnackbatUtil.showMessageLong(muViewPager,msg.getData().getString("message"));
                    break;
                case KEEP:
                    break;


                default:
                    SnackbatUtil.showErrorLong(muViewPager, msg.getData().getString("message"));
            }
        }
    }
}
