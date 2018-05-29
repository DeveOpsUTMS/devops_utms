package com.valuelabs.poc.devops_utms;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevopsUtmsApplicationTests {
	
	public int add(int a,int b){
		return a+b;
	}
	
	/*public int divide(int a,int b){
		return a/b;
	}*/
	

	/*@Test
	public void contextLoads() {
		
	}*/
	
	@Test
	public void getPositiveNumber(){
		
		assertEquals(2, add(1,1));
	}
	
	@Test
	public void getNegativeNumber(){
		assertEquals(-2, add(-1,-1));
	}
	
	@Test
	public void getNegativeResult(){
		assertEquals(-1, add(-2,1));
	}
	;
	@Test
	public void getPositiveResult(){
		assertEquals(1, add(2,-1));
	}
	
	

}
