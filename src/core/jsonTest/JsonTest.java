package core.jsonTest;

import java.time.LocalDateTime;
import java.util.function.Supplier;





public class JsonTest {

	static Group group = new Group();
	
	static void startInitJson(){
		
		
		for (int i = 0; i < 2; i++) {
			User u = new User();
			u.setId(i);
			u.setCreateTime(System.currentTimeMillis());
			u.setInfo("start init user :" + i);
			u.setName("U" + i);
			group.addUser(u);
		}
		
		
	}
	

	public interface Defaultable{
		
	}
	
	private interface DefaultInterface{
		
		static Object create(Supplier<Object> supplier){
			return  supplier.get();
		}
	}
	
	public static void main(String [] args){
//		System.out.println("System start--------------");
		
//		JsonTest.startInitJson();
//		String ss = group.toString();
//		System.out.println(ss);
//		System.out.println("start compare----------");
		
		
//		long t = System.currentTimeMillis();
//		String encodeStr = group.encode();
//		System.out.println("Encode Json cost time:" + (System.currentTimeMillis() - t));
	
		
//		t = System.currentTimeMillis();
		
//		String jsonString = JSON.toJSONString(group);
//		System.out.println("Encode FastJson cost time:" + (System.currentTimeMillis() - t));
		
		
//		t = System.currentTimeMillis();
//		group.decode(encodeStr);
//		System.out.println("Decode Json cost time:" + (System.currentTimeMillis() - t));
		
		
//		t = System.currentTimeMillis();
//		group = JSON.parseObject(jsonString,Group.class);
//		System.out.println("Decode FastJson cost time:" + (System.currentTimeMillis() - t));
//		jsonString = group.toString();
//		System.out.println(jsonString);
		
		
//		List<String> str = Arrays.asList("1","2","3","14");
//		str.stream().map(t->Integer.parseInt(t)).filter(q -> (q-1)>10).forEach(t ->{
//			System.out.println(t);
//		});
		
		
		LocalDateTime  date = LocalDateTime.now();
		int day = date.getDayOfMonth();
		System.out.println(day);
		int dayOfYear = date.getDayOfYear();
		System.out.println(dayOfYear);
	}
	
}
