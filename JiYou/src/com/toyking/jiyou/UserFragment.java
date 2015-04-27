package com.toyking.jiyou;


import com.toyking.jiyou.model.User;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserFragment extends Fragment{
	
	public final static int request_code_user_edit = 1;
	
	private LinearLayout layout_user_setting = null;
	private LinearLayout layout_user_edit_profile = null;
	private LinearLayout layout_user_exit = null;
	private TextView txt_user_realname = null;
	private TextView txt_user_username = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_user, container, false);
		layout_user_setting = (LinearLayout) view.findViewById(R.id.layout_user_setting);
		layout_user_edit_profile = (LinearLayout) view.findViewById(R.id.layout_user_edit_profile);
		layout_user_exit = (LinearLayout) view.findViewById(R.id.layout_user_exit);
		txt_user_realname = (TextView) view.findViewById(R.id.txt_user_realname);
		txt_user_username = (TextView) view.findViewById(R.id.txt_user_username);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		layout_user_edit_profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),UserEditActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", ((MainActivity) getActivity()).getUser());
				intent.putExtras(bundle);
				startActivityForResult(intent, request_code_user_edit);
			}
		});
		
		layout_user_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),UserSettingActivity.class);
				startActivity(intent);
			}
		});
		
		layout_user_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				(getActivity()).finish();
			}
		});
		
		
		txt_user_realname.setText( ((MainActivity) getActivity()).getUser().getRealname());
		txt_user_username.setText("¼ÆÓÑºÅ£º" + ((MainActivity) getActivity()).getUser().getUsername());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case request_code_user_edit:
				if(resultCode == android.app.Activity.RESULT_OK){
					Bundle bundle = data.getExtras();
					User user = (User) bundle.getSerializable("user");
					((MainActivity) getActivity()).setUser(user);
					txt_user_realname.setText( ((MainActivity) getActivity()).getUser().getRealname());
					txt_user_username.setText("¼ÆÓÑºÅ£º" + ((MainActivity) getActivity()).getUser().getUsername());
				}
				break;

			default:
				break;
		}
		
	}

}
