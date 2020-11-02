package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<Key, Value> implements HashTable<Key, Value> {

	MyLinkedList<Key, Value>[] table = new MyLinkedList[5];
	private int numberOfElements = 0;

	public HashTableImpl() {
		table[0] = new MyLinkedList<Key, Value>();
		table[1] = new MyLinkedList<Key, Value>();
		table[2] = new MyLinkedList<Key, Value>();
		table[3] = new MyLinkedList<Key, Value>();
		table[4] = new MyLinkedList<Key, Value>();

	}

	public Value get(Key k) {
		int inputKHash = k.hashCode();
		int kIndex = (inputKHash & 0x7fffffff) % table.length; 
		return table[kIndex].find(k);
	} 

	public Value put(Key k, Value v) {

		// TODO Auto-generated method stub
		int kHash = k.hashCode();

		int index = (kHash & 0x7fffffff) % table.length;
		if (v == null) {
			Value value = table[index].delete(k);
			if (value != null) {
				resizeTest(-1);
			}
			return value;
		}
		Item<Key, Value> i = new Item<Key, Value>(k, v);
		Value val = table[index].add(i);
		if (val == null) {
			resizeTest(1);
		}
		return val;
	}

	private void resizeTest(int x) {
		int arraylength;

		arraylength = table.length;
		numberOfElements += x;
		if (arraylength == (numberOfElements / 4)) {
			Resize(arraylength);
		}
	}

	private void Resize(int arraylength) {
		MyLinkedList<Key, Value>[] table2 = new MyLinkedList[arraylength * 2];
		// System.out.println(table2.length);
		for (int x = 0; x < table2.length; x++) {
			table2[x] = new MyLinkedList<Key, Value>();
			// System.out.println("x"+x);
		}
		for (int y = 0; y < table.length; y++) {
			MyLinkedList<Key, Value> item = table[y];
			// System.out.println("y"+y);
			while (item.head != null) {
				// System.out.println("yes1");
				while (item.current.next != null) {
					// System.out.println("yes2");
					item.current = item.current.next;
				}
				// System.out.println("yes3");
				Item<Key, Value> i = item.current;
				Reput(i, table2);
				item.delete(i.key);
				item.current = item.head;
			}
		}
		table = table2;

	}

	private void Reput(Item<Key, Value> i, MyLinkedList<Key, Value>[] table2) {
		// System.out.println("yes4");
		int newHash = i.key.hashCode();
		int newIndex = (newHash & 0x7fffffff) % table2.length;
		table2[newIndex].add(i);
	}

}

class MyLinkedList<Key, Value> {
	Item<Key, Value> head = null;
	Item<Key, Value> current = head;

	MyLinkedList() {
	}

	Value find(Key k) {
		current = head;
		if (head == null) {
			return null;
		} else if (k.equals(head.key)) {
			return head.value;
		} else {
			while (current.next != null) {
				current = current.next;
				if (current.key.equals(k)) {
					return current.value;
				}
			}
			current = head;
			return null;
		}
	}

	Value add(Item<Key, Value> i) {
		current = head;
		if (head == null) {
			head = i;
			current = head;
			return null;
		} else if (i.key.equals(head.key)) {
			Value temp = current.value;
			head.value = i.value;
			return temp;
		} else {
			while (current.next != null) {
				current = current.next;
				if (current.key.equals(i.key)) {
					Value temp = current.value;
					current.value = i.value;
					return temp;
				}

			}
			current.next = i;
			current = head;
			return null;
		}
	}

	Value delete(Key k) {
		current = head;
		if (head == null) {
			return null;
		} else if (head.key.equals(k)) {
			Value temp = head.value;
			head = head.next;
			current = head;
			return temp;
		} else {
			Item<Key, Value> previous = current;
			while (current.next != null) {
				current = current.next;
				if (current.key.equals(k)) {
					Value temp1 = current.value;
					previous.next = current.next;
					return temp1;
				}
				previous = current;
			}
			return null;
		}
	}

}

class Item<Key, Value> {
	Key key = null;
	Value value = null;
	Item<Key, Value> next = null;

	Item(Key k, Value v) {
		if (k == null || v == null) {
			throw new IllegalArgumentException();
		}

		this.key = k;
		this.value = v;

	}
}
