package com.toyking.jiyou;

import net.qiujuer.genius.widget.GeniusEditText;

import com.toyking.jiyou.model.SquareStatus;
import com.toyking.jiyou.model.User;
import com.toyking.jiyou.service.ServiceException;
import com.toyking.jiyou.service.UserService;
import com.toyking.jiyou.service.UserServiceImp;

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

public class SquareAddStatusActivity extends Activity {
	
	private LinearLayout layout_square_add_status_cancel = null;
	private LinearLayout layout_square_add_status_ok = null;
	private GeniusEditText edt_square_add_status_content = null;
	
	private User user = null;
	
	public ProgressDialog dialog = null;

    @SuppressLint("HandlerLeak") private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if (dialog != null && dialog.isShowing()) dialog.dismiss();
            switch (msg.what) {
                case 0:
                    String error_msg = msg.getData().getSerializable("ERROR_MSG").toString();
                    Toast.makeText(SquareAddStatusActivity.this, error_msg, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(SquareAddStatusActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
	
	private void init(){
		Intent intent = getIntent();
		Bundle budle = intent.getExtras();
		user = (User) budle.getSerializable("user");
		layout_square_add_status_cancel = (LinearLayout) findViewById(R.id.layout_square_add_status_cancel);
		layout_square_add_status_ok = (LinearLayout) findViewById(R.id.layout_square_add_status_ok);
		edt_square_add_status_content = (GeniusEditText) findViewById(R.id.edt_square_add_status_content);
		
		layout_square_add_status_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		layout_square_add_status_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final SquareStatus square_status =new SquareStatus();
				//square_status.setId(id) from server
				//square_status.setTime(time) from server
				square_status.setUsername(user.getUsername());
				square_status.setContent(edt_square_add_status_content.getText().toString());
				if (dialog == null) dialog = new ProgressDialog(SquareAddStatusActivity.this);
				dialog.setTitle("发状态");
				dialog.setMessage("状态发布中，请稍后...");
				//dialog.setCancelable(false);
				dialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						UserService user_service = new UserServiceImp();
						try {
							user_service.SquareAddStatus(square_status, user);
							handler.sendEmptyMessage(1);
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
							data.putSerializable("ERROR_MSG", "发状态出错，网络连接失败");
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
		setContentView(R.layout.activity_square_add_status);
		
		init();
	}
}
