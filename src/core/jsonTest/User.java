package core.jsonTest;

import core.json.JSONObject;


public class User {

	
	private long id;
	
	private String name;
	
	private long createTime;
	
	private String info;
	
	

	private final static String key_id = "1";
	private final static String key_name = "2";
	private final static String key_createTime = "3";
	private final static String key_info = "4";
	
	public User() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String encode(){
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put(key_id, id);
			obj.put(key_name, name);
			obj.put(key_createTime, createTime);
			obj.put(key_info, info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

	public void initFromJson(String str){
		if(str == null){
			return;
		}
		try{
			JSONObject obj = new JSONObject(str);
			this.id = obj.optLong(key_id, 0);
			this.name = obj.optString(key_name);
			this.createTime = obj.optLong(key_createTime, 0);
			this.info = obj.optString(key_info);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "id:" + id + ";name:" + name + ";createTime:" + createTime + "; info:" + info;
	}
	
	
}
