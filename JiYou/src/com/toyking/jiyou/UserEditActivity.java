package com.toyking.jiyou;

import com.toyking.jiyou.model.User;
import com.toyking.jiyou.service.ServiceException;
import com.toyking.jiyou.service.UserService;
import com.toyking.jiyou.service.UserServiceImp;

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

public class UserEditActivity extends Activity {

	private User user = null;
	
	private LinearLayout layout_user_edit_ok = null;
	private LinearLayout layout_user_edit_cancel = null;
	
	private GeniusEditText edt_user_edit_realname = null;
	private GeniusEditText edt_user_edit_sex = null;
	private GeniusEditText edt_user_edit_company = null;
	private GeniusEditText edt_user_edit_job = null;
	private GeniusEditText edt_user_edit_city = null;
	private GeniusEditText edt_user_edit_address = null;
	private GeniusEditText edt_user_edit_phone = null;
	private GeniusEditText edt_user_edit_email = null;
	private GeniusEditText edt_user_edit_qq = null;
	private GeniusEditText edt_user_edit_studentid = null;
	
	public ProgressDialog dialog = null;
	
	@SuppressLint("HandlerLeak") private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if (dialog != null && dialog.isShowing()) dialog.dismiss();
            switch (msg.what) {
                case 0:
                    String error_msg = msg.getData().getSerializable("ERROR_MSG").toString();
                    Toast.makeText(UserEditActivity.this, error_msg, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(UserEditActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.putExtras(msg.getData());
					setResult(RESULT_OK, intent);
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

		layout_user_edit_ok = (LinearLayout) findViewById(R.id.layout_user_edit_ok);
		layout_user_edit_cancel = (LinearLayout) findViewById(R.id.layout_user_edit_cancel);
		
		edt_user_edit_realname = (GeniusEditText) findViewById(R.id.edt_user_edit_realname);
		edt_user_edit_sex = (GeniusEditText) findViewById(R.id.edt_user_edit_sex);
		edt_user_edit_company = (GeniusEditText) findViewById(R.id.edt_user_edit_company);
		edt_user_edit_job = (GeniusEditText) findViewById(R.id.edt_user_edit_job);
		edt_user_edit_city = (GeniusEditText) findViewById(R.id.edt_user_edit_city);
		edt_user_edit_address = (GeniusEditText) findViewById(R.id.edt_user_edit_address);
		edt_user_edit_phone = (GeniusEditText) findViewById(R.id.edt_user_edit_phone);
		edt_user_edit_email = (GeniusEditText) findViewById(R.id.edt_user_edit_email);
		edt_user_edit_qq = (GeniusEditText) findViewById(R.id.edt_user_edit_qq);
		edt_user_edit_studentid = (GeniusEditText) findViewById(R.id.edt_user_edit_studentid);

		edt_user_edit_realname.setText(user.getRealname());
		edt_user_edit_sex.setText(user.getSex());
		edt_user_edit_company.setText(user.getCompany());
		edt_user_edit_job.setText(user.getJob());
		edt_user_edit_city.setText(user.getCity());
		edt_user_edit_address.setText(user.getAddress());
		edt_user_edit_phone.setText(user.getPhone());
		edt_user_edit_email.setText(user.getEmail());
		edt_user_edit_qq.setText(user.getQq());
		edt_user_edit_studentid.setText(user.getStudentid());
		
		layout_user_edit_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog == null) dialog = new ProgressDialog(UserEditActivity.this);
				dialog.setTitle("更新个人信息");
				dialog.setMessage("正在更新信息，请稍后...");
				dialog.show();
				
				user.setRealname(edt_user_edit_realname.getText().toString());
				user.setSex(edt_user_edit_sex.getText().toString());
				user.setCompany(edt_user_edit_company.getText().toString());
				user.setJob(edt_user_edit_job.getText().toString());
				user.setCity(edt_user_edit_city.getText().toString());
				user.setAddress(edt_user_edit_address.getText().toString());
				user.setPhone(edt_user_edit_phone.getText().toString());
				user.setEmail(edt_user_edit_email.getText().toString());
				user.setQq(edt_user_edit_qq.getText().toString());
				user.setStudentid(edt_user_edit_studentid.getText().toString());

				new Thread(new Runnable() {
					@Override
					public void run() {
						UserService user_service = new UserServiceImp();
						try {
							user_service.EditUser(user);
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
							data.putSerializable("ERROR_MSG", "完善个人信息出错，网络连接失败");
							msg.setData(data);
							handler.sendMessage(msg);
						}

					}
				}).start();
			}
		});
		
		layout_user_edit_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_edit);
		
		init();
	}
	
	
}
