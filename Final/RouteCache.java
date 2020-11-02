import java.util.HashMap;
import java.util.Map;
/**
 * This is a bounded cache that maintains only the most recently accessed IP Addresses
 * and their routes.  Only the least recently accessed route will be purged from the
 * cache when the cache exceeds capacity.  There are 2 closely coupled data structures:
 *   -  a Map keyed to IP Address, used for quick lookup
 *   -  a Queue of the N most recently accessed IP Addresses
 * All operations must be O(1).  A big hint how to make that happen is contained
 * in the type signature of the Map on line 38.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RouteCache
{
    // instance variables - add others if you need them
    // do not change the names of any fields as the test code depends on them
    
    // Cache total capacity and current fill count.
    private final int capacity;
    private int nodeCount;
    
    // private class for nodes in a doubly-linked queue
    // used to keep most-recently-used data
    private class Node {
        private Node prev, next;
        private final IPAddress elem; 
        private final int route;

        Node(IPAddress elem, int route) {   
            prev = next = null;
            this.elem = elem;
            this.route = route;
        }  
    }
    private Node head = null;
    private Node tail = null;
    private Map<IPAddress, Node> nodeMap; // the cache itself

    /**
     * Constructor for objects of class RouteCache
     */
    public RouteCache(int cacheCapacity)
    {
		this.capacity = cacheCapacity; 
		nodeMap=new HashMap<>();
    	// your code goes here
    }

    /**
     * Lookup the output port for an IP Address in the cache
     * 
     * @param  addr   a possibly cached IP Address
     * @return     the cached route for this address, or null if not found 
     */
    public Integer lookupRoute(IPAddress addr)
    {
    	Node node = nodeMap.get(addr);
    	if(node == null) {
    		return null;	
    	}
    	else {
    		return node.route;
    	}
     }
     
    /**
     * Update the cache each time an element's route is looked up.
     * Make sure the element and its route is in the Map.
     * Enqueue the element at the tail of the queue if it is not already in the queue.  
     * Otherwise, move it from its current position to the tail of the queue.  If the queue
     * was already at capacity, remove and return the element at the head of the queue.
     * 
     * @param  elem  an element to be added to the queue, which may already be in the queue. 
     *               If it is, don't add it redundantly, but move it to the back of the queue
     * @return       the expired least recently used element, if any, or null
     */
    public IPAddress updateCache(IPAddress elem, int route)
    {
    	IPAddress address;
    	Node node = nodeMap.get(elem);
    	//System.out.println( "node " +node);
    	//System.out.println( "nodecount " +nodeCount+", cap: "+capacity);
    	if(node == null) {
    		Node x = new Node(elem,route);
    		if(nodeCount==0) {
    			head=tail=x;
    			nodeMap.put(elem, x);
    			nodeCount++;
    			return null;
    		}else if(nodeCount==capacity) {
    			// remove oldest
    			address=head.elem;
    			//System.out.println( "head " +address);
    			head.prev.next=null;
    			head=head.prev;
    			nodeMap.remove(address);
    			// add new
    			tail.prev=x;
    			x.next = tail; 
    			tail=x;
    			nodeMap.put(elem, x);
    			//no count change
    			return address;
    		}
    		else {
    			tail.prev=x;
    			x.next = tail;
    			tail=x;
    			nodeMap.put(elem, x);
    			nodeCount++;
    			return null; 
    		}
    	}
    	else {
    		if (head==node){
    		head=node.prev;
    		node.next=tail; 
    		tail=node;
    		node.prev=null;
    		return null;
    		}
    		else if (tail==node){
    			return null;
        	}
    		else {
    			//System.out.println("next = prev");
        		//System.out.println( "prev " +node.prev);
        		//System.out.println( " next" +node.next);
        		node.prev.next=node.next;
        		//System.out.println( "prev " +node.prev.next);
        		//System.out.println( " next" +node.next);
        		//System.out.println("prev = next");
        		node.next.prev=node.prev;
        		//System.out.println( "prev " +node.prev);
        		//System.out.println( " next" +node.next.prev);
        		//System.out.println("tail.prev = node");
        		tail.prev=node; 
        		node.next=tail;
        		//System.out.println("tail = node");
        		tail=node;
        		node.prev=null;
        		return null;	
    		}
    		
    	}
    }

    
    /**
     * For testing and debugging, return the contents of the LRU queue in most-recent-first order,
     * as an array of IP Addresses in CIDR format. Return a zero length array if the cache is empty
     * 
     */
    String[] dumpQueue()
    {
    	// your code goes here
    	String[] IPs= new String[nodeMap.size()]; 
    	if(IPs.length==0) {
    		return IPs;
    	}
    	Node x= tail.next;
    	//System.out.println("tail " +tail.elem.toCIDR());
    	IPs[0]=tail.elem.toCIDR();
    	for(int i = 1; i<this.nodeCount; i++) {
    		//System.out.println(i+ " " +x.elem.toCIDR());
    		IPs[i]=x.elem.toCIDR();
    		x=x.next;
    	}
    	return IPs; 
    }
    
    
}
