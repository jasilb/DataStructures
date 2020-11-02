package edu.yu.cs.com1320.project.impl.HashTableImpl;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<Key, Value> implements HashTable<Key, Value> {
	
	
	MyLinkedList<Key,Value>[] table = new MyLinkedList[5];
	
	
	
	public HashTableImpl(){
		table[0]=new MyLinkedList<Key,Value>();
		table[1]=new MyLinkedList<Key,Value>();
		table[2]=new MyLinkedList<Key,Value>();
		table[3]=new MyLinkedList<Key,Value>();
		table[4]=new MyLinkedList<Key,Value>();

	}

	public Value get(Key k) {
		int inputKHash=k.hashCode();
		int kIndex=(inputKHash & 0x7fffffff)%5;
		return  table[kIndex].find(k);
	}

	public Value put(Key k, Value v) {

			// TODO Auto-generated method stub
			int kHash=k.hashCode();

			int index = (kHash & 0x7fffffff)  % 5;
			if (v==null){
			return table[index].delete(k);
			}
			Item<Key, Value> i= new Item<Key, Value>(k,v);
			

				return table[index].add(i);
			

		}
	
}
	class MyLinkedList<Key, Value> {
		Item<Key, Value> head=null;
		Item<Key, Value> current=head;
			
			
			MyLinkedList(){}
			




				Value find(Key k) {
					current=head;
						if(head==null) {
							return null;
						}
						else if(k.equals(head.key)){
							return head.value;
						}
						else {
						while(current.next!=null) {
							current=current.next;
							if(current.key.equals(k)){
								return current.value;
							}
						}
						current=head;
					return null;
				}
				}


				 Value add (Item<Key, Value> i) {
					 current=head;
					if(head==null) {
						head =i;
						current= head;
						return null;
					}
					else if(i.key.equals(head.key)) {
						Value temp= current.value;
						head.value=i.value;
						return temp;
					}
					else {
						while(current.next!=null) {
							current=current.next;
							if(current.key.equals(i.key)) {
								Value temp= current.value;
								current.value=i.value;
								return temp;
							}
								
						}
						current.next=i;
						current=head;
						return null;
					}
				}
				 
				  Value delete(Key k) {
					  current=head;
					  if(head==null) {
						return null;
					  }
					  else if (head.key.equals(k)) {
						  Value temp= head.value;
						  head= head.next;
						  current = head;
						  return temp;
					  }
					  else {
						  Item<Key, Value> previous=current;
						  while (current.next!= null) {
							  current=current.next;
							  if (current.key.equals(k)) {
								  Value temp1= current.value;
								  previous.next=current.next;
								  return temp1;
							  }
							  previous=current;
				}
				return null;
			}
				}

	}

			 class Item<Key,Value>{
				Key key=null;
				Value value=null;
				Item <Key, Value> next=null;
				
			Item(Key k, Value v) {
				if(k == null || v == null){
					throw new IllegalArgumentException();
				}

				this.key = k;
				this.value =v;
				
				}
			}
	
			 
			 
