import java.util.LinkedList;

public class BitVectorTrie<Value> {
    
    private static final int R = 2;      
    private Node root;

    private static class Node {
        private Object val; 
        private Node[] next = new Node[R];  
    }

   /****************************************************
    * Is the key in the symbol table?
    ****************************************************/
    public boolean isRoutable(BitVector key) {
        return get(key) != null;
    }

   /****************************************************
    * get needs the most changes since its result depends
    * not on the entire key but on its longest matching 
    * prefix
    ****************************************************/
    public Value get(BitVector key) {
        return get(root, key, 0, null);
    }

    private Value get(Node x, BitVector key, int d, Value bestSoFar) {
    	//System.out.println(d+" Key " +key.toString().substring(0, d) + ", " +bestSoFar);
    	if (x == null) {
    		//System.out.println("null");
    		return bestSoFar;
    	}
        if (x.val != null) {
        	bestSoFar = (Value) x.val;
        	//System.out.println(d+" x.val " +x.val);
        }
        if (d == key.size()) {
        	return bestSoFar;
        }
        int c = key.get(d);
        return get(x.next[c], key, d+1, bestSoFar);
		 
   }

   /****************************************************
    * Insert Value value into the prefix Trie.
    * If a different value exists for the same key
    * throw an IllegalArgumentException
    ****************************************************/
    public void put(BitVector key, Value port) {
        root = put(root, key, port, 0);
    }

    private Node put(Node x, BitVector key, Value port, int d) {
    	 if (x == null) {
    		 x = new Node(); 
    	 }
    	 //System.out.println(d+" Key " +key + ", " +port);
    	 //System.out.println(d+" x.val " +x.val);
         if (d == key.size()) {
        	 if ((x.val != null)&&(!(x.val.equals(port)))) {
            	 throw new IllegalArgumentException();
             }
             if (x.val == null) {
            	 d++;
             }
             x.val = port;
             return x;
         }
         int c = key.get(d);
         x.next[c] = put(x.next[c], key, port, d+1);
         return x;
     }

   /****************************************************
    * Delete the value for a key.
    * If no value exists for this key
    * throw and IllegalArgumentException
    ****************************************************/
    public void delete(BitVector key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, BitVector key, int d) {
    	//System.out.println(d+" Key " +key);
    	 if (x == null) {
    		 throw new IllegalArgumentException();
    	 }
         if (d == key.size()) {
        	 if (x.val == null) {
        		 //System.out.println(d+" x.val is null: " +x.val);
        		 throw new IllegalArgumentException();
        	 }
        	 //System.out.println(d+" x.val to make null: " +x.val);
             x.val = null;
         }
         else {
             int c = key.get(d);
             x.next[c] = delete(x.next[c], key, d+1);
         }

         // remove subtrie if empty
        
         //System.out.println(" delete subtree ");
         if (x.val != null) {
        	 //System.out.println(" not  null " +x.val);
        	 return x;
         }
         for (int c = 0; c < R; c++) {
        	 if (x.next[c] != null) {
        		 //System.out.println(" not  null " +x.val);
            	 return x;
             } 
         } 
         //System.out.println(" null/delete node ");
         return null;

    }

}
