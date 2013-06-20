package de.leuchtetgruen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * @author Hannes Walz<info@leuchtetgruen.de>
 *
 * This class provides some functional programming to collection/array handling in
 * java allowing you to write much more concise code. 
 *
 */
public class F {
	// INTERFACES
	public static interface Runner {
		public void run(Object o);
	};
	
	public static interface HashRunner {
		public void run(Object k, Object v);
	}
	
	public static interface Mapper {
		public Object map(Object o);
	}
	
	public static interface Reducer {
		public Object reduce(Object memo, Object o);
	}
	
	public static interface Decider {
		public boolean decide(Object o);
	}
	
	public static interface Cloneable {
		public Object clone();
	}
	
	public static interface Comparator<T> {
		public int compare(T o1, T o2);
	}
	
	
	// EACH
	public static void each(Collection<?> c, Runner r) {
		for (Object o : c) {
			r.run(o);
		}
	}
	
	public static void each(Object[] arr, Runner r) {
		for (Object o : arr) {
			r.run(o);
		}
	}
	
	public static void each(HashMap<?, ?> map, HashRunner r) {
		Set<?> s = map.keySet();
		for (Object k : s) {
			r.run(k, map.get(k));
		}
	}
	
	// MAP
	public static List<?> map(Collection<?> c, Mapper r) {
		ArrayList<Object> ret = new ArrayList<Object>();
		for (Object o : c) {
			ret.add(r.map(o));
		}
		return ret;
	}
	
	public static Object[] map(Object[] arr, Mapper r) {
		ArrayList<Object> ret = new ArrayList<Object>();
		for (Object o : arr) {
			ret.add(r.map(o));
		}
		return ret.toArray(new Object[ret.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> mapT(Object[] arr, Mapper r, Class<T> t) {
		ArrayList<T> ret = new ArrayList<T>();
		for (Object o : arr) {
			ret.add((T) r.map(o));
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> mapT(Collection<?> c, Mapper r, Class<T> t) {
		ArrayList<T> ret = new ArrayList<T>();
		for (Object o : c) {
			ret.add((T) r.map(o));
		}
		return ret;
	}
	
	// REDUCE
	public static Object reduce(Collection<?> c, Reducer r, Object memo) {
		for (Object o : c) {
			memo = r.reduce(memo, o);
		}
		return memo;
	}
	
	public static Object reduce(Object[] arr, Reducer r, Object memo) {
		for (Object o : arr) {
			memo = r.reduce(memo, o);
		}
		return memo;
	}
	
	// FILTER
	public static <T> List<T> filter(Collection<T> c,Decider r) {
		ArrayList<T> ret = new ArrayList<T>();
		for (T o: c) {
			if (r.decide(o)) ret.add(o);
		}
		return ret;
	}
	
	public static <T> T[] filter(T[] arr, Decider r) {
		ArrayList<T> ret = new ArrayList<T>();
		for (T o: arr) {
			if (r.decide(o)) ret.add(o);
		}
		@SuppressWarnings("unchecked")
		T[] array = (T[]) new Object[ret.size()];
		return ret.toArray(array);
	}
	
	// FIND
	public static <T> T find(Collection<T> c, Decider r) {
		for (T o: c) {
			if (r.decide(o)) return o;
		}
		return null;
	}
	
	public static <T> T find(T[] arr, Decider r) {
		for (T o: arr) {
			if (r.decide(o)) return o;
		}
		return null;
	}
	
	// REJECT
	public static <T> List<T> reject(Collection<T> c,Decider r) {
		ArrayList<T> ret = new ArrayList<T>();
		for (T o: c) {
			if (!r.decide(o)) ret.add(o);
		}
		return ret;
	}
	
	public static <T> Object[] reject(T[] arr, Decider r) {
		ArrayList<T> ret = new ArrayList<T>();
		for (T o: arr) {
			if (!r.decide(o)) ret.add(o);
		}
		@SuppressWarnings("unchecked")
		T[] array = (T[]) new Object[ret.size()];
		return ret.toArray(array);
	}
	
	// ISVALIDFORALL
	public static boolean isValidForAll(Collection<?> c, Decider r) {
		boolean all = true;
		for (Object o: c) {
			all = all && r.decide(o);
		}
		return all;
	}
	
	public static boolean isValidForAll(Object[] arr, Decider r) {
		boolean all = true;
		for (Object o: arr) {
			all = all && r.decide(o);
		}
		return all;
	}
	
	// ISVALIDFORANY
	public static boolean isValidForAny(Collection<?> c, Decider r) {
		boolean all = false;
		for (Object o: c) {
			all = all || r.decide(o);
		}
		return all;
	}
	
	public static boolean isValidForAny(Object[] arr, Decider r) {
		boolean all = false;
		for (Object o: arr) {
			all = all || r.decide(o);
		}
		return all;
	}
	
	// COUNTVALIDENTRIES
	public static int countValidEntries(Collection<?> c, Decider r) {
		int count = 0;
		for (Object o : c) {
			if (r.decide(o)) count++;
		}
		return count;
	}
	
	public static int countValidEntries(Object[] arr, Decider r) {
		int count = 0;
		for (Object o : arr) {
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
	public static <T> T min(Collection<T> c, final Comparator<T> r) {
		Object min = null;
		return (T) reduce(c, new Reducer() {
			public Object reduce(Object memo, Object o) {
				if (memo == null) return o;
				int result = r.compare((T) o, (T) memo); 
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
		Object min = null;
		return (T) reduce(arr, new Reducer() {
			public Object reduce(Object memo, Object o) {
				if (memo == null) return o;
				int result = r.compare((T) o, (T) memo); 
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
	public static <T> T max(Collection<T> c, final Comparator<T> r) {
		Object max = null;
		return (T) reduce(c, new Reducer() {
			public Object reduce(Object memo, Object o) {
				if (memo == null) return o;
				int result = r.compare((T) o, (T) memo); 
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
		Object max = null;
		return (T) reduce(arr, new Reducer() {
			public Object reduce(Object memo, Object o) {
				if (memo == null) return o;
				int result = r.compare((T) o, (T) memo); 
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
	public static <T> HashMap<Object, List<T>> group(Collection<T> c, Mapper r) {
		HashMap<Object, List<T>> ret = new HashMap<Object, List<T>>();
		for (T o: c) {
			Object mapped = r.map(o);
			ArrayList<T> list = (ArrayList<T>) ret.get(mapped);
			if (list==null) {
				list = new ArrayList<T>();
			}
			list.add(o);
			ret.put(mapped, list);
		}
		
		return ret;
	}
	
	public static <T> HashMap<Object, List<T>> group(T[] arr, Mapper r) {
		HashMap<Object, List<T>> ret = new HashMap<Object, List<T>>();
		for (T o: arr) {
			Object mapped = r.map(o);
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
		public static F.Runner printer = new F.Runner() {
			public void run(Object o) {
				System.out.println(o);
			}
		};
		
		public static void print(Collection<?> c) {
			each(c, printer);
		}
		
		public static void print(Object[] arr) {
			each(arr, printer);
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
		
		public static <T> void iterateOverGroup(HashMap<Object, List<T>> group, final GroupIterator i) {
			each(group, new HashRunner() {
				public void run(Object k, Object v) {
					i.onNewGroup(k);
					@SuppressWarnings("unchecked")
					List<Object> l = (List<Object>) v;
					each(l, new Runner() {
						public void run(Object o) {
							i.onNewEntry(o);
						}						
					});
				}
			});
		}
	}
}
