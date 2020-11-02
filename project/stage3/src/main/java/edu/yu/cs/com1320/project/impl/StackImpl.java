package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Stack;

/**
 * @param <T>
 */
public class StackImpl<T> implements Stack<T> {
	class Head {
		Com<T> headOfStack;
	}

	class Com<T> {
		T item = null;
		Com<T> next = null;
	}

	Head head = new Head(); 

	public StackImpl() {
	}

	/**
	 * @param element object to add to the Stack
	 */
	public void push(T element) {
		// TODO Auto-generated method stub
		if (head.headOfStack == null) {
			Com<T> pile = new Com<T>();
			pile.item = element;
			head.headOfStack = pile;
			// System.out.println(pile.item);
		} else {
			Com<T> pile = new Com<T>();
			pile.item = element;
			pile.next = head.headOfStack;
			head.headOfStack = pile;
			// System.out.println(pile.item);
		}
	}

	/**
	 * removes and returns element at the top of the stack
	 * 
	 * @return element at the top of the stack, null if the stack is empty
	 */
	public T pop() {
		if (head.headOfStack == null) {
			return null;
		}

		Com<T> lastIn = head.headOfStack;
		head.headOfStack = lastIn.next;
		// System.out.println(lastIn.item);
		return lastIn.item;
	}

	/**
	 * @return the element at the top of the stack without removing it
	 */
	public T peek() {
		if (head.headOfStack == null) {
			return null;
		}
		Com<T> lastIn = head.headOfStack;
		return lastIn.item;
	}
	/**
	 * @return how many elements are currently in the stack
	 */
	public int size() {
		Com<T> counter = head.headOfStack;
		int count = 0;
		if (head.headOfStack != null) {
			counter = head.headOfStack.next;
			count++;
			while (counter != null) {
				counter = counter.next;
				count++;
			}
		}
		return count;
		// TODO Auto-generated method stub
	}

}
