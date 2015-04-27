package com.toyking.jiyou;

import com.toyking.jiyou.model.Job;
import com.toyking.jiyou.model.User;
import com.toyking.jiyou.service.ServiceException;
import com.toyking.jiyou.service.UserService;
import com.toyking.jiyou.service.UserServiceImp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class JobDetailActivity extends Activity {
	
	private int job_id = -1;
	//private User user = null;
	
	private LinearLayout layout_job_detail_back = null;
	private ScrollView layout_job_detail_main = null;
	private TextView txt_job_detail_job = null;
	private TextView txt_job_detail_company = null;
	private TextView txt_job_detail_description = null;
	private TextView txt_job_detail_requirement = null;
	private TextView txt_job_detail_publisher_realname = null;
	private TextView txt_job_detail_publisher_companyjob = null;
	private TextView txt_job_detail_publisher_time= null;
	private TextView txt_job_detail_publisher_email = null;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String error_msg = msg.getData().getSerializable("ERROR_MSG").toString();
				Toast.makeText(JobDetailActivity.this, error_msg,Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Job job = (Job) msg.getData().getSerializable("job");
				User publisher = (User) msg.getData().getSerializable("publisher");
				txt_job_detail_job.setText(job.getJob());
				txt_job_detail_company.setText(job.getCompanyInfo());
				txt_job_detail_description.setText(job.getDescription());
				txt_job_detail_requirement.setText(job.getRequirement());
				txt_job_detail_publisher_realname.setText(publisher.getRealname());
				txt_job_detail_publisher_companyjob.setText(publisher.getCompanyJobWithOr());
				txt_job_detail_publisher_email.setText("简历邮箱：" + job.getEmail());
				txt_job_detail_publisher_time.setText("发布时间：" + job.getPublishtime());
				layout_job_detail_main.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	};
	
	private void init(){
		Intent intent = getIntent();
		Bundle budle = intent.getExtras();
		//user = (User) budle.getSerializable("user");
		job_id = budle.getInt("job_id", -1);
		
		layout_job_detail_back = (LinearLayout) findViewById(R.id.layout_job_detail_back);
		layout_job_detail_main = (ScrollView) findViewById(R.id. layout_job_detail_main);
		txt_job_detail_job = (TextView) findViewById(R.id.txt_job_detail_job);
		txt_job_detail_company = (TextView) findViewById(R.id.txt_job_detail_compnay);
		txt_job_detail_description = (TextView) findViewById(R.id.txt_job_detail_description);
		txt_job_detail_requirement = (TextView) findViewById(R.id.txt_job_detail_requirement);
		txt_job_detail_publisher_realname = (TextView) findViewById(R.id.txt_job_detail_pulisher_realname);
		txt_job_detail_publisher_companyjob = (TextView) findViewById(R.id.txt_job_detail_pulisher_companyjob);
		txt_job_detail_publisher_email = (TextView) findViewById(R.id.txt_job_detail_pulisher_email);
		txt_job_detail_publisher_time = (TextView) findViewById(R.id.txt_job_detail_pulisher_time);
		
		layout_job_detail_main.setVisibility(View.GONE);
		
		layout_job_detail_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					UserService user_service = new UserServiceImp();
					Job job = user_service.GetJobById(job_id);
					User publisher = user_service.GetPublicUserInfoByUsername(job.getPublisher());
					Bundle bundle = new Bundle();
					bundle.putSerializable("job", job);
					bundle.putSerializable("publisher", publisher);
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
					data.putSerializable("ERROR_MSG", "获取职位详情出错，网络连接失败");
					msg.setData(data);
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_job_detail);
		
		init();
	}
	
	
}
