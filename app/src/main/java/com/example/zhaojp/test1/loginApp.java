package com.example.zhaojp.test1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class loginApp extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mUsername,mPassword;
    private Button mButton;
    private me mePage;
    private Tencent mTencent;

    private sqlCreate sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_app);

        sql=new sqlCreate( loginApp.this );

        //mEmailView=findViewById(R.id.email);
        mUsername=findViewById(R.id.username);
        mPassword=findViewById(R.id.password);
        mButton=findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String email=mEmailView.getText().toString();
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                canLogin(username,password);
            }
        });


        //文字跳转（注册)
        TextView textView2=findViewById(R.id.login);
        String text2="Register";
        SpannableString spannableString1=new SpannableString(text2);

        spannableString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(loginApp.this,registerApp.class);
                startActivity(intent);
            }
        }, 0, text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView2.setText(spannableString1);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());




        //自动补充密码
        /*mPassword.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    SharedPreferences sp=getSharedPreferences("User",MODE_PRIVATE);
                    if(sp.getBoolean("rememberPassword",false)==true) {
                        String getPassword = sp.getString( "password", null );
                        mPassword.setText( getPassword );
                    }
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });*/

        mTencent = Tencent.createInstance("1107997805",getApplicationContext());
        Button qq=findViewById( R.id.qq );
        qq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mTencent.login( loginApp.this,"all",new BaseUiListener() );
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());

        if(requestCode == Constants.REQUEST_API) {
            if(resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, new BaseUiListener());
            }
        }
    }


    public void canLogin(final String username, final String password){
        if(net()){
            BmobQuery<Person> query = new BmobQuery<Person>();
            query.addWhereEqualTo("username",username);
            query.findObjects(new FindListener<Person>() {
                @Override
                public void done(List<Person> object, BmobException e) {
                    if(e==null){
                        if(username.equals(object.get(0).getUsername())&&password.equals(object.get(0).getPassword()))
                        {
                            Toast.makeText(loginApp.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        /*Intent intent=new Intent(loginApp.this, MainActivity.class);
                        startActivity(intent);*/
                        /*if (mePage == null) {
                            mePage = new me();
                        }
                        getFragmentManager().beginTransaction().add(R.id.fragment_content, mePage).commitAllowingStateLoss();*/

                            //存储信息
                            SharedPreferences sp=getSharedPreferences("User", Context.MODE_PRIVATE);
                            final SharedPreferences.Editor edit = sp.edit();
                            //通过editor对象写入数据
                            edit.putBoolean("login",true);


                            //是否保存密码
                        /*CheckBox rememberPassword=findViewById(R.id.rememberPassword);
                        rememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                // TODO Auto-generated method stub
                                if(isChecked){//选中
                                    edit.putString("name",username);
                                    edit.putString("password",password);

                                    edit.putBoolean("rememberPassword",true);
                                }else{//未选中

                                }
                            }
                        });*/


                            //提交数据存入到xml文件中
                            edit.commit();

                            Intent intent=new Intent(loginApp.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(loginApp.this, "User name or password error", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(loginApp.this, "User name or password error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            SQLiteDatabase sqLiteDatabase = sql.getReadableDatabase();
            String sqls = "select *from users where username = ?";

            Cursor rawQuery = sqLiteDatabase.rawQuery(sqls, new String[] { username});
            if (rawQuery.moveToFirst() == true) {
                rawQuery.close();
                Toast.makeText(loginApp.this, "Login successfully", Toast.LENGTH_SHORT).show();

                //存储信息
                SharedPreferences sp=getSharedPreferences("User", Context.MODE_PRIVATE);
                final SharedPreferences.Editor edit = sp.edit();
                //通过editor对象写入数据
                edit.putBoolean("login",true);
                //提交数据存入到xml文件中
                edit.commit();

                Intent intent=new Intent(loginApp.this, MainActivity.class);
                startActivity(intent);
            }
            else
                Toast.makeText(loginApp.this, "User name or password error", Toast.LENGTH_SHORT).show();
        }
    }

    //判断是否有网
    public boolean net()
    {
        ConnectivityManager connectivityManager;//用于判断是否有网络

        connectivityManager = (ConnectivityManager)loginApp.this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        NetworkInfo info = connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息

        if(info==null)
            return false;
        else
            return true;
    }


    private class BaseUiListener implements IUiListener {

        //这个类需要实现三个方法 onComplete（）：登录成功需要做的操作写在这里
        // onError onCancel 方法具体内容自己搜索

        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Toast.makeText( getApplicationContext(), "登录成功", Toast.LENGTH_SHORT ).show();
            try {
                //获得的数据是JSON格式的，获得你想获得的内容
                //如果你不知道你能获得什么，看一下下面的LOG
                Log.v( "----TAG--", "-------------" + response.toString() );
                String openidString = ((JSONObject) response).getString( "openid" );
                mTencent.setOpenId( openidString );
                mTencent.setAccessToken( ((JSONObject) response).getString( "access_token" ), ((JSONObject) response).getString( "expires_in" ) );


                Log.v( "TAG", "-------------" + openidString );
                //access_token= ((JSONObject) response).getString("access_token");
                //expires_in = ((JSONObject) response).getString("expires_in");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(getApplicationContext(), qqToken);

            //    info.getUserInfo(new BaseUIListener(this,"get_simple_userinfo"));
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    //用户信息获取到了
                    try {
                        Log.v("用户名", ((JSONObject) o).getString("nickname"));
                        Log.v("用户姓名", ((JSONObject) o).getString("gender"));
                        Log.v("UserInfo",o.toString());

                        //存储信息
                        SharedPreferences sp=getSharedPreferences("User", Context.MODE_PRIVATE);
                        final SharedPreferences.Editor edit = sp.edit();
                        //通过editor对象写入数据
                        edit.putBoolean("login",true);
                        //提交数据存入到xml文件中
                        edit.commit();
                        Intent intent1 = new Intent(loginApp.this,MainActivity.class);
                        startActivity(intent1);
                        finish();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(UiError uiError) {
                    Log.v("UserInfo","onError");
                }

                @Override
                public void onCancel() {
                    Log.v("UserInfo","onCancel");
                }
            });

        }

        public void onError(UiError uiError) {

        }

        public void onCancel() {

        }
    }

}
