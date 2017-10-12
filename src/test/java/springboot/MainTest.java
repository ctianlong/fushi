package springboot;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;

import springboot.util.ArithUtil;

public class MainTest {
	
	@Test
	public void test1(){
		List<String> list = null;
		for(String string : list){
			System.out.println(string);
		}
	}
	
	@Test
	public void testUriComponentsBuilder(){
		System.out.println(UriComponentsBuilder.fromUriString("/api/users").queryParam("size", 10).queryParam("page", 1).toUriString());
	}

	@Test
	public void testGenerateScore(){
		int n = 13;
		double result = Arrays.stream(new int[]{2,1,3,5,4,6,7,8,10,9,44,88,77}).sorted().limit(n - 1).skip(1).average().getAsDouble();
		System.out.println(result);
		System.out.println(Stream.empty().count());
	}
	
	@Test
	public void testBigDecimal(){
		//double
		System.out.println(0.1+0.2);
		System.out.println(0.4444);
		System.out.println(Double.toString(11.33));
		double d1 = ArithUtil.add(0.135, 0.398);
		System.out.println(d1);
		double d2 = 0.1 + 0.2;
		System.out.println(d2);
		double d3 = 1.22;
		System.out.println(d3);
		System.out.println(new BigDecimal(1.22));
		System.out.println(new BigDecimal("1.22").scale());
	}
	
	@Test
	public void testStream(){
		Set<String> set = new HashSet<>();
		System.out.println(set.size());
		System.out.println(set.stream().map(s -> s.toUpperCase()).collect(Collectors.toSet()));
	}
	
	@Test
	public void testStringBuilder() {
		StringBuilder sb = new StringBuilder("1,");
		System.out.println(sb.length());
		System.out.println(sb.deleteCharAt(sb.length() - 1).toString());
	}
	
	@Test
	public void testDouble() {
		double d = 18817878571D;
		System.out.println(d);
		System.out.println(String.valueOf(d));
		System.out.println(String.format("%.0f", d));
		System.out.println(new DecimalFormat("#").format(d));
	}
}
