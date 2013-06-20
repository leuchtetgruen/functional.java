package de.leuchtetgruen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
	
	public static void each(HashMap<Object, Object> map, HashRunner r) {
		Set<Object> s = map.keySet();
		for (Object k : s) {
			r.run(k, map.get(k));
		}
	}
	
	// MAP
	public static List<Object> map(Collection<?> c, Mapper r) {
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
	public static List<Object> filter(Collection<?> c,Decider r) {
		ArrayList<Object> ret = new ArrayList<Object>();
		for (Object o: c) {
			if (r.decide(o)) ret.add(o);
		}
		return ret;
	}
	
	public static Object[] filter(Object[] arr, Decider r) {
		ArrayList<Object> ret = new ArrayList<Object>();
		for (Object o: arr) {
			if (r.decide(o)) ret.add(o);
		}
		return ret.toArray(new Object[ret.size()]);
	}
	
	// FIND
	public static Object find(Collection<?> c, Decider r) {
		for (Object o: c) {
			if (r.decide(o)) return o;
		}
		return null;
	}
	
	public static Object find(Object[] arr, Decider r) {
		for (Object o: arr) {
			if (r.decide(o)) return o;
		}
		return null;
	}
	
	// REJECT
	public static List<Object> reject(Collection<?> c,Decider r) {
		ArrayList<Object> ret = new ArrayList<Object>();
		for (Object o: c) {
			if (!r.decide(o)) ret.add(o);
		}
		return ret;
	}
	
	public static Object[] reject(Object[] arr, Decider r) {
		ArrayList<Object> ret = new ArrayList<Object>();
		for (Object o: arr) {
			if (!r.decide(o)) ret.add(o);
		}
		return ret.toArray(new Object[ret.size()]);
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
	public static List<?> sortWithoutCopy(List<?> c, Comparator<Object> r) {
		Collections.sort(c, r);
		return c;
	}
	
	public static Object[] sortWithoutCopy(Object[] arr, Comparator<Object> r) {
		Object[] copy = arr.clone();
		Arrays.sort(copy, r);
		return copy;
	}
	
	// MIN
	public static Object min(Collection<?> c, final Comparator<Object> r) {
		Object min = null;
		return reduce(c, new Reducer() {
			
			@Override
			@SuppressWarnings("unchecked")
			public Object reduce(Object memo, Object o) {
				if (memo == null) return o;
				
				Comparable<Object> co = (Comparable<Object>) o;
				Comparable<Object> cm = (Comparable<Object>) memo;
				int result = r.compare(co, cm); 
				if (result < 0) {
					return co;
				}
				else {
					return cm;
				}
			}
		}, min);
	}
	
	public static Object min(Object[] arr, final Comparator<Object> r) {
		Object min = null;
		return reduce(arr, new Reducer() {
			
			@Override
			@SuppressWarnings("unchecked")
			public Object reduce(Object memo, Object o) {
				if (memo == null) return o;
				
				Comparable<Object> co = (Comparable<Object>) o;
				Comparable<Object> cm = (Comparable<Object>) memo;
				int result = r.compare(co, cm); 
				if (result < 0) {
					return co;
				}
				else {
					return cm;
				}
			}
		}, min);
	}
	
	// MAX
	public static Object max(Collection<?> c, final Comparator<Object> r) {
		Object max = null;
		return reduce(c, new Reducer() {
			
			@SuppressWarnings("unchecked")
			@Override
			public Object reduce(Object memo, Object o) {
				if (memo == null) return o;
				
				Comparable<Object> co = (Comparable<Object>) o;
				Comparable<Object> cm = (Comparable<Object>) memo;
				int result = r.compare(co, cm); 
				if (result > 0) {
					return co;
				}
				else {
					return cm;
				}
			}
		}, max);
	}
	
	public static Object max(Object[] arr, final Comparator<Object> r) {
		Object max = null;
		return reduce(arr, new Reducer() {
			
			@Override
			@SuppressWarnings("unchecked")
			public Object reduce(Object memo, Object o) {
				if (memo == null) return o;
				
				Comparable<Object> co = (Comparable<Object>) o;
				Comparable<Object> cm = (Comparable<Object>) memo;
				int result = r.compare(co, cm); 
				if (result > 0) {
					return co;
				}
				else {
					return cm;
				}
			}
		}, max);
	}
	
	// GROUP
	public static HashMap<Object, Object> group(Collection<?> c, Mapper r) {
		HashMap<Object, Object> ret = new HashMap<Object, Object>();
		for (Object o: c) {
			Object mapped = r.map(o);
			@SuppressWarnings("unchecked")
			ArrayList<Object> list = (ArrayList<Object>) ret.get(mapped);
			if (list==null) {
				list = new ArrayList<Object>();
			}
			list.add(o);
			ret.put(mapped, list);
		}
		
		return ret;
	}
	
	public static HashMap<Object, Object> group(Object[] arr, Mapper r) {
		HashMap<Object, Object> ret = new HashMap<Object, Object>();
		for (Object o: arr) {
			Object mapped = r.map(o);
			@SuppressWarnings("unchecked")
			ArrayList<Object> list = (ArrayList<Object>) ret.get(mapped);
			if (list==null) {
				list = new ArrayList<Object>();
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
		
		public static void iterateOverGroup(HashMap<Object, Object> group, final GroupIterator i) {
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
