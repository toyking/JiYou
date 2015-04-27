package com.toyking.jiyou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.toyking.jiyou.model.Job;
import com.toyking.jiyou.model.User;
import com.toyking.jiyou.service.ServiceException;
import com.toyking.jiyou.service.UserService;
import com.toyking.jiyou.service.UserServiceImp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class JobActivity extends Activity {
	
	private User user = null;
	
	private JobAdapter job_adapter = null; // 适配器
	private ArrayList<Map<String, Object>> data = null; // 数据源
	
	private LinearLayout layout_job_add = null;
	private LinearLayout layout_job_back = null;
	private ListView list_job = null;
	
	private ArrayList<Map<String, Object>> arrayList_job = null;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String error_msg = msg.getData().getString("ERROR_MSG") .toString();
				Toast.makeText(JobActivity.this, error_msg, Toast.LENGTH_SHORT) .show();
				break;
			case 1:
				// 数据源添加数据
				data.clear();
				for (int i = arrayList_job.size() - 1; i >= 0; i--) {
					Map<String, Object> mp = arrayList_job.get(i);
					Job job= (Job) mp.get("job");
					User publisher = (User) mp.get("publisher");
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("job_id", job.getId());
					item.put("txt_job_item_job", job.getJob() );
					item.put("txt_job_item_companyInfo", job.getCompanyInfo());
					item.put("txt_job_item_pulisher", publisher.getRealname() + publisher.getCompanyJobWithOr());
					data.add(item);
				}
				job_adapter.notifyDataSetChanged();
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

		layout_job_back = (LinearLayout) findViewById(R.id.layout_job_back);
		layout_job_add = (LinearLayout) findViewById(R.id.layout_job_add);
		list_job = (ListView) findViewById(R.id.list_job);
		
		// 构建你的适配器
		data = new ArrayList<Map<String, Object>>();// 构建你的数据源
		String from[]=new String[] { "txt_job_item_job", "txt_job_item_companyInfo", "txt_job_item_pulisher"};
		int to[]=new int[] { R.id.txt_job_item_job, R.id.txt_job_item_company, R.id.txt_job_item_pulisher};
		job_adapter = new JobAdapter(JobActivity.this, data, R.layout.activity_job_item, from, to);
		list_job.setAdapter(job_adapter);
		
		layout_job_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		layout_job_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				Intent intent = new Intent(JobActivity.this, JobAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				UserService user_service = new UserServiceImp();
				try {
					arrayList_job = user_service.GetJobList(user);
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
					data.putSerializable("ERROR_MSG", "获取实习出错，网络连接失败");
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
		setContentView(R.layout.activity_job);
		
		init();
	}
	
	
	private class JobAdapter extends SimpleAdapter {

		private Context context_current = null;
		private List<Map<String, Object>> list_data = null;

		public JobAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			list_data = data;
			context_current = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			final Map<String, Object> mp = list_data.get(position);
			
			LinearLayout layout_job_detail = (LinearLayout) view.findViewById(R.id.layout_job_detail);

			layout_job_detail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context_current, JobDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("user", user);
					bundle.putInt("job_id", (Integer) mp.get("job_id") );
					intent.putExtras(bundle);
					context_current.startActivity(intent);
				}
			});
			return view;
		}
	}
}
