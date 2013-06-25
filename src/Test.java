import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.leuchtetgruen.F;

public class Test {
	

	public static void main(String[] args) {
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
		
		F.Utils.LazyIntegerSet s = new F.Utils.LazyIntegerSet(1, 5);
		F.Utils.print(s);
		
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
