package com.toyking.jiyou.service;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toyking.jiyou.model.Job;
import com.toyking.jiyou.model.Party;
import com.toyking.jiyou.model.PartyAttender;
import com.toyking.jiyou.model.SquarePrize;
import com.toyking.jiyou.model.SquareStatus;
import com.toyking.jiyou.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImp implements UserService {

	public final static int STATUS_CODE_OK = 200;
	private final static String uri_path = "http://10.202.76.55/";// 服务器地址

	public String post_message(String uri, String content) throws Exception {
		uri = uri_path + uri;
		Log.d("post_message", uri + "?data=" + content);

		// NameValuePair --> List<NameValuePair> --> HttpEntity --> HttpPost -->
		// HttpClient
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(uri);

		// 参数封装
		NameValuePair param = new BasicNameValuePair("data", content);
		List<NameValuePair> list_params = new ArrayList<NameValuePair>();
		list_params.add(param);
		post.setEntity(new UrlEncodedFormEntity(list_params, HTTP.UTF_8));

		// 执行结果
		HttpResponse response = client.execute(post);
		int status_code = response.getStatusLine().getStatusCode();
		if (status_code != STATUS_CODE_OK) {
			throw new ServiceException("服务器出错");
		}
		return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
	}

	@Override
	public void Login(String username, String password) throws Exception {
		// JSON数据封装
		JSONObject object = new JSONObject();
		object.put("username", username);
		object.put("password", password);

		String result = post_message("Android/Login", object.toString());

		Log.d("userLogin", result);
		if (!result.equals("success")) {
			throw new ServiceException(result);
		}
	}

	@Override
	public void UserAdd(User user) throws Exception {
		// JSON数据封装
		Gson gson = new Gson();

		// 执行结果
		String result = post_message("Android/UserAdd", gson.toJson(user));

		Log.d("userRegister", result);
		if (!result.equals("success")) {
			throw new ServiceException(result);
		}
	}

	@Override
	public User GetUser(String username, String password) throws Exception {
		// JSON数据封装
		JSONObject object = new JSONObject();
		object.put("username", username);
		object.put("password", password);

		String str_result = post_message("Android/GetUser", object.toString());

		Log.d("getUser", str_result);
		if (str_result.equals("fail")) {
			throw new ServiceException("获取用户信息失败！");
		}

		// JSON数据解析
		Gson gson = new Gson();
		User user = gson.fromJson(str_result, User.class);

		return user;
	}

	@Override
	public void EditUser(User user) throws Exception {
		// JSON数据封装
		Gson gson = new Gson();
		String str_result = post_message("Android/EditUser", gson.toJson(user));

		Log.d("EditUser", str_result);
		if (!str_result.equals("success")) {
			throw new ServiceException(str_result);
		}

	}

	@Override
	public void SquareAddStatus(SquareStatus square_status, User user)
			throws Exception {
		// JSON数据封装
		Gson gson = new Gson();
		String str_post_data = "[" + gson.toJson(square_status) + ","
				+ gson.toJson(user) + "]";

		String result = post_message("Android/SquareAddStatus", str_post_data);

		Log.d("SquareAddStatus", result);
		if (!result.equals("success")) {
			throw new ServiceException(result);
		}
	}

	@Override
	public ArrayList<Map<String, Object>> GetSquareStatusList(User user)
			throws Exception {
		// JSON数据封装
		Gson gson = new Gson();
		String str_result = post_message("Android/GetSquareStatusList",
				gson.toJson(user));

		Log.d("GetSquareStatusList", str_result);
		if (str_result.equals("fail")) {
			throw new ServiceException("获取状态列表失败！");
		}

		// JSON数据解析
		Type type = new TypeToken<ArrayList<SquareStatus>>() {
		}.getType();
		ArrayList<SquareStatus> list = gson.fromJson(str_result, type);
		ArrayList<Map<String, Object>> list_ret = new ArrayList<Map<String, Object>>();
		try {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> mp = new HashMap<String, Object>();
				mp.put("square_status", list.get(i));
				mp.put("square_person", GetPublicUserInfoByUsername(list.get(i)
						.getUsername()));
				List<User> list_user = GetSquarePrize(list.get(i).getId());
				if (list_user.size() <= 0) {
					mp.put("square_prize", "");
				} else {
					String str_prize = list_user.get(0).getRealname();
					for (int j = 1; j < list_user.size(); j++) {
						str_prize += "，" + list_user.get(j).getRealname();
					}
					mp.put("square_prize", str_prize);
				}
				list_ret.add(mp);
			}
		} catch (Exception e) {
			throw new ServiceException("获取状态列表失败！");
		}
		return list_ret;
	}

	@Override
	public void PartyAdd(Party party, User user) throws Exception {
		// JSON数据封装
		Gson gson = new Gson();
		String str_post_data = "[" + gson.toJson(party) + ","
				+ gson.toJson(user) + "]";

		String str_result = post_message("Android/PartyAdd", str_post_data);

		Log.d("PartyAdd", str_result);
		if (!str_result.equals("success")) {
			throw new ServiceException(str_result);
		}

	}

	@Override
	public ArrayList<Map<String, Object>> GetPartyList(User user) throws Exception {
		// JSON数据封装
		Gson gson = new Gson();
		String str_result = post_message("Android/GetPartyList", gson.toJson(user));

		Log.d("GetPartyList", str_result);
		if (str_result.equals("fail")) {
			throw new ServiceException("获取活动列表失败！");
		}

		// JSON数据解析
		Type type = new TypeToken<ArrayList<Party>>() {
		}.getType();
		ArrayList<Party> list_party = gson.fromJson(str_result, type);
		ArrayList<Map<String, Object>> list_ret = new ArrayList<Map<String, Object>>();
		try {
			for (int i = 0; i < list_party.size(); i++) {
				Party party = list_party.get(i);
				User publisher = GetPublicUserInfoByUsername(party .getUsername());
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("party", party);
				item.put("publisher", publisher);
				list_ret.add(item);
			}
		} catch (Exception e) {
			throw new ServiceException("获取活动状态列表失败！");
		}
		return list_ret;
	}

	@Override
	public Party GetPartyById(int id) throws Exception {
		// JSON数据封装
		JSONObject object = new JSONObject();
		object.put("id", id);

		String str_result = post_message("Android/GetPartyById",
				object.toString());
		Log.d("GetPartyById", str_result);

		if (str_result.equals("fail")) {
			throw new ServiceException("获取活动详情失败！");
		}

		// JSON数据解析
		Gson gson = new Gson();
		Party party = gson.fromJson(str_result, Party.class);
		return party;
	}

	/*
	 * @Override public String GetPartyPersonByUsername(String username) throws
	 * Exception { //JSON数据封装 JSONObject object = new JSONObject();
	 * object.put("username", username);
	 * 
	 * String str_result = post_message("Android/GetPartyPersonByUsername",
	 * object.toString());
	 * 
	 * Log.d("GetPartyPersonByUsername", str_result); if
	 * (str_result.equals("fail")) { throw new ServiceException("获取状态列表失败！"); }
	 * 
	 * return str_result; }
	 */

	@Override
	public User GetPublicUserInfoByUsername(String username) throws Exception {
		// JSON数据封装
		JSONObject object = new JSONObject();
		object.put("username", username);

		String str_result = post_message("Android/GetPublicUserInfoByUsername",
				object.toString());

		Log.d("GetPublicUserInfoByUsername", str_result);
		if (str_result.equals("fail")) {
			throw new ServiceException("获取状态列表失败！");
		}

		// JSON数据解析
		Gson gson = new Gson();
		User user = gson.fromJson(str_result, User.class);
		return user;
	}

	@Override
	public void SquarePrizeAdd(int square_id, User user) throws Exception {
		// JSON数据封装
		JSONObject object = new JSONObject();
		Log.d("square_id", square_id + "");
		Log.d("username", user.getUsername());
		Log.d("password", user.getPassword());

		object.put("square_id", square_id);
		object.put("username", user.getUsername());
		object.put("password", user.getPassword());

		String str_result = post_message("Android/SquarePrizeAdd",
				object.toString());

		Log.d("SquarePrizeAdd", str_result);
		if (!str_result.equals("success")) {
			throw new ServiceException(str_result);
		}

	}

	@Override
	public ArrayList<User> GetSquarePrize(int square_id) throws Exception {
		// JSON数据封装
		JSONObject object = new JSONObject();
		object.put("square_id", square_id);

		String str_result = post_message("Android/GetSquarePrize",
				object.toString());

		Log.d("GetSquarePrize", str_result);
		if (str_result.equals("fail")) {
			throw new ServiceException("获取赞失败");
		}

		// JSON数据解析
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<SquarePrize>>() {
		}.getType();
		ArrayList<SquarePrize> list_prize = gson.fromJson(str_result, type);
		ArrayList<User> list_user = new ArrayList<User>();
		try {
			for (int i = 0; i < list_prize.size(); i++) {
				User user = GetPublicUserInfoByUsername(list_prize.get(i)
						.getUsername());
				list_user.add(user);
			}
		} catch (Exception e) {
			throw new ServiceException("获取赞失败");
		}
		return list_user;
	}

	@Override
	public ArrayList<Map<String, Object>> GetPartyAttenderList(int party_id)
			throws Exception {
		// JSON数据封装
		JSONObject object = new JSONObject();
		object.put("party_id", party_id);

		String str_result = post_message("Android/GetPartyAttenderList",
				object.toString());

		Log.d("GetPartyAttendList", str_result);
		if (str_result.equals("fail")) {
			throw new ServiceException("获取赞失败");
		}

		// JSON数据解析
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<PartyAttender>>() {
		}.getType();
		ArrayList<PartyAttender> list_attender = gson
				.fromJson(str_result, type);
		ArrayList<Map<String, Object>> list_ret = new ArrayList<Map<String, Object>>();
		try {
			for (int i = 0; i < list_attender.size(); i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				User user = GetPublicUserInfoByUsername(list_attender.get(i)
						.getUsername());
				item.put("party_attender_user", user);
				item.put("party_attender_time", list_attender.get(i).getTime());
				list_ret.add(item);
			}
		} catch (Exception e) {
			throw new ServiceException("获取报名的人失败");
		}
		return list_ret;
	}

	@Override
	public void PartyAttenderAdd(PartyAttender party_attender, User user)
			throws Exception {
		// JSON数据封装
		Gson gson = new Gson();
		String post_data = "[" + gson.toJson(party_attender) + ","
				+ gson.toJson(user) + "]";
		String result = post_message("Android/PartyAttenderAdd", post_data);

		Log.d("PartyAttenderAdd", result);
		if (!result.equals("success")) {
			throw new ServiceException(result);
		}
	}

	@Override
	public void JobAdd(Job job, User user) throws Exception {
		// JSON数据封装
		Gson gson = new Gson();
		String str_post_data = "[" + gson.toJson(job) + "," + gson.toJson(user) + "]";

		String str_result = post_message("Android/JobAdd", str_post_data);

		Log.d("JobAdd", str_result);
		if (!str_result.equals("success")) {
			throw new ServiceException(str_result);
		}

	}

	@Override
	public ArrayList<Map<String, Object>> GetJobList(User user) throws Exception {
		// JSON数据封装
		Gson gson = new Gson();
		String str_result = post_message("Android/GetJobList",gson.toJson(user));

		Log.d("GetJobList", str_result);
		if (str_result.equals("fail")) {
			throw new ServiceException("获取实习列表失败！");
		}

		// JSON数据解析
		Type type = new TypeToken<ArrayList<Job>>() {}.getType();
		ArrayList<Job> list_job = gson.fromJson(str_result, type);
		ArrayList<Map<String, Object>> list_ret = new ArrayList<Map<String, Object>>();
		try {
			for (int i = 0; i < list_job.size(); i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				Job job = list_job.get(i);
				User publisher = GetPublicUserInfoByUsername( job.getPublisher());
				item.put("job", job);
				item.put("publisher", publisher);
				list_ret.add(item);
			}
		} catch (Exception e) {
			throw new ServiceException("获取实习列表失败！");
		}
		return list_ret;
	}

	@Override
	public Job GetJobById(int job_id) throws Exception {
		// JSON数据封装
		JSONObject object = new JSONObject();
		object.put("job_id", job_id);

		String str_result = post_message("Android/GetJobById", object.toString());
		Log.d("GetJobById", str_result);

		if (str_result.equals("fail")) {
			throw new ServiceException("获取职位详情失败！");
		}

		// JSON数据解析
		Gson gson = new Gson();
		Job job = gson.fromJson(str_result, Job.class);
		return job;
	}

}
