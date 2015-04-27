package com.toyking.jiyou;

import com.toyking.jiyou.model.User;
import com.toyking.jiyou.service.ServiceException;
import com.toyking.jiyou.service.UserService;
import com.toyking.jiyou.service.UserServiceImp;

import net.qiujuer.genius.widget.GeniusButton;
import net.qiujuer.genius.widget.GeniusEditText;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	public static final String TAG = RegisterActivity.class.getName();
	
	private LinearLayout layout_register_back = null;
	private GeniusButton btn_register_submit = null;
	private GeniusEditText edt_register_username = null;
	private GeniusEditText edt_register_password = null;
	private GeniusEditText edt_register_realname = null;
	private GeniusEditText edt_register_phone = null;
	
	public ProgressDialog dialog = null;
	
    @SuppressLint("HandlerLeak") private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if (dialog != null && dialog.isShowing()) dialog.dismiss();
            switch (msg.what) {
                case 0:
                    String error_msg = msg.getData().getSerializable("ERROR_MSG").toString();
                    Toast.makeText(RegisterActivity.this, error_msg, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(RegisterActivity.this, "×¢²á³É¹¦", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    intent.putExtras(msg.getData());
    				startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
	
	private void init(){
		layout_register_back = (LinearLayout) findViewById(R.id.layout_register_back);
		btn_register_submit = (GeniusButton) findViewById(R.id.btn_register_submit);
		edt_register_username = (GeniusEditText) findViewById(R.id.edt_register_username);
		edt_register_password = (GeniusEditText) findViewById(R.id.edt_register_password);
		edt_register_realname = (GeniusEditText) findViewById(R.id.edt_register_realname);
		edt_register_phone = (GeniusEditText) findViewById(R.id.edt_register_phone);
		
		layout_register_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
		
		btn_register_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final User user=new User();
				user.setUsername( edt_register_username.getText().toString() );
				user.setPassword( edt_register_password.getText().toString() );
				user.setRealname( edt_register_realname.getText().toString() );
				user.setPhone(  edt_register_phone.getText().toString() );
				
				if (dialog == null) {
                    dialog = new ProgressDialog(RegisterActivity.this);
                }
                dialog.setTitle("×¢²á");
                dialog.setMessage("×¢²áÖÐ£¬ÇëÉÔºó...");
                //dialog.setCancelable(false);
                dialog.show();
                
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserService user_service = new UserServiceImp();
                        try {
                        	user_service.UserAdd(user);
                        	User regist_user = user_service.GetUser(user.getUsername() , user.getPassword());
							Bundle bundle = new Bundle();
							bundle.putSerializable("user", regist_user);
							Message msg = new Message();
							msg.what = 1;
							msg.setData(bundle);
							handler.sendMessage(msg);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = 0;
                            Bundle data = new Bundle();
                            data.putSerializable("ERROR_MSG", e.getMessage());
                            msg.setData(data);
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = 0;
                            Bundle data = new Bundle();
                            data.putSerializable("ERROR_MSG", "×¢²á³ö´í£¬ÍøÂçÁ¬½ÓÊ§°Ü");
                            msg.setData(data);
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		init();
	}
	
}
