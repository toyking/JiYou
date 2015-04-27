package com.toyking.jiyou;


import com.toyking.jiyou.model.User;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

    private LinearLayout layout_main_square = null;
    private LinearLayout layout_main_team = null;
    private LinearLayout layout_main_message = null;
    private LinearLayout layout_main_user = null;
    
    private TextView txt_main_square = null;
    private TextView txt_main_team = null;
    private TextView txt_main_message = null;
    private TextView txt_main_user = null;
    
	private SquareFragment fragment_square = null;
	private TeamFragment fragment_team = null;
	private MessageFragment fragment_message = null;
	private UserFragment fragment_user = null;
	
	private User user = null;
	
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	private void setNormal(){
		Drawable drawable = null;
		drawable = getResources().getDrawable(R.drawable.icon_square_gray);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		txt_main_square.setCompoundDrawables(null, drawable, null, null);
		txt_main_square.setTextColor(getResources().getColor(R.color.color_gray));
				
		drawable = getResources().getDrawable(R.drawable.icon_team_gray);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		txt_main_team.setCompoundDrawables(null, drawable, null, null);
		txt_main_team.setTextColor(getResources().getColor(R.color.color_gray));
		
		drawable = getResources().getDrawable(R.drawable.icon_message_gray);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		txt_main_message.setCompoundDrawables(null, drawable, null, null);
		txt_main_message.setTextColor(getResources().getColor(R.color.color_gray));
		
		drawable = getResources().getDrawable(R.drawable.icon_user_gray);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		txt_main_user.setCompoundDrawables(null, drawable, null, null);
		txt_main_user.setTextColor(getResources().getColor(R.color.color_gray));
    }
    
    //³õÊ¼»¯¿Ø¼þ
    private void init(){
    	Intent intent = getIntent();
		Bundle budle = intent.getExtras();
		user = (User) budle.getSerializable("user");
    	
		layout_main_square = (LinearLayout) findViewById(R.id.layout_main_square);
		layout_main_team = (LinearLayout) findViewById(R.id.layout_main_team);
		layout_main_message = (LinearLayout) findViewById(R.id.layout_main_message);
		layout_main_user = (LinearLayout) findViewById(R.id.layout_main_user);
		
		txt_main_square = (TextView) findViewById(R.id.txt_main_square);
		txt_main_team = (TextView) findViewById(R.id.txt_main_team);
		txt_main_message = (TextView) findViewById(R.id.txt_main_message);
		txt_main_user = (TextView) findViewById(R.id.txt_main_user);
		
		fragment_square = new SquareFragment();
		fragment_team = new TeamFragment();
		fragment_message = new MessageFragment();
		fragment_user = new UserFragment();
		
		Drawable drawable = getResources().getDrawable(R.drawable.icon_square_green);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		txt_main_square.setCompoundDrawables(null, drawable, null, null);
		txt_main_square.setTextColor(getResources().getColor(R.color.color_green));
		getFragmentManager().beginTransaction().replace(R.id.layout_main_container, fragment_square).commit();
		/*Drawable drawable = getResources().getDrawable(R.drawable.icon_team_green);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		txt_main_team.setCompoundDrawables(null, drawable, null, null);
		txt_main_team.setTextColor(getResources().getColor(R.color.color_green));
		getFragmentManager().beginTransaction().add(R.id.layout_main_container, fragment_team).commit();*/
		
		layout_main_square.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setNormal();
				getFragmentManager().beginTransaction().replace(R.id.layout_main_container, fragment_square).commit();
				Drawable drawable = getResources().getDrawable(R.drawable.icon_square_green);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				txt_main_square.setCompoundDrawables(null, drawable, null, null);
				txt_main_square.setTextColor(getResources().getColor(R.color.color_green));
				
			}
		});
		
		layout_main_team.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setNormal();
				getFragmentManager().beginTransaction().replace(R.id.layout_main_container,fragment_team).commit();
				Drawable drawable = getResources().getDrawable(R.drawable.icon_team_green);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				txt_main_team.setCompoundDrawables(null, drawable, null, null);
				txt_main_team.setTextColor(getResources().getColor(R.color.color_green));
				
			}
		});
		
		layout_main_message.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setNormal();
				getFragmentManager().beginTransaction().replace(R.id.layout_main_container,fragment_message).commit();
				Drawable drawable = getResources().getDrawable(R.drawable.icon_message_green);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				txt_main_message.setCompoundDrawables(null, drawable, null, null);
				txt_main_message.setTextColor(getResources().getColor(R.color.color_green));
				
			}
		});
		
		layout_main_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setNormal();
				getFragmentManager().beginTransaction().replace(R.id.layout_main_container,fragment_user).commit();
				Drawable drawable = getResources().getDrawable(R.drawable.icon_user_green);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				txt_main_user.setCompoundDrawables(null, drawable, null, null);
				txt_main_user.setTextColor(getResources().getColor(R.color.color_green));
			}
		});
		
		
    }
	

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        init();
    }

	@Override
	public void onBackPressed() {
		Intent intent= new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
	    startActivity(intent); 
	}
	
	
}
