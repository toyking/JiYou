package com.toyking.jiyou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.toyking.jiyou.model.SquareStatus;
import com.toyking.jiyou.model.User;
import com.toyking.jiyou.service.ServiceException;
import com.toyking.jiyou.service.UserService;
import com.toyking.jiyou.service.UserServiceImp;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.widget.Toast;

public class SquareFragment extends ListFragment{
	
	private SquareAdapter squareAdapter = null; // 适配器
	private ArrayList<Map<String, Object>> data = null; // 数据源
	
	private LinearLayout layout_square_add = null;
	private ArrayList<Map<String, Object>> square_status_list = null;
	
    @SuppressLint("HandlerLeak") private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String error_msg = msg.getData().getString("ERROR_MSG").toString();
                    Toast.makeText(getActivity(), error_msg, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                	// 数据源添加数据
                	data.clear();
					for (int i = square_status_list.size() - 1; i >= 0; i--) {
						Map<String, Object> mp = square_status_list.get(i);
						SquareStatus square_status = (SquareStatus) mp.get("square_status");
						User square_person = (User) mp.get("square_person");
						String square_prize = mp.get("square_prize").toString();
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("txt_square_username", square_status.getUsername());
						item.put("txt_square_content", square_status.getContent());
						item.put("txt_square_time", square_status.getTime());
						item.put("txt_square_id", square_status.getId());
						item.put("txt_square_realname",square_person.getRealname());
						item.put("txt_square_prize",square_prize);
						if(square_person.getCompany()==null || square_person.getJob()== null ) item.put("txt_square_companyjob", "");
						else item.put("txt_square_companyjob", " | " + square_person.getCompany() + square_person.getJob() );
						if(square_person.getCity()==null) item.put("txt_square_city", "");
						else item.put("txt_square_city", square_person.getCity());
						data.add(item);
					}
					squareAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_square, container, false);
		layout_square_add = (LinearLayout) view.findViewById(R.id.layout_square_add);
		return view;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		data = new ArrayList<Map<String, Object>>();// 构建你的数据源
		
		String from[] =new String[] { "txt_square_content", "txt_square_time", "txt_square_realname", "txt_square_companyjob", "txt_square_city" , "txt_square_prize"};
		int to[] = new int[] { R.id.txt_square_content, R.id.txt_square_time, R.id.txt_square_realname, R.id.txt_square_companyjob, R.id.txt_square_city, R.id.txt_square_prize};
		
		// 构建你的适配器
		squareAdapter = new SquareAdapter(getActivity(), data, R.layout.fragment_square_item, from, to);
		setListAdapter(squareAdapter);
		
		layout_square_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SquareAddFragment fragment=SquareAddFragment.getInstance();
				fragment.show(getFragmentManager(), "发布消息");
			}
		});
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				UserService user_service = new UserServiceImp();
				try {
					square_status_list = user_service.GetSquareStatusList(((MainActivity) getActivity()).getUser());
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
					data.putSerializable("ERROR_MSG", "获取状态出错，网络连接失败");
					msg.setData(data);
					handler.sendMessage(msg);
				}
			}
		}).start();
		
	}



	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@SuppressLint("HandlerLeak") public class SquareAdapter extends SimpleAdapter{

		private List<Map<String, Object>> list_data = null;
		private Context context_current=null;

		public SquareAdapter(Context context, List<Map<String, Object>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			list_data = data;
			context_current = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			
			final Map<String, Object> mp = list_data.get(position);
			final int pos=position;
			LinearLayout layout_square_prize = (LinearLayout) view.findViewById(R.id.layout_square_prize);
			LinearLayout layout_square_comment = (LinearLayout) view.findViewById(R.id.layout_square_comment);
			final LinearLayout layout_square_prize_list = (LinearLayout) view.findViewById(R.id.layout_square_prize_list);
			final TextView txt_square_prize = (TextView) view.findViewById(R.id.txt_square_prize);
			if(txt_square_prize.getText().toString().equals("")){
				layout_square_prize_list.setVisibility(View.GONE);
			}else{
				layout_square_prize_list.setVisibility(View.VISIBLE);
			}
			
			final Handler handler_item=new Handler(){

				@Override
				public void handleMessage(Message msg) { 
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					
					switch (msg.what) {
					case 0:
						String error_msg = msg.getData().getSerializable("ERROR_MSG").toString();
						Toast.makeText(context_current, error_msg, Toast.LENGTH_SHORT).show();
						break;
					case 1:
						String txtString=txt_square_prize.getText().toString();
						layout_square_prize_list.setVisibility(View.VISIBLE);
						if(txtString.equals("")){
							txt_square_prize.setText(((MainActivity)context_current).getUser().getRealname());
						}else{
							txt_square_prize.setText(txtString + "，" +((MainActivity)context_current).getUser().getRealname());
						}
						mp.put("txt_square_prize", txt_square_prize.getText().toString());
						data.set(pos, mp);
						break;
					default:
						break;
					}
				}
				
			};

			layout_square_prize.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							UserService user_service = new UserServiceImp();
							try {
								user_service.SquarePrizeAdd( (Integer) mp.get("txt_square_id") , ( (MainActivity)context_current).getUser() );
								handler_item.sendEmptyMessage(1);
							} catch (ServiceException e) {
								Message msg = new Message();
								msg.what = 0;
								Bundle data = new Bundle();
								data.putSerializable("ERROR_MSG", e.getMessage());
								msg.setData(data);
								handler_item.sendMessage(msg);
							} catch (Exception e) {
								e.printStackTrace();
								Message msg = new Message();
								msg.what = 0;
								Bundle data = new Bundle();
								data.putSerializable("ERROR_MSG", "点赞失败，网络连接失败");
								msg.setData(data);
								handler_item.sendMessage(msg);
							}
						}
					}).start();
				}
			});
			
			layout_square_comment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Toast.makeText(context_current, mp.get("期待中").toString(), Toast.LENGTH_SHORT).show();
				}
			});
			
			return view;
		}
		
	}
 
	
}
