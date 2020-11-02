import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

public class TestRouterAndTrieOnly {
// this test class will test the router and the trie
	@Test
	public void testRouterGetExact() {
		IPRouter router = new IPRouter(8,100); //I made the cache size big so that it wont interfere with the trie
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
		int get1= router.getRoute(new IPAddress("1.2.3.4"));
		int get2= router.getRoute(new IPAddress("3.4.6.8"));
		int get3= router.getRoute(new IPAddress("10.20.30.40"));
		int get4= router.getRoute(new IPAddress("15.30.45.60"));
		 assertEquals(1, get1); 
		 assertEquals(2, get2); 
		 assertEquals(3, get3); 
		 assertEquals(4, get4); 
	}
	@Test
	public void testRouterGetClosest() {
		IPRouter router = new IPRouter(8,100); //I made the cache size big so that it wont interfere with the trie
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
	}
	@Test
	public void testRouterGetOverlap1() {
		IPRouter router = new IPRouter(8,100); //I made the cache size big so that it wont interfere with the trie
		String address1 = "1.2.3.4/10"; 	//0000000100(0000100000001100000100)
		String overlap = "1.2.3.4/12"; 		//000000010000(00100000001100000100)
		int port1 =1;
		int port2 =2;
		router.addRule(address1, port1);
		router.addRule(overlap, port2);
		int get1= router.getRoute(new IPAddress("1.2.3.4"));
		//greatest prefix is at port 2
		 assertEquals(2, get1); 
		 //adding a greater prefix
		 String overlap2 = "1.2.3.4/15";	//000000010000001(00000001100000100)
		 int port3 =3;
		 router.addRule(overlap2, port3);
		 int get2= router.getRoute(new IPAddress("1.2.3.4"));
		 assertEquals(3, get2);
		 assertEquals(true, router.trie.isRoutable(new IPAddress("1.2.3.5")));
	}
	@Test
	public void testRouterGetOverlap2() {
		IPRouter router = new IPRouter(8,100); //I made the cache size big so that it wont interfere with the trie
		 try {
	            router.loadRoutes("routes2.txt");
	        }
	        catch (FileNotFoundException e) {
	            throw new RuntimeException("Bad routes file name. Tests aborted");
	        }
		

		 assertEquals(1, router.getRoute(new IPAddress("85.85.85.85")));
		 assertEquals(6, router.getRoute(new IPAddress("24.98.100.123")));
		 assertEquals(7, router.getRoute(new IPAddress("24.91.100.123")));
		 router.addRule("25.64.0.0/10", 1);
		 router.addRule("25.91.0.0/16", 2);
		 router.addRule("25.98.0.0/15", 3);
		 assertEquals(-1,router.getRoute(new IPAddress("25.0.0.0")));
		 
	}
	@Test
	public void testRouterDelete1() {
		IPRouter router = new IPRouter(8,100); //I made the cache size big so that it wont interfere with the trie
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
	}
	@Test
	public void testRouterDelete2() {
		IPRouter router = new IPRouter(8,100); //I made the cache size big so that it wont interfere with the trie
		String address1 = "1.2.3.4/10"; 	//0000000100(0000100000001100000100)
		String overlap = "1.2.3.4/12"; 		//000000010000(00100000001100000100)
		int port1 =1;
		int port2 =2;
		router.addRule(address1, port1);
		router.addRule(overlap, port2);
		int get1= router.getRoute(new IPAddress("1.2.3.4"));
		//greatest prefix is at port 2
		 assertEquals(2, get1); 
		 //adding a greater prefix
		 String overlap2 = "1.2.3.4/15";	//000000010000001(00000001100000100)
		 int port3 =3;
		 router.addRule(overlap2, port3);
		 int get2= router.getRoute(new IPAddress("1.2.3.4"));
		 assertEquals(3, get2);
		 //delete overlap2
		 router.deleteRule(overlap2);
		 int getold= router.getRoute(new IPAddress("1.2.3.4"));
		 assertEquals(2, getold); 
		 //delete overlap
		 router.deleteRule(overlap);
		 int getfirstput= router.getRoute(new IPAddress("1.2.3.4"));
		 assertEquals(1, getfirstput); 
		 //delete address1, now trie is empty
		 router.deleteRule(address1);
		 int empty= router.getRoute(new IPAddress("1.2.3.4"));
		 assertEquals(-1, empty); 
	}
	//errors
	@Test (expected = IllegalArgumentException.class)
	public void testRouterNoPort() {
		IPRouter router = new IPRouter(2,100); //I made the cache size big so that it wont interfere with the trie
		String address1 = "1.2.3.4/10";
		int port1 =3;
		router.addRule(address1, port1);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testRouterPortEqualsNPorts() {
		IPRouter router = new IPRouter(2,100); //I made the cache size big so that it wont interfere with the trie
		String address1 = "1.2.3.4/10";
		int port1 =2;
		router.addRule(address1, port1);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testRouterPutSameAddress() {
		IPRouter router = new IPRouter(5,100); //I made the cache size big so that it wont interfere with the trie
		String address1 = "1.2.3.4/10";
		router.addRule(address1, 1);
		router.addRule(address1, 2);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testRouterDeleteNothing() {
		IPRouter router = new IPRouter(5,100); //I made the cache size big so that it wont interfere with the trie
		String address1 = "1.2.3.4/10";
		String addressToDelete = "1.2.3.4/5";
		router.addRule(address1, 1);
		router.deleteRule(addressToDelete);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testRouterDeleteNothin2g() {
		IPRouter router = new IPRouter(5,100); //I made the cache size big so that it wont interfere with the trie
		String addressToDelete = "1.2.3.4/5";
		router.deleteRule(addressToDelete);
	}
	
}
