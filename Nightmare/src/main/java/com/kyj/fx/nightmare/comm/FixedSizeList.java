/**
 * 
 */
package com.kyj.fx.nightmare.comm;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 */
public class FixedSizeList<E> implements List<E> {

	private LinkedList<E> list = new LinkedList<>();
	/**
	 * 
	 */
	private int maxSize;

	public FixedSizeList(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public boolean add(E element) {
		if (this.size() < maxSize) {
			return list.add(element);
		} else {
			list.removeFirst();
			return list.add(element);
		}
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		
		if(c.size() == maxSize)
		{
			list.clear(); list.addAll(c);
		}
		else if( c.size() > maxSize)
		{
			int start = c.size() - maxSize;
			c.stream().skip(start).limit(c.size()).forEach(a -> add(a));
		}
		c.forEach(a -> add(a));
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new RuntimeException("Not implementation");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		list.clear();

	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override
	public E set(int index, E element) {
		return list.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		list.add(index, element);
	}

	@Override
	public E remove(int index) {
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

}
