package de.leuchtetgruen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Iterator;


/**
 * @author Hannes Walz<info@leuchtetgruen.de>
 *
 * This class provides some functional programming to collection/array handling in
 * java allowing you to write much more concise code. 
 *
 */
public class F {
	// INTERFACES
	public static interface Runner<T> {
		public void run(T o);
	};
	
	public static interface HashRunner<T,U> {
		public void run(T k, U v);
	}
	
	public static interface Mapper<T,U> {
		public U map(T o);
	}
	
	public static interface Reducer<T,U> {
		public U reduce(U memo, T o);
	}
	
	public static interface Decider<T> {
		public boolean decide(T o);
	}
	
	
	public static interface Comparator<T> {
		public int compare(T o1, T o2);
	}
	
	
	// EACH
	public static <T> void each(Iterable<T> c, Runner<T> r) {
		for (T o : c) {
			r.run(o);
		}
	}
	
	public static <T> void each(T[] arr, Runner<T> r) {
		for (T o : arr) {
			r.run(o);
		}
	}
	
	public static <T,U> void each(HashMap<T, U> map, HashRunner<T,U> r) {
		Set<T> s = map.keySet();
		for (T k : s) {
			r.run(k, map.get(k));
		}
	}
	
	// MAP
	public static <T,U> List<U> map(Iterable<T> c, Mapper<T,U> r) {
		ArrayList<U> ret = new ArrayList<U>();
		for (T o : c) {
			ret.add(r.map(o));
		}
		return ret;
	}
	
	public static <T,U> List<U> map(T[] arr, Mapper<T,U> r) {
		ArrayList<U> ret = new ArrayList<U>();
		for (T o : arr) {
			ret.add(r.map(o));
		}
		return ret;
	}
	
	
	
	// REDUCE
	public static <T,U> U reduce(Iterable<T> c, Reducer<T,U> r, U memo) {
		for (T o : c) {
			memo = r.reduce(memo, o);
		}
		return memo;
	}
	
	public static <T,U> U reduce(T[] arr, Reducer<T,U> r, U memo) {
		for (T o : arr) {
			memo = r.reduce(memo, o);
		}
		return memo;
	}
	
	// FILTER
	public static <T> List<T> filter(Iterable<T> c,Decider<T> r) {
		ArrayList<T> ret = new ArrayList<T>();
		for (T o: c) {
			if (r.decide(o)) ret.add(o);
		}
		return ret;
	}
	
	public static <T> T[] filter(T[] arr, Decider<T> r) {
		ArrayList<T> ret = new ArrayList<T>();
		for (T o: arr) {
			if (r.decide(o)) ret.add(o);
		}
		@SuppressWarnings("unchecked")
		T[] array = (T[]) new Object[ret.size()];
		return ret.toArray(array);
	}
	
	// FIND
	public static <T> T find(Iterable<T> c, Decider<T> r) {
		for (T o: c) {
			if (r.decide(o)) return o;
		}
		return null;
	}
	
	public static <T> T find(T[] arr, Decider<T> r) {
		for (T o: arr) {
			if (r.decide(o)) return o;
		}
		return null;
	}
	
	// REJECT
	public static <T> List<T> reject(Iterable<T> c,Decider<T> r) {
		ArrayList<T> ret = new ArrayList<T>();
		for (T o: c) {
			if (!r.decide(o)) ret.add(o);
		}
		return ret;
	}
	
	public static <T> T[] reject(T[] arr, Decider<T> r) {
		ArrayList<T> ret = new ArrayList<T>();
		for (T o: arr) {
			if (!r.decide(o)) ret.add(o);
		}
		@SuppressWarnings("unchecked")
		T[] array = (T[]) new Object[ret.size()];
		return ret.toArray(array);
	}
	
	// ISVALIDFORALL
	public static <T> boolean isValidForAll(Iterable<T> c, Decider<T> r) {
		boolean all = true;
		for (T o: c) {
			all = all && r.decide(o);
		}
		return all;
	}
	
	public static <T >boolean isValidForAll(T[] arr, Decider<T> r) {
		boolean all = true;
		for (T o: arr) {
			all = all && r.decide(o);
		}
		return all;
	}
	
	// ISVALIDFORANY
	public static <T> boolean isValidForAny(Iterable<T> c, Decider<T> r) {
		boolean all = false;
		for (T o: c) {
			all = all || r.decide(o);
		}
		return all;
	}
	
	public static <T> boolean isValidForAny(T[] arr, Decider<T> r) {
		boolean all = false;
		for (T o: arr) {
			all = all || r.decide(o);
		}
		return all;
	}
	
	// COUNTVALIDENTRIES
	public static <T> int countValidEntries(Iterable<T> c, Decider<T> r) {
		int count = 0;
		for (T o : c) {
			if (r.decide(o)) count++;
		}
		return count;
	}
	
	public static <T> int countValidEntries(T[] arr, Decider<T> r) {
		int count = 0;
		for (T o : arr) {
			if (r.decide(o)) count++;
		}
		return count;
	}
	
	// SORT
	public static <T> List<T> sortWithoutCopy(List<T> c, java.util.Comparator<Object> r) {
		Collections.sort(c, r);
		return c;
	}
	
