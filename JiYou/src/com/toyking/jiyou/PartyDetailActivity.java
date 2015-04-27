package com.toyking.jiyou;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.toyking.jiyou.model.Party;
import com.toyking.jiyou.model.PartyAttender;
import com.toyking.jiyou.model.User;
import com.toyking.jiyou.service.ServiceException;
import com.toyking.jiyou.service.UserService;
import com.toyking.jiyou.service.UserServiceImp;

import net.qiujuer.genius.widget.GeniusButton;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PartyDetailActivity extends Activity {

	private int party_id = -1;
	private User user = null;
	
	private ArrayList<Map<String, Object>> list_party_attender = null;
	private PartyAttenderAdapter adpter_party_attender = null; // 适配器
	private ArrayList<Map<String, Object>> data = null; // 数据源

	private TextView txt_party_detail_publisher_realname = null;
	private TextView txt_party_detail_publisher_companyjob = null;
	private TextView txt_party_detail_publisher_time = null;
	private TextView txt_party_detail_theme = null;
	private TextView txt_party_detail_time = null;
	private TextView txt_party_detail_address = null;
	private TextView txt_party_detail_peoplenum = null;
	private TextView txt_party_detail_content = null;
	private LinearLayout layout_party_detail_cancel = null;
	private GeniusButton btn_party_detail_attend = null;
	private ListViewForScrollView layout_party_attender_list = null;
	private ScrollView layout_party_detail_main = null;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String error_msg = msg.getData().getSerializable("ERROR_MSG").toString();
				Toast.makeText(PartyDetailActivity.this, error_msg,Toast.LENGTH_SHORT).show();
				break;
			case 1:
				for(int i=0;i<list_party_attender.size();i++){
					Map<String, Object> item = new HashMap<String, Object>();
					Map<String, Object> party_attender = list_party_attender.get(i);
					User party_attender_user = (User) party_attender.get("party_attender_user");
					item.put("txt_party_attender_realname", party_attender_user.getRealname());
					item.put("txt_party_attender_companyjob", party_attender_user.getCompanyJobWithOr() );
					item.put("txt_party_attender_time", "报名时间：" + party_attender.get("party_attender_time").toString());
					data.add(item);
				}
				adpter_party_attender.notifyDataSetChanged();
				
				Party party = (Party) msg.getData().getSerializable("party");
				txt_party_detail_theme.setText(party.getTheme());
				txt_party_detail_time.setText(party.getTime());
				txt_party_detail_address.setText(party.getAddress());
				txt_party_detail_peoplenum.setText(party.getPeoplenum() + "");
				txt_party_detail_content.setText(party.getDescription());
				
				User publisher = (User) msg.getData().getSerializable("publisher");
				txt_party_detail_publisher_realname.setText(publisher.getRealname());
				txt_party_detail_publisher_companyjob.setText(publisher.getCompanyJobWithOr());
				txt_party_detail_publisher_time.setText("发布时间：" + party.getPublish_time());
				
				layout_party_detail_main.setVisibility(View.VISIBLE);
				
				break;
			
			case 2:
				User party_attender_user = user;
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("txt_party_attender_realname", party_attender_user.getRealname());
				item.put("txt_party_attender_companyjob", party_attender_user.getCompanyJobWithOr() );
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				item.put("txt_party_attender_time", "报名时间：" + formatter.format(curDate) );
				data.add(item);
				adpter_party_attender.notifyDataSetChanged();
				Toast.makeText(PartyDetailActivity.this, "报名成功！",Toast.LENGTH_SHORT).show();
			default:
				break;
			}
		}
	};

	private void init() {
		Intent intent = getIntent();
		Bundle budle = intent.getExtras();
		user = (User) budle.getSerializable("user");
		party_id = budle.getInt("party_id", -1);
		
		txt_party_detail_publisher_realname = (TextView) findViewById(R.id.txt_party_detail_pulisher_realname);
		txt_party_detail_publisher_companyjob = (TextView) findViewById(R.id.txt_party_detail_pulisher_companyjob);
		txt_party_detail_publisher_time = (TextView) findViewById(R.id.txt_party_detail_pulisher_time);
		txt_party_detail_theme = (TextView) findViewById(R.id.txt_party_detail_theme);
		txt_party_detail_time = (TextView) findViewById(R.id.txt_party_detail_time);
		txt_party_detail_address = (TextView) findViewById(R.id.txt_party_detail_address);
		txt_party_detail_peoplenum = (TextView) findViewById(R.id.txt_party_detail_peoplenum);
		txt_party_detail_content = (TextView) findViewById(R.id.txt_party_detail_content);
		layout_party_detail_cancel = (LinearLayout) findViewById(R.id.layout_party_detail_back);
		btn_party_detail_attend = (GeniusButton) findViewById(R.id.btn_party_detail_attend);
		layout_party_attender_list = (ListViewForScrollView) findViewById(R.id.layout_party_attender_list);
		layout_party_detail_main = (ScrollView) findViewById(R.id.layout_party_detail_main);
		
		String from[] =new String[] {"txt_party_attender_realname", "txt_party_attender_companyjob", "txt_party_attender_time"};
		int to[] = new int[] {R.id.txt_party_attender_realname, R.id.txt_party_attender_companyjob, R.id.txt_party_attender_time };
		data = new ArrayList<Map<String,Object>>();
		adpter_party_attender = new PartyAttenderAdapter(PartyDetailActivity.this, data, R.layout.activity_party_attender_item, from, to);
		layout_party_attender_list.setAdapter(adpter_party_attender);
		
		layout_party_detail_main.setVisibility(View.GONE);
		
		layout_party_detail_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btn_party_detail_attend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							UserService user_service = new UserServiceImp();
							PartyAttender party_attender = new PartyAttender();
							party_attender.setUsername(user.getUsername() );
							party_attender.setParty_id(party_id);
							user_service.PartyAttenderAdd(party_attender,user);
							handler.sendEmptyMessage(2);
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
							data.putSerializable("ERROR_MSG", "报名失败，网络连接失败");
							msg.setData(data);
							handler.sendMessage(msg);
						}
					}
				}).start();
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					UserService user_service = new UserServiceImp();
					Party party = user_service.GetPartyById(party_id);
					User publisher = user_service.GetPublicUserInfoByUsername(party.getUsername());
					list_party_attender = user_service.GetPartyAttenderList(party_id);
					Bundle bundle = new Bundle();
					bundle.putSerializable("party", party);
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
					data.putSerializable("ERROR_MSG", "获取活动详情出错，网络连接失败");
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
		setContentView(R.layout.activity_party_detail);

		init();
	}

	private class PartyAttenderAdapter extends SimpleAdapter {

		/*private Context context_current = null;
		private LayoutInflater inflater = null;  
		private List<Map<String, Object>> list_data = null;*/

		public PartyAttenderAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			/*list_data = data;
			context_current = context;*/
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			return view;
		}
		
		
	}

}
