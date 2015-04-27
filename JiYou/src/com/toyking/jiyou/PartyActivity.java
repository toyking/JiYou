package com.toyking.jiyou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.toyking.jiyou.model.Party;
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

public class PartyActivity extends Activity {
	
	private LinearLayout layout_party_add = null;
	private LinearLayout layout_party_back = null;
	
	private ListView list_party = null;
	private PartyAdapter party_adapter = null; // 适配器
	private ArrayList<Map<String, Object>> data = null; // 数据源
	
	private User user = null;
	
	private ArrayList<Map<String, Object>> arrayList_party = null;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String error_msg = msg.getData().getString("ERROR_MSG") .toString();
				Toast.makeText(PartyActivity.this, error_msg, Toast.LENGTH_SHORT) .show();
				break;
			case 1:
				// 数据源添加数据
				data.clear();
				for (int i = arrayList_party.size() - 1; i >= 0; i--) {
					Map<String, Object> mp = arrayList_party.get(i);
					Party party = (Party) mp.get("party");
					User publisher = (User) mp.get("publisher");
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("party_id", party.getId());
					item.put("party_theme", party.getTheme());
					item.put("party_time", party.getTime());
					item.put("party_address", party.getAddress());
					item.put("party_peoplenum", "活动需要人数："+party.getPeoplenum());
					item.put("party_publish_time", party.getPublish_time() +" 发布");
					item.put("party_person", publisher.getRealname() + publisher.getCompanyJobWithOr());
					data.add(item);
				}
				party_adapter.notifyDataSetChanged();
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
		
		layout_party_back = (LinearLayout) findViewById(R.id.layout_party_back);
		layout_party_add = (LinearLayout) findViewById(R.id.layout_party_add);
		list_party = (ListView) findViewById(R.id.list_party);
		
		// 构建你的适配器
		data = new ArrayList<Map<String, Object>>();// 构建你的数据源
		String from[]=new String[] { "party_theme", "party_time", "party_address", "party_peoplenum", "party_publish_time" ,"party_person"};
		int to[]=new int[] { R.id.txt_party_item_theme, R.id.txt_party_item_time, R.id.txt_party_item_address, R.id.txt_party_item_peoplenum, R.id.txt_party_item_publish_time, R.id.txt_party_item_person};
		party_adapter = new PartyAdapter(PartyActivity.this, data, R.layout.activity_party_item, from, to);
		list_party.setAdapter(party_adapter);
		
		layout_party_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		layout_party_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				Intent intent = new Intent(PartyActivity.this, PartyAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_party);
		
		init();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				UserService user_service = new UserServiceImp();
				try {
					arrayList_party = user_service.GetPartyList(user);
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
					data.putSerializable("ERROR_MSG", "获取活动出错，网络连接失败");
					msg.setData(data);
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	private class PartyAdapter extends SimpleAdapter {
		
		private Context context_current = null;
		private List<Map<String, Object>> list_data = null;

		public PartyAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			list_data = data;
			context_current = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			final Map<String, Object> mp = list_data.get(position);
			
			/*TextView txt_party_item_person = (TextView) view.findViewById(R.id.txt_party_item_person);
			GetPartyPersonAsyncTask task=new GetPartyPersonAsyncTask(txt_party_item_person, mp.get("party_username").toString());
			task.execute(3000);*/
			
			LinearLayout layout_party_detail = (LinearLayout) view.findViewById(R.id.layout_party_detail);

			layout_party_detail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context_current, PartyDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("user", user);
					bundle.putInt("party_id", (Integer) mp.get("party_id"));
					intent.putExtras(bundle);
					context_current.startActivity(intent);
				}
			});
			return view;
		}
		
		/*private class GetPartyPersonAsyncTask extends AsyncTask<Integer, Integer, String>{

			private TextView textView = null ;
			private String username = null;
			
			public GetPartyPersonAsyncTask(TextView textView,String username) {  
		        super();
				this.textView = textView;
				this.username = username;
		    } 

			@Override
			protected String doInBackground(Integer... params) {
				try{
					UserService user_service = new UserServiceImp();
					User party_person = user_service.GetPublicUserInfoByUsername(username);
					if (party_person.getCompany() == null || party_person.getJob() == null)  return party_person.getRealname();
					else return party_person.getRealname() + " | " + party_person.getCompany() + party_person.getJob();
				}catch(Exception e){
					return null;
				}
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				textView.setText(result);
			}
		}*/
	}
}
