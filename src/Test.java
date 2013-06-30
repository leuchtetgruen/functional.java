import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.leuchtetgruen.F;

public class Test {
	

	public static void main(String[] args) throws Exception {
		String[] arr = { "New York", "Rio", "Tokyo", "New Mexico" };
		List<String> c = Arrays.asList(arr);

		testEach(arr,c);
		testMap(arr, c);
		testReduce(arr, c);
		testFilterAndRejectAndFind(arr, c);
		testAnyAllCountValid(arr, c);
		testSortWithoutCopy(arr, c);
		testMinMax(arr, c);
		testGroup(arr, c);
		testLazyEvaluation();
		
	
		
		//testConcurrency();
		
		// System.out.println("..");
		// 		F.LazyList<Future<Integer>> ll = F.FixedThreadConcurrency.getLazyList(new F.LazyListDataSource<Integer>() {
		// 			public Integer get(int i, F.LazyList<Integer> l)  {
		// 				return i;
		// 			}
		// 			
		// 			public int size() {
		// 				return 5;
		// 			}
		// 			
		// 			public boolean shouldCache() {
		// 				return true;
		// 			}
		// 		});
		// 		
		// 		for (Future<Integer> fi : ll) {
		// 			System.out.println(fi.get());
		// 		}
		// 		
		// 		F.FixedThreadConcurrency.finishService();
	}
	
	

	
	public static void testConcurrency() {
		final F.LazyList<Integer> l = new F.Utils.LazyIntegerList(1, 500000);
		final F.Decider<Integer> primeDecider = new F.Decider<Integer>() {
																					public boolean decide(Integer i) {
																						for (int j=2; j < (i/2); j++) {
																							if ((i%j)==0) return false;
																						}
																						return true;
																					}
																				};
																				

		System.out.println("Finding primes between 1 and 500000 without concurrency...");
		F.Utils.benchmark(new Runnable() {
			public void run() {
				List<Integer> primes = F.filter(l, primeDecider);
			}
		});
		
		System.out.println("Finding primes between 1 and 500000 with concurrency...");		
		F.Utils.benchmark(new Runnable() {
			public void run() {
				try {
						List<Integer> primes = F.FixedThreadConcurrency.filter(l, primeDecider);
				} catch (Exception e) {
					System.err.println("Oh noes something went wrong");
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public static void testLazyEvaluation() {
		F.LazyList<Long> fibonacciList = new F.LazyList<Long>(new F.LazyListDataSource<Long>() {
			public Long get(int i, F.LazyList<Long> ll) {
				if (i >= 2) {
					return ll.get(i-1) + ll.get(i-2);
				}
				else {
					return (long) i;
				}
			}
			
			public int size() {
				return 50;
			}
			
			public boolean shouldCache() {
				return true;
			}
		});
		
		F.Utils.print(fibonacciList);
				
		System.out.println("Fibonacci list from 2 to 10");
		F.LazyList<Long> fibonacciSublist = (F.LazyList<Long>) fibonacciList.subList(2,10); // still lazy
		Long[] fibonacciArr = fibonacciSublist.toArray(new Long[fibonacciSublist.size()]); // toArray makes it become non lazy
		F.Utils.print(fibonacciArr);
		

		// or we could do it like this
		F.LazyList<Long> fib = F.infiniteLazyList(new F.LazyMapper<Integer, Long>() {
			public Long map(Integer n, F.LazyList<Long> l) {
				return ((n==0) || (n==1)) ? n : l.get(n-2) + l.get(n-1);
			}
		});
		
		F.Utils.print(fib.subList(2,10));
		

	}

	private static void testEach(String[] arr, List<String> c) {
		System.out.println("Testing -> each");
		
		F.each(c, new F.Utils.Printer<String>());
		System.out.println("--");
		F.each(arr, new F.Utils.Printer<String>());
	}

	
	private static void testMap(String[] arr, List<String> c) {
		System.out.println("Testing -> map");
		F.Mapper<String, Integer> m = new F.Mapper<String, Integer>() {
			
			@Override
			public Integer map(String o) {
				return o.length();
			}
		};
		
		F.Utils.print(F.map(arr, m));
		System.out.println("--");
		F.Utils.print(F.map(c, m));
	}
	
	private static void testReduce(String[] arr, List<String> c) {
		System.out.println("Testing -> reduce");
		
		F.Reducer rTotalLength = new F.Reducer<String, Integer>() {
			@Override
			public Integer reduce(Integer memo, String o) {
				return memo + o.length();
			}
		};
		
		System.out.println(F.reduce(c, rTotalLength, 0));
		System.out.println(F.reduce(arr, rTotalLength, 0));
	}
	
	private static F.Decider<String> moreThanOneWordDecider = new F.Decider<String>() {
		
		@Override
		public boolean decide(String o) {
			return o.contains(" ");
		}
	};
	
	private static void testFilterAndRejectAndFind(String[] arr, List<String> c) {
		System.out.println("Testing -> filter and reject and find");
		
		F.Utils.print(F.filter(arr, moreThanOneWordDecider));
		System.out.println("--");
		F.Utils.print(F.filter(c, moreThanOneWordDecider));
		System.out.println("~");
		F.Utils.print(F.reject(arr, moreThanOneWordDecider));
		System.out.println("--");
		F.Utils.print(F.reject(c, moreThanOneWordDecider));
		System.out.println("~");
		System.out.println(F.find(arr, moreThanOneWordDecider));
		System.out.println(F.find(c, moreThanOneWordDecider));
	}
	
	private static void testAnyAllCountValid(String[] arr, List<String> c) {
		System.out.println("Testing -> isValidForAny ..All and countValidEntries");
		
		System.out.println(F.isValidForAny(arr, moreThanOneWordDecider));
		System.out.println(F.isValidForAny(c, moreThanOneWordDecider));
		System.out.println("~");
		System.out.println(F.isValidForAll(arr, moreThanOneWordDecider));
		System.out.println(F.isValidForAll(c, moreThanOneWordDecider));
		System.out.println("~");
		System.out.println(F.countValidEntries(arr, moreThanOneWordDecider));
		System.out.println(F.countValidEntries(c, moreThanOneWordDecider));
	}
	
	private static Comparator<Object> stringLengthComparator = new Comparator<Object>() {
		public int compare(Object arg0, Object arg1) {
			int i1 = ((String) arg0).length();
			int i2 = ((String) arg1).length();
			if (i1==i2) return 0;
			return (i1 > i2) ? 1 : -1 ;
		}
	};
	
	private static de.leuchtetgruen.F.Comparator<String> FstringLengthComparator = new de.leuchtetgruen.F.Comparator<String>() {
		public int compare(String arg0, String arg1) {
			int i1 = ((String) arg0).length();
			int i2 = ((String) arg1).length();
			if (i1==i2) return 0;
			return (i1 > i2) ? 1 : -1 ;
		}
	};
	
	private static void testSortWithoutCopy(String[] arr, List<String> c) {
		System.out.println("Testing -> sortWithoutCopy");
		
		F.Utils.print(F.sortWithoutCopy(arr, stringLengthComparator));
		System.out.println("--");
		F.Utils.print(F.sortWithoutCopy(c, stringLengthComparator));
	}
	
	private static void testMinMax(String[] arr, List<String> c) {
		System.out.println("Testing -> min / max");
		
		System.out.println(F.min(c, FstringLengthComparator));
		System.out.println(F.min(arr, FstringLengthComparator));
		System.out.println("~");
		System.out.println(F.max(c, FstringLengthComparator));
		System.out.println(F.max(arr, FstringLengthComparator));
		
	}
	
	private static void testGroup(String[] arr, List<String> c) {
		System.out.println("Testing -> group");
		
		F.Mapper<String, String> m = new F.Mapper<String, String>() {
			public String map(String o) {
				return o.subSequence(0, 1).toString();
			}
		};
		
		F.Utils.iterateOverGroup(F.group(arr, m), F.Utils.groupPrinter);
		System.out.println("--");
		F.Utils.iterateOverGroup(F.group(c, m), F.Utils.groupPrinter);
	}
}
