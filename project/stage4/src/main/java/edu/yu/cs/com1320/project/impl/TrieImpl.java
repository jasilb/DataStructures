package edu.yu.cs.com1320.project.impl;

import java.util.ArrayList;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.impl.DocumentImpl;

public class TrieImpl<Value> implements Trie<Value> {

	private static final int alphabetSize = 91; // extended ASCII
	private Node root; // root of trie

	class Node<Value> {
		protected List<Value> val = new ArrayList<Value>();
		protected Node[] links = new Node[alphabetSize];

	}

	public TrieImpl() {
	}

	public void put(String key, Value val) {
		String words = key;
		words =words.toUpperCase();
		if (val == null) {
			return;
		} else {
			//System.out.println(val);
			this.root = put(this.root, words, val, 0);
		}
	}

	/**
	 *
	 * @param x
	 * @param key
	 * @param val
	 * @param d
	 * @return
	 */
	private Node put(Node<Value> x, String key, Value val, int d) {
		//System.out.println(d);
		// create a new node
		if (x == null) {
			//System.out.println("new");
			x = new Node<Value>();
		}
		// we've reached the last node in the key,
		// set the value for the key and return the node
		if (d == key.length()) {
			//System.out.println("val: " + val);
			x.val.add(val);
			//System.out.println("put");
			return x;
		}
		// proceed to the next node in the chain of nodes that
		// forms the desired key
		char c = key.charAt(d);
		x.links[c] = this.put(x.links[c], key, val, d + 1);
		return x;
	}

	public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
		String words = key;
		words =words.toUpperCase();
		List<Value> list = new ArrayList<Value>();
		Node x = get(this.root, words, 0);
		if (x == null) {
			return list;
		}
		
			list.addAll(x.val);
			Collections.sort(list,comparator);
		
		return (List<Value>) list;

	}

	/**
	 * A char in java has an int value. see
	 * http://docs.oracle.com/javase/8/docs/api/java/lang/Character.html#getNumericValue-char-
	 * see http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.1.2
	 */
	private Node get(Node x, String key, int d) {
		
		// link was null - return null, indicating a miss
		if (x == null) {
			return null;
		}
		// we've reached the last node in the key,
		// return the node
		if (d == key.length()) {
			return x;
		}
		// proceed to the next node in the chain of nodes that
		// forms the desired key
		char c = key.charAt(d);
		return this.get(x.links[c], key, d + 1);
	}

	public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
		String words = prefix;
		words =words.toUpperCase();
		List<Node> results = new ArrayList<Node>();
		Node x = get(this.root, words, 0);
		if (x != null) {
			collect(x, new StringBuilder(words), results);
		}
		List<List<Value>> list= new ArrayList<List<Value>>();
		for (Node n: results) {
			if (n.val != null) {
				list.add(n.val);
			}
		}
		Set<Value> values =new HashSet<Value>();
		for (List<Value> v :list) {
			values.addAll(v);
		}
		List<Value> vv= new ArrayList<Value>(values);
		Collections.sort(vv, comparator);
		return vv;
	}

	private void collect(Node x, StringBuilder prefix, List<Node> results) {

//if this node has a value, add it’s key to the queue
		if (x.val != null) {
//add a string made up of the chars from
//root to this node to the result set
			results.add(x);
		}
//visit each non-null child/link
		for (char c = 0; c < alphabetSize; c++) {
			if (x.links[c] != null) { 
//add child's char to the string
				prefix.append(c);
				this.collect(x.links[c], prefix, results);
//remove the child's char to prepare for next iteration
				prefix.deleteCharAt(prefix.length() - 1);
			}
		}
	}

	public Set<Value> deleteAllWithPrefix(String prefix) {
		String words = prefix;
		System.out.println(words);
		words =words.toUpperCase();
		Node x = get(this.root, words, 0);
		Set<Value> set = new HashSet<Value>();
		if (x != null) {
			dcollect(x, new StringBuilder(words), set);
			System.out.println("delete method:");
			char c =words.charAt(words.length()-1);
			System.out.println(c + " letter");
			if(words.length()==1) {
				deleteSubtree(this.root,c);
			}
			else {
				String before = words.substring(0, words.length()-1);
				System.out.println(before +" word being sent");
				Node y = get(this.root, before, 0);
				deleteSubtree(y,c);
			}
		}
		return set;
	}

	private void deleteSubtree(Node y, char c) {
		System.out.println(c);
		y.links[c]=null;
		
	}

	private void dcollect(Node x, StringBuilder prefix, Set<Value> set) {
		if (x.val != null) { 
			//add a string made up of the chars from
			//root to this node to the result set
						set.addAll(x.val);
					}
			//visit each non-null child/link
					for (char c = 0; c < alphabetSize; c++) {
						if (x.links[c] != null) { 
			//add child's char to the string
							prefix.append(c);
							this.dcollect(x.links[c], prefix, set);
			//remove the child's char to prepare for next iteration
							prefix.deleteCharAt(prefix.length() - 1);
						}
					}
	}

	public Set<Value> deleteAll(String key) {
		String words = key;
		words =words.toUpperCase();
		Node x =get(this.root, words, 0);
		Set<Value> set = new HashSet<Value>();
		if (x != null) {
			set.addAll(x.val);
			x.val.clear();
			////System.out.println("null " + words);
			if(deleteAllInNode(x) == false) {
				char c =words.charAt(words.length()-1);
				if(words.length()==1) {
					deleteSubtree(this.root,c);
				}
				else {
					String before = words.substring(0, words.length()-1);
					Node y = get(this.root, before, 0);
					deleteSubtree(y,c);
				}
			}

		}
		


		return set; 
	}

	private boolean deleteAllInNode(Node x) {
		
		for (int c = 0; c < alphabetSize; c++) {
			if (x.links[c] != null) {
				return true; // not empty
			}
		}
		return false;
	}

	public Value delete(String key, Value val) {
		String words = key;
		words =words.toUpperCase();
		Node n = get(this.root, words, 0);
		if(n==null) {
			return null;
		}
		Value v = null;
		int x = n.val.indexOf(val);
		//System.out.println(x +"+ "+ words);
		if (x == -1) {
			
			return null;
		} 
		 
		v = (Value) n.val.remove(x);
		if(CheckNode(n) == false) {
			char c =words.charAt(words.length()-1);
			if(words.length()==1) {
				deleteSubtree(this.root,c);
			}
			else {
				String before = words.substring(0, words.length()-2);
				Node y = get(this.root, before, 0);
				deleteSubtree(y,c);
			}
		}
		return v;
	}

	private boolean CheckNode(Node n) {
		if(n.val != null) {
			return false;
		}
		for (int c = 0; c < alphabetSize; c++) {
			if (n.links[c] != null) {
				return true; // not empty
			}
		}
		return false;
	}

}


