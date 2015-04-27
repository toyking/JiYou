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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private GeniusEditText edt_login_username = null;
	private GeniusEditText edt_login_password = null;
	private LinearLayout layout_login_back = null;
	private LinearLayout layout_login_register = null;
	private GeniusButton btn_login_submit = null;
	
	public ProgressDialog dialog = null;

    @SuppressLint("HandlerLeak") private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if (dialog != null && dialog.isShowing()) dialog.dismiss();
            switch (msg.what) {
                case 0:
                    String error_msg = msg.getData().getSerializable("ERROR_MSG").toString();
                    Toast.makeText(LoginActivity.this, error_msg, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, "µÇÂ¼³É¹¦", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtras(msg.getData());
    				startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

	private void init() {
		edt_login_username = (GeniusEditText) findViewById(R.id.edt_login_username);
		edt_login_password = (GeniusEditText) findViewById(R.id.edt_login_password);
		layout_login_back = (LinearLayout) findViewById(R.id.layout_login_back);
		layout_login_register = (LinearLayout) findViewById(R.id.layout_login_register);
		btn_login_submit = (GeniusButton) findViewById(R.id.btn_login_submit);

		layout_login_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});

		layout_login_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}

		});

		btn_login_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String username = edt_login_username.getText().toString();
				final String password = edt_login_password.getText().toString();
				
				if (dialog == null) dialog = new ProgressDialog(LoginActivity.this);
				dialog.setTitle("µÇÂ½");
				dialog.setMessage("µÇÂ½ÖÐ£¬ÇëÉÔºó...");
				//dialog.setCancelable(false);
				dialog.show();

				new Thread(new Runnable() {
					@Override
					public void run() {
						UserService user_service = new UserServiceImp();
						try {
							user_service.Login(username, password);
							User user = user_service.GetUser(username, password);
							Bundle bundle = new Bundle();
							bundle.putSerializable("user", user);
							Message msg = new Message();
							msg.what = 1;
							msg.setData(bundle);
							handler.sendMessage(msg);
						} catch (ServiceException e) {
							Message msg = new Message();
							msg.what = 0;
							Bundle data = new Bundle();
							data.putSerializable("ERROR_MSG", e.getMessage());
							msg.setData(data);
							handler.sendMessage(msg);
						} catch (Exception e) {
							Message msg = new Message();
							msg.what = 0;
							Bundle data = new Bundle();
							data.putSerializable("ERROR_MSG", "µÇÂ¼³ö´í£¬ÍøÂçÁ¬½ÓÊ§°Ü");
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
		setContentView(R.layout.activity_login);
		init();
	}
	
	
}
