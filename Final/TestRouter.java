
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

/**
 * The test class TestRouter.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TestRouter
{

    //private TrieST<Integer> router;
    private IPRouter router;
    /**
     * Default constructor for test class TestRouter
     */
    public TestRouter()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
       this.router = new IPRouter(8,4); 
        try {
            router.loadRoutes("routes1.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
    }

    /**
     * Handle an unroutable address
     */
    @Test 
    public void testBadRoute()  
    {
        IPAddress address = new IPAddress("73.73.0.1");
        assertEquals(-1, this.router.getRoute(address));
    }

    /**
     * Handle an address that only matches one prefix
     */
    @Test
    public void port2Test()
    {
        IPAddress address = new IPAddress("85.2.0.1");
        System.out.println(" address " +address);
        int res = this.router.getRoute(address);
        assertEquals(2, res); 
    }

    /**
     * Handle an address that only matches multiple prefixes. Only the longest one counts
     */
    @Test
    public void port1Test()
    {
        IPAddress address = new IPAddress("85.85.85.85");
        System.out.println(" address " +address);
        int res = this.router.getRoute(address);
        assertEquals(1, res);
    }
    
    @Test
    public void myTest1() { 
    	//delete
    	System.out.println("  ");
    	System.out.println(" new router ");
    	this.router = new IPRouter(8,4); 
    	IPAddress address = new IPAddress("24.34.0.0/16");
    	System.out.println(" add " +address);
    	router.addRule("24.34.0.0/16", 1);
    	System.out.println(" delete " );
        router.deleteRule("24.34.0.0/16");
        System.out.println(" find/not there " );
        assertEquals(-1, router.getRoute(new IPAddress("24.34.0.0/16")));
    }
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    	String[] array= this.router.dumpCache();
    	for(String ip: array) {
    		System.out.println(" cache= "+ ip);
    	}
    	
    }
}
