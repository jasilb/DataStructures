import static org.junit.Assert.*;

import org.junit.Test;

public class TestCache {

	@Test
	public void testInCache() {
		IPRouter router = new IPRouter(8,5);
		String address1 = "1.2.3.4"; //00000001000000100000001100000100
		String address2 = "3.4.6.8"; //00000011000001000000011000001000
		String address3 = "10.20.30.40";//00001010000101000001111000101000
		String address4 = "15.30.45.60";//00001111000111100010110100111100
		int port1 =1;
		int port2 =2;
		int port3 =3;
		int port4 =4;
		router.addRule(address1, port1);
		router.addRule(address2, port2);
		router.addRule(address3, port3);
		router.addRule(address4, port4);
		//add to cache
		 router.getRoute(new IPAddress("1.2.3.4"));
		 router.getRoute(new IPAddress("3.4.6.8"));
		 router.getRoute(new IPAddress("10.20.30.40"));
		 router.getRoute(new IPAddress("15.30.45.60"));
		 //check if addresses are in the cache
		 assertEquals(true, router.isCached(new IPAddress("1.2.3.4"))); 
		 assertEquals(true, router.isCached(new IPAddress("3.4.6.8"))); 
		 assertEquals(true, router.isCached(new IPAddress("10.20.30.40"))); 
		 assertEquals(true, router.isCached(new IPAddress("15.30.45.60"))); 
	}
	@Test
	public void testCacheLocation() {
		IPRouter router = new IPRouter(8,5);
		String address1 = "1.2.3.4"; //00000001000000100000001100000100
		String address2 = "3.4.6.8"; //00000011000001000000011000001000
		String address3 = "10.20.30.40";//00001010000101000001111000101000
		String address4 = "15.30.45.60";//00001111000111100010110100111100
		int port1 =1;
		int port2 =2;
		int port3 =3;
		int port4 =4;
		router.addRule(address1, port1);
		router.addRule(address2, port2);
		router.addRule(address3, port3);
		router.addRule(address4, port4);
		//add to cache
		 router.getRoute(new IPAddress("1.2.3.4"));
		 router.getRoute(new IPAddress("3.4.6.8"));
		 router.getRoute(new IPAddress("10.20.30.40"));
		 router.getRoute(new IPAddress("15.30.45.60"));
		 //check if addresses are in the cache
		 assertEquals(true, router.isCached(new IPAddress("1.2.3.4"))); 
		 assertEquals(true, router.isCached(new IPAddress("3.4.6.8"))); 
		 assertEquals(true, router.isCached(new IPAddress("10.20.30.40"))); 
		 assertEquals(true, router.isCached(new IPAddress("15.30.45.60"))); 
		 String[] locations = {address4, address3,address2,address1}; 
		 assertArrayEquals(locations,router.dumpCache());
	}
	@Test
	public void testCacheLocationWithUpdateMiddle() {
		IPRouter router = new IPRouter(8,5);
		String address1 = "1.2.3.4"; //00000001000000100000001100000100
		String address2 = "3.4.6.8"; //00000011000001000000011000001000
		String address3 = "10.20.30.40";//00001010000101000001111000101000
		String address4 = "15.30.45.60";//00001111000111100010110100111100
		int port1 =1;
		int port2 =2;
		int port3 =3;
		int port4 =4;
		router.addRule(address1, port1);
		router.addRule(address2, port2);
		router.addRule(address3, port3);
		router.addRule(address4, port4);
		//add to cache
		 router.getRoute(new IPAddress("1.2.3.4"));
		 router.getRoute(new IPAddress("3.4.6.8"));
		 router.getRoute(new IPAddress("10.20.30.40"));
		 router.getRoute(new IPAddress("15.30.45.60"));
		 router.getRoute(new IPAddress("3.4.6.8"));
		 //check if addresses are in the cache
		 assertEquals(true, router.isCached(new IPAddress("1.2.3.4"))); 
		 assertEquals(true, router.isCached(new IPAddress("3.4.6.8"))); 
		 assertEquals(true, router.isCached(new IPAddress("10.20.30.40"))); 
		 assertEquals(true, router.isCached(new IPAddress("15.30.45.60"))); 
		 String[] locations = {address2,address4, address3, address1}; 
		 assertArrayEquals(locations,router.dumpCache());
		 
	}
	@Test
	public void testCacheLocationWithUpdateHead() {
		IPRouter router = new IPRouter(8,5);
		String address1 = "1.2.3.4"; //00000001000000100000001100000100
		String address2 = "3.4.6.8"; //00000011000001000000011000001000
		String address3 = "10.20.30.40";//00001010000101000001111000101000
		String address4 = "15.30.45.60";//00001111000111100010110100111100
		int port1 =1;
		int port2 =2;
		int port3 =3;
		int port4 =4;
		router.addRule(address1, port1);
		router.addRule(address2, port2);
		router.addRule(address3, port3);
		router.addRule(address4, port4);
		//add to cache
		 router.getRoute(new IPAddress("1.2.3.4"));
		 router.getRoute(new IPAddress("3.4.6.8"));
		 router.getRoute(new IPAddress("10.20.30.40"));
		 router.getRoute(new IPAddress("15.30.45.60"));
		 router.getRoute(new IPAddress("1.2.3.4"));
		 //check if addresses are in the cache
		 assertEquals(true, router.isCached(new IPAddress("1.2.3.4"))); 
		 assertEquals(true, router.isCached(new IPAddress("3.4.6.8"))); 
		 assertEquals(true, router.isCached(new IPAddress("10.20.30.40"))); 
		 assertEquals(true, router.isCached(new IPAddress("15.30.45.60"))); 
		 String[] locations = {address1,address4, address3,address2}; 
		 assertArrayEquals(locations,router.dumpCache());
		 
	}
	@Test
	public void testCacheLocationWithUpdateTail() {
		IPRouter router = new IPRouter(8,5);
		String address1 = "1.2.3.4"; //00000001000000100000001100000100
		String address2 = "3.4.6.8"; //00000011000001000000011000001000
		String address3 = "10.20.30.40";//00001010000101000001111000101000
		String address4 = "15.30.45.60";//00001111000111100010110100111100
		int port1 =1;
		int port2 =2;
		int port3 =3;
		int port4 =4;
		router.addRule(address1, port1);
		router.addRule(address2, port2);
		router.addRule(address3, port3);
		router.addRule(address4, port4);
		//add to cache
		 router.getRoute(new IPAddress("1.2.3.4"));
		 router.getRoute(new IPAddress("3.4.6.8"));
		 router.getRoute(new IPAddress("10.20.30.40"));
		 router.getRoute(new IPAddress("15.30.45.60"));
		 router.getRoute(new IPAddress("15.30.45.60"));
		 //check if addresses are in the cache
		 assertEquals(true, router.isCached(new IPAddress("1.2.3.4"))); 
		 assertEquals(true, router.isCached(new IPAddress("3.4.6.8"))); 
		 assertEquals(true, router.isCached(new IPAddress("10.20.30.40"))); 
		 assertEquals(true, router.isCached(new IPAddress("15.30.45.60"))); 
		 String[] locations = {address4, address3,address2,address1}; 
		 assertArrayEquals(locations,router.dumpCache());
		 
	}
	@Test
	public void testCacheLocationWithOverflow() {
		IPRouter router = new IPRouter(8,3);
		String address1 = "1.2.3.4"; //00000001000000100000001100000100
		String address2 = "3.4.6.8"; //00000011000001000000011000001000
		String address3 = "10.20.30.40";//00001010000101000001111000101000
		String address4 = "15.30.45.60";//00001111000111100010110100111100
		int port1 =1;
		int port2 =2;
		int port3 =3;
		int port4 =4;
		router.addRule(address1, port1);
		router.addRule(address2, port2);
		router.addRule(address3, port3);
		router.addRule(address4, port4);
		//add to cache
		 router.getRoute(new IPAddress("1.2.3.4"));
		 router.getRoute(new IPAddress("3.4.6.8"));
		 router.getRoute(new IPAddress("10.20.30.40"));
		 router.getRoute(new IPAddress("15.30.45.60"));
		 
		 //check if addresses are in the cache
		 assertEquals(false, router.isCached(new IPAddress("1.2.3.4"))); 
		 assertEquals(true, router.isCached(new IPAddress("3.4.6.8"))); 
		 assertEquals(true, router.isCached(new IPAddress("10.20.30.40"))); 
		 assertEquals(true, router.isCached(new IPAddress("15.30.45.60"))); 
		 String[] locations = {address4, address3,address2}; 
		 assertArrayEquals(locations,router.dumpCache());
		 
	}
	@Test
	public void testCacheLocationWithOverflow2() {
		IPRouter router = new IPRouter(8,3);
		String address1 = "1.2.3.4"; //00000001000000100000001100000100
		String address2 = "3.4.6.8"; //00000011000001000000011000001000
		String address3 = "10.20.30.40";//00001010000101000001111000101000
		String address4 = "15.30.45.60";//00001111000111100010110100111100
		int port1 =1;
		int port2 =2;
		int port3 =3;
		int port4 =4;
		router.addRule(address1, port1);
		router.addRule(address2, port2);
		router.addRule(address3, port3);
		router.addRule(address4, port4);
		//add to cache
		 router.getRoute(new IPAddress("1.2.3.4"));
		 router.getRoute(new IPAddress("3.4.6.8"));
		 router.getRoute(new IPAddress("10.20.30.40"));
		 //update first address
		 router.getRoute(new IPAddress("1.2.3.4"));
		 router.getRoute(new IPAddress("15.30.45.60"));
		 
		 //check if addresses are in the cache
		 assertEquals(true, router.isCached(new IPAddress("1.2.3.4"))); 
		 assertEquals(false, router.isCached(new IPAddress("3.4.6.8"))); 
		 assertEquals(true, router.isCached(new IPAddress("10.20.30.40"))); 
		 assertEquals(true, router.isCached(new IPAddress("15.30.45.60"))); 
		 String[] locations = {address4, address1,address3}; 
		 assertArrayEquals(locations,router.dumpCache()); 
	}
	@Test
	public void testCacheEmpty() {
		IPRouter router = new IPRouter(8,3);
		String address1 = "1.2.3.4"; //00000001000000100000001100000100
		String address2 = "3.4.6.8"; //00000011000001000000011000001000
		String address3 = "10.20.30.40";//00001010000101000001111000101000
		String address4 = "15.30.45.60";//00001111000111100010110100111100
		int port1 =1;
		int port2 =2;
		int port3 =3;
		int port4 =4;
		router.addRule(address1, port1);
		router.addRule(address2, port2);
		router.addRule(address3, port3);
		router.addRule(address4, port4);
		 String[] locations = {}; 
		 assertArrayEquals(locations,router.dumpCache());
	}
	@Test
	public void testCacheWithDelete1() {
		IPRouter router = new IPRouter(8,3); //I made the cache size big so that it wont interfere with the trie
		String address1 = "1.2.3.4/10"; 	//0000000100(0000100000001100000100)
		String address2 = "3.4.6.8/12"; 	//000000110000(01000000011000001000)
		String address3 = "10.20.30.40/15";	//000010100001010(00001111000101000)
		String address4 = "15.30.45.60/20";	//00001111000111100010(110100111100)
		int port1 =1;
		int port2 =2;
		int port3 =3;
		int port4 =4;
		router.addRule(address1, port1);
		router.addRule(address2, port2);
		router.addRule(address3, port3);
		router.addRule(address4, port4);
		int get1= router.getRoute(new IPAddress("1.2.3.4"));
		int get2= router.getRoute(new IPAddress("3.4.6.8"));
		int get3= router.getRoute(new IPAddress("10.20.30.40"));
		int get4= router.getRoute(new IPAddress("15.30.45.60"));
		 assertEquals(1, get1); 
		 assertEquals(2, get2); 
		 assertEquals(3, get3); 
		 assertEquals(4, get4); 
		 //remove address1
		 router.deleteRule(address1);
		 int getnew= router.getRoute(new IPAddress("1.2.3.4"));
		 assertEquals(-1, getnew); 
		 String[] locations = {address1.substring(0,address1.lastIndexOf("/") ), address4.substring(0,address4.lastIndexOf("/")),address3.substring(0,address3.lastIndexOf("/"))}; 
		 //{"1.2.3.4","10.20.30.40";"15.30.45.60";}
		 assertArrayEquals(locations,router.dumpCache()); 
	}
}
