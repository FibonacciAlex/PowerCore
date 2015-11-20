package core.test.concurrent;

import java.util.ArrayList;
import java.util.List;





public class ProxyTest{


	

	public static void main(String[] args) throws Exception{
		List<A> aList = new ArrayList<A>();
		aList.add(new A(1, 2, 3));
		aList.add(new A(2, 3, 4));
		
		A a3 = new A(1, 2, 3);
		boolean contains = aList.contains(a3);
		System.out.println("-------contain------" + contains);
		boolean equiq = false;
		
		for (A a : aList) {
			if(a.equals(a3)){
				equiq = true;
			}
		}
		
		System.out.println("--------equiq-------" + equiq);
	
		
	}
	
	static class A{
		int one;
		int two;
		int three;
		public A(int one, int two, int three) {
			super();
			this.one = one;
			this.two = two;
			this.three = three;
		}
}
}
