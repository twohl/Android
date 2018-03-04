package com.script.muhelp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.script.muhelp.R;
import com.script.muhelp.adapter.MuChatRecyclerViewAdapter;
import com.script.muhelp.entity.NotWork;
import com.script.muhelp.entity.User;
import com.script.muhelp.util.String2File;
import com.script.muhelp.util.StyleUtil;

import static com.script.muhelp.VarPool.CANLE;
import static com.script.muhelp.VarPool.OK;

public class NWActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    private ImageView imageView2;
    private TextView title;
    private TextView nickname;
    private TextView content;
    private TextView price;
    private TextView organtime;
    private TextView acctime;
    private TextView accnickname;
    private TextView nw_id;
    private Button back;
    private Button accepet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nw);
        setListener();
        setDataS();
        setStyle();
    }

    private void setStyle(){
        StyleUtil.setzhuangtailan(this);

    }

    private void setListener(){
        back = findViewById(R.id.back);
        accepet = findViewById(R.id.accepet);
        back.setOnClickListener(this);
        accepet.setOnClickListener(this);
    }

    private void setDataS(){
        NotWork notWork = (NotWork) getIntent().getSerializableExtra("nw");
        User user = (User) getIntent().getSerializableExtra("my");
        String id = notWork.getId()+"";
        String titl = notWork.getName();
        String nick = notWork.getOrganiser().getNickname();
        String cont = notWork.getContent();
        String pirc = notWork.getPrice();
        String organt = notWork.getOrgantime().toString();
        String acct = notWork.getAcctime() == null ?"":notWork.getAcctime().toString();
        String accnick = notWork.getAccepter() == null?"":notWork.getAccepter().getNickname();
        nw_id = findViewById(R.id.nw_id);
        title = findViewById(R.id.title);
        nickname = findViewById(R.id.nickname);
        content = findViewById(R.id.content);
        price = findViewById(R.id.price);
        organtime = findViewById(R.id.organtime);
        acctime = findViewById(R.id.acctime);
        accnickname = findViewById(R.id.accnickname);
        imageView = findViewById(R.id.imageview);
        imageView2 = findViewById(R.id.imageview2);
        String2File.setImage(imageView,"/icons/"+notWork.getOrganiser().getId()+"_icon");
        if(notWork.getAccepter()!=null)
            String2File.setImage(imageView,"/icons/"+notWork.getAccepter().getId()+"_icon");
        nw_id.setText(id);
        title.setText(titl);
        nickname.setText(nick);
        content.setText(cont);
        price.setText(pirc);
        organtime.setText(organt);
        acctime.setText(acct);
        accnickname.setText(accnick);
        if(user.getId() == notWork.getOrganiser().getId()){
            accepet.setText("取消");
        }else if(notWork.getAccepter()!=null&&user.getId() == notWork.getAccepter().getId()){
            accepet.setText("完成");
        }
    }

    @Override
    public void onBackPressed() {
        setResult(CANLE);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                setResult(CANLE);
                finish();
                break;
            case R.id.accepet:
                accepetNW();
                break;
        }
    }
    private void accepetNW(){
        Intent intent = new Intent();
        intent.putExtra("nw_id",nw_id.getText().toString());
        setResult(OK,intent);
        finish();
    }
}
