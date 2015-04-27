package com.toyking.jiyou;

import net.qiujuer.genius.widget.GeniusButton;
import net.qiujuer.genius.widget.GeniusEditText;

import com.toyking.jiyou.model.Job;
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

public class JobAddActivity extends Activity {

	private LinearLayout layout_job_add_cancel = null;
	private GeniusButton btn_job_add_submit = null;
	private GeniusEditText edt_job_add_job = null;
	private GeniusEditText edt_job_add_company = null;
	private GeniusEditText edt_job_add_city = null;
	private GeniusEditText edt_job_add_salary = null;
	private GeniusEditText edt_job_add_email = null;
	private GeniusEditText edt_job_add_description = null;
	private GeniusEditText edt_job_add_requirement = null;
	
	private User user = null;
	public ProgressDialog dialog = null;
	
	@SuppressLint("HandlerLeak") private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if (dialog != null && dialog.isShowing()) dialog.dismiss();
            switch (msg.what) {
                case 0:
                    String error_msg = msg.getData().getSerializable("ERROR_MSG").toString();
                    Toast.makeText(JobAddActivity.this, error_msg, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(JobAddActivity.this, "发布实习成功", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(JobAddActivity.this, JobActivity.class);
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
    
    private void init(){
    	Intent intent = getIntent();
		Bundle budle = intent.getExtras();
		user = (User) budle.getSerializable("user");
		
		layout_job_add_cancel = (LinearLayout) findViewById(R.id.layout_job_add_cancel);
		btn_job_add_submit = (GeniusButton) findViewById(R.id.btn_job_add_submit);
		edt_job_add_job = (GeniusEditText) findViewById(R.id.edt_job_add_job);
		edt_job_add_company = (GeniusEditText) findViewById(R.id.edt_job_add_company);
		edt_job_add_city = (GeniusEditText) findViewById(R.id.edt_job_add_city);
		edt_job_add_salary = (GeniusEditText) findViewById(R.id.edt_job_add_salary);
		edt_job_add_email = (GeniusEditText) findViewById(R.id.edt_job_add_email);
		edt_job_add_description = (GeniusEditText) findViewById(R.id.edt_job_add_description);
		edt_job_add_requirement = (GeniusEditText) findViewById(R.id.edt_job_add_requirement);
		
		layout_job_add_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		btn_job_add_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Job job = new Job();
				//job.setId(id) from server
				//party.setPublish_time(publish_time) from server
				job.setJob(edt_job_add_job.getText().toString());
				job.setCompany(edt_job_add_company.getText().toString());
				job.setCity(edt_job_add_city.getText().toString());
				job.setSalary(edt_job_add_salary.getText().toString());
				job.setEmail(edt_job_add_email.getText().toString());
				job.setDescription(edt_job_add_description.getText().toString());
				job.setRequirement(edt_job_add_requirement.getText().toString());
				job.setPublisher(user.getUsername());
				
				if (dialog == null) dialog = new ProgressDialog(JobAddActivity.this);
				dialog.setTitle("发实习");
				dialog.setMessage("实习发布中，请稍后...");
				//dialog.setCancelable(false);
				dialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						UserService user_service = new UserServiceImp();
						try {
							user_service.JobAdd(job, user);
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
							data.putSerializable("ERROR_MSG", "发布实习出错，网络连接失败");
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
		setContentView(R.layout.activity_job_add);
		
		init();
	}
	
	
	
}
