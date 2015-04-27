package com.toyking.jiyou;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TeamFragment extends Fragment {

	private LinearLayout layout_team_project = null;
	private LinearLayout layout_team_job = null;
	private LinearLayout layout_team_party = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_team, container, false);
		layout_team_project = (LinearLayout) view.findViewById(R.id.layout_team_project);
		layout_team_job = (LinearLayout) view.findViewById(R.id.layout_team_job);
		layout_team_party = (LinearLayout) view.findViewById(R.id.layout_team_party);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		layout_team_project.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		layout_team_job.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), JobActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", ((MainActivity) getActivity()).getUser());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		layout_team_party.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PartyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", ((MainActivity) getActivity()).getUser());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

}
