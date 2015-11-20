package core.jsonTest;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import core.json.JSONObject;

public class Group {

	
	private Map<String,User> users = new Hashtable<String,User>(); 
    
	private long updateTime;

	private User owner;
	
	private final String key_time = "1";
	
	private final String key_map = "2";
	
	private final String key_owner = "3";
	
	
	
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		users.put(user.getName(), user);
	}

	public User getUser(String name) {
		return users.get(name);
	}
	
	void decode(String str){
		try{
			users.clear();
			JSONObject obj = new JSONObject(str);
			this.updateTime = obj.optLong(key_time);
			JSONObject tree = obj.getJSONObject(key_map);
			if(tree != null){
				for (Iterator<String> itr = tree.keys(); itr.hasNext();) {
					String ss = tree.getString(itr.next());
					User u = new User();
					u.initFromJson(ss);
					addUser(u);
				}
			}
			owner = new User();
			owner.initFromJson(obj.optString(key_owner));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	String encode(){
		JSONObject obj = new JSONObject();
		try{
			obj.put(key_time, this.updateTime);
			Set<Entry<String,User>> set = users.entrySet();
			JSONObject tree = new JSONObject();
			for (Iterator itr = set.iterator(); itr.hasNext();) {
				Entry<String, User> entry = (Entry<String, User>) itr.next();
				tree.put(entry.getKey(), entry.getValue().encode());
			}
			obj.put(key_map, tree);
			obj.put(key_owner, owner.encode());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return obj.toString();
	}

	@Override
	public String toString() {

		String str = "";
		str += "updateTime:" + updateTime;
		Set<Entry<String,User>> set = users.entrySet();
		for (Iterator itr = set.iterator(); itr.hasNext();) {
			Entry<String, User> entry = (Entry<String, User>) itr.next();
			str += entry.getValue().toString();
		}
		
		return str;
	}
	
	
	
}
