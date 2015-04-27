package com.toyking.jiyou;

import com.toyking.jiyou.model.Party;
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

public class PartyAddActivity extends Activity {
	
	private LinearLayout layout_party_add_cancel = null;
	private GeniusButton btn_party_add_submit = null;
	private GeniusEditText edt_party_add_theme = null;
	private GeniusEditText edt_party_add_time = null;
	private GeniusEditText edt_party_add_address = null;
	private GeniusEditText edt_party_add_peoplenum = null;
	private GeniusEditText edt_party_add_description = null;
	
	private User user = null;
	public ProgressDialog dialog = null;

    @SuppressLint("HandlerLeak") private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if (dialog != null && dialog.isShowing()) dialog.dismiss();
            switch (msg.what) {
                case 0:
                    String error_msg = msg.getData().getSerializable("ERROR_MSG").toString();
                    Toast.makeText(PartyAddActivity.this, error_msg, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(PartyAddActivity.this, "发布活动成功", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(PartyAddActivity.this, PartyActivity.class);
    				Bundle bundle = new Bundle();
    				bundle.putSerializable("user", user);
    				intent.putExtras(bundle);
    				startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
	
	private void  init(){
		Intent intent = getIntent();
		Bundle budle = intent.getExtras();
		user = (User) budle.getSerializable("user");
		layout_party_add_cancel = (LinearLayout) findViewById(R.id.layout_party_add_cancel);
		btn_party_add_submit = (GeniusButton) findViewById(R.id.btn_party_add_submit);
		edt_party_add_theme = (GeniusEditText) findViewById(R.id.edt_party_add_theme);
		edt_party_add_time = (GeniusEditText) findViewById(R.id.edt_party_add_time);
		edt_party_add_address = (GeniusEditText) findViewById(R.id.edt_party_add_address);
		edt_party_add_peoplenum = (GeniusEditText) findViewById(R.id.edt_party_add_peoplenum);
		edt_party_add_description = (GeniusEditText) findViewById(R.id.edt_party_add_description);
		
		layout_party_add_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_party_add_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Party party = new Party();
				// party.setId(id) from server
				// party.setPublish_time(publish_time) from server
				party.setAddress(edt_party_add_address.getText().toString());
				party.setDescription(edt_party_add_description.getText().toString());
				party.setPeoplenum(Integer.parseInt(edt_party_add_peoplenum.getText().toString()));
				party.setTheme(edt_party_add_theme.getText().toString());
				party.setTime(edt_party_add_time.getText().toString());
				party.setUsername(user.getUsername());
				
				if (dialog == null) dialog = new ProgressDialog(PartyAddActivity.this);
				dialog.setTitle("发活动");
				dialog.setMessage("活动发布中，请稍后...");
				//dialog.setCancelable(false);
				dialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						UserService user_service = new UserServiceImp();
						try {
							user_service.PartyAdd(party, user);
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
							data.putSerializable("ERROR_MSG", "发布活动出错，网络连接失败");
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
		setContentView(R.layout.activity_party_add);
		
		init();
	}

}
