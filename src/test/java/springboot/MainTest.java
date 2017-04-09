package springboot;

import java.util.List;

import org.junit.Test;

public class MainTest {
	
	@Test
	public void test1(){
		List<String> list = null;
		for(String string : list){
			System.out.println(string);
		}
	}

}