	public static <T> T[] sortWithoutCopy(T[] arr, java.util.Comparator<Object> r) {
		T[] copy = arr.clone();
		Arrays.sort(copy, r);
		return copy;
	}
	
	// MIN
	@SuppressWarnings("unchecked")
	public static <T> T min(Iterable<T> c, final Comparator<T> r) {
		T min = null;
		return reduce(c, new Reducer<T,T>() {
			public T reduce(T memo, T o) {
				if (memo == null) return o;
				int result = r.compare(o, memo); 
				if (result < 0) {
					return o;
				}
				else {
					return memo;
				}
			}
		}, min);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T min(T[] arr, final Comparator<T> r) {
		T min = null;
		return reduce(arr, new Reducer<T,T>() {
			public T reduce(T memo, T o) {
				if (memo == null) return o;
				int result = r.compare(o, memo); 
				if (result < 0) {
					return o;
				}
				else {
					return memo;
				}
			}
		}, min);
	}
	
	// MAX
	@SuppressWarnings("unchecked")
	public static <T> T max(Iterable<T> c, final Comparator<T> r) {
		T max = null;
		return reduce(c, new Reducer<T,T>() {
			public T reduce(T memo, T o) {
				if (memo == null) return o;
				int result = r.compare(o, memo); 
				if (result > 0) {
					return o;
				}
				else {
					return memo;
				}
			}
		}, max);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T max(T[] arr, final Comparator<T> r) {
		T max = null;
		return reduce(arr, new Reducer<T,T>() {
			public T reduce(T memo, T o) {
				if (memo == null) return o;
				int result = r.compare(o, memo); 
				if (result > 0) {
					return o;
				}
				else {
					return memo;
				}
			}
		}, max);
	}
	
	// GROUP
	public static <T, U> HashMap<U, List<T>> group(Iterable<T> c, Mapper<T,U> r) {
		HashMap<U, List<T>> ret = new HashMap<U, List<T>>();
		for (T o: c) {
			U mapped = r.map(o);
			ArrayList<T> list = (ArrayList<T>) ret.get(mapped);
			if (list==null) {
				list = new ArrayList<T>();
			}
			list.add(o);
			ret.put(mapped, list);
		}
		
		return ret;
	}
	
	public static <T, U> HashMap<U, List<T>> group(T[] arr, Mapper<T,U> r) {
		HashMap<U, List<T>> ret = new HashMap<U, List<T>>();
		for (T o: arr) {
			U mapped = r.map(o);
			ArrayList<T> list = (ArrayList<T>) ret.get(mapped);
			if (list==null) {
				list = new ArrayList<T>();
			}
			list.add(o);
			ret.put(mapped, list);
		}
		
		return ret;
	}
	
	// UTILS
	public static class Utils {
		
		public static class Printer<T> implements Runner<T> {
			public void run(T o) {
				System.out.println(o);
			}
		}
		
		public static <T> void print(Iterable<T> c) {
			each(c, new Printer<T>());
		}
		
		public static <T> void print(T[] arr) {
			each(arr, new Printer<T>());
		}
		
		public static interface GroupIterator {
			public void onNewGroup(Object k);
			public void onNewEntry(Object v);
		}
		
		public static GroupIterator groupPrinter = new GroupIterator() {
			
			@Override
			public void onNewGroup(Object k) {
				System.out.println(k);
			}
			
			@Override
			public void onNewEntry(Object v) {
				System.out.println("\t" + v);
			}
		}; 
		
		public static <T, U> void iterateOverGroup(HashMap<T, List<U>> group, final GroupIterator i) {
			each(group, new HashRunner<T,List<U>>() {
				public void run(T k, List<U> v) {
					i.onNewGroup(k);
					List<U> l = (List<U>) v;
					each(l, new Runner<U>() {
						public void run(U o) {
							i.onNewEntry(o);
						}						
					});
				}
			});
		}
		
		public static int COMPARATOR_FIRST_IS_GREATER 	= -1;
		public static int COMPARATOR_BOTH_ARE_EQUAL		= 0;
		public static int COMPARATOR_SECOND_IS_GREATER 	= 1;
		
		public static int intCompare(int i1, int i2) {
			if (i1==i2) return 0;
			return (i1 > i2) ? 1 : -1;
		}
		
		public static int doubleCompare(double d1, double d2) {
			if (d1==d2) return 0;
			return (d1 > d2) ? 1 : -1;
		}
		
		public static int longCompare(long l1, long l2) {
			if (l1==l2) return 0;
			return (l1 > l2) ? 1 : -1;
		}
		
		public static int floatCompare(float f1, float f2) {
			if (f1==f2) return 0;
			return (f1 > f2) ? 1 : -1;
		}
		
		public static Collection<Integer> indexSet(Collection<?> c) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			for (int i=0; i< c.size(); i++) {
				l.add(i);
			}
			return l;
		}
		
		// Lazy sets
		public static class LazyIntegerSet implements Iterable<Integer>, Iterator<Integer> {
			private int from;
			private int to;
			private int current;

			public LazyIntegerSet(int from, int to) {
				this.from = from;
				this.to = to;
				this.current = from - 1;
			}

			public boolean hasNext() {
				return (current < to);
			}

			public Integer next() {
				current++;
				return current;
			}

			public Iterator<Integer> iterator() {
				return this;
			}

			public void remove() {
				// does nothing
			}
		}
		
	}
}
