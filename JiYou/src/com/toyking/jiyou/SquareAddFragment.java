package com.toyking.jiyou;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SquareAddFragment extends DialogFragment{
	
	private LinearLayout layout_square_add_cancel = null;
	private TextView txt_square_add_status = null;
	private TextView txt_square_add_party = null;
	private TextView txt_square_add_job = null;
	private TextView txt_square_add_project = null;
	
	public static SquareAddFragment getInstance() {
		SquareAddFragment fragment = new SquareAddFragment();
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_square_add, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		layout_square_add_cancel = (LinearLayout) view.findViewById(R.id.layout_square_add_cancel);
		txt_square_add_status = (TextView) view.findViewById(R.id.txt_square_add_status);
		txt_square_add_party = (TextView) view.findViewById(R.id.txt_square_add_party);
		txt_square_add_job = (TextView) view.findViewById(R.id.txt_square_add_job);
		txt_square_add_project = (TextView) view.findViewById(R.id.txt_square_add_project);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		layout_square_add_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		txt_square_add_status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent(getActivity(), SquareAddStatusActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", ((MainActivity) getActivity()).getUser());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		txt_square_add_party.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent(getActivity(), PartyAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", ((MainActivity) getActivity()).getUser());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		txt_square_add_job.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent(getActivity(), JobAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", ((MainActivity) getActivity()).getUser());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		txt_square_add_project.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				
			}
		});
		
	}
	
	
	
}
