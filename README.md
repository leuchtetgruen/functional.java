Functional.java
===============

A set of java classes that makes writing concise java applications (be it mobile or desktop) easier
by using the functional programm paradigm.

However, once Java 8 is released many of the classes, interfaces and methods of Funcitonal.java
will be obsolete. Still, they will be useful for mobile programming as android java (dalvik) is still
actually Java 5.

The package contains a subclass of AbstractCollection (CollectionUtils), with a copy constructor that extends a normal collection
with functional methods. More methods and utilities are defined in the F.java class. See the javadocs for full
information on what is contained in the package.

##Let's go

But here is a little preview of what you can expect:

First let's create a CollectionsUtils instance, that contains numbers from 1 to 10.

```java
	// Create a list that contains numbers 1 to 10
	List<Integer> numberList = new F.Utils.LazyIntegerList(1, 10);
	// Create a CollectionUtils instance of this list
	CollectionUtils<Integer> numberListWithExtensions = new CollectionUtils<Integer>(numberList);
	// Have it printed
	F.Utils.print(numberListWithExtensions);
```

##each 

The last call is using the utility class. We could have also used the each method of the list:

```java
	numberListWithExtensions.each(new F.Runner<Integer>() {
		public void run(Integer o) {
			System.out.println(o);
		}
	});
```

##map

Let's assume that we need the string representation of the double of each number of the list, so we would want to have "2","4","6",... Here's how we could do that:

```java
	CollectionUtils<String> doubleStrings = numberListWithExtensions.map(new F.Mapper<Integer, String>() {
		public String map(Integer i) {
			return Integer.toString(i * 2);
		}
	});
	
	System.out.println(doubleStrings.join(", ")); // 2, 4, 6, 8, 10, ...
```

As a bonus, using the join method of CollectionUtils we even have it as a comma separated string.

##reduce

Ok reducing is rather tricky. Say you want to have the sum of the numbers between 1 and 10. You could use a reduce method like this:

```java
	Integer sum = numberListWithExtensions.reduce(new F.Reducer<Integer, Integer>() {
		public Integer reduce(Integer memo, Integer i) {
			return memo + i;
		}
	}, 0);
	
	System.out.println(sum); // 55
```

Everytime the reduce method is called the memo parameter is the result of the computation so far. So for the first call it is 0 (because we passed 0 as the second parameter for the call). For the next call it is 0 + 1 = 1. For the next call it is 1 + 2 = 3. For the next it is 3 + 3 = 6 and so on. I hope you get the idea. You can also use this method to calculate the minimum or maximum of a collection (but there's a different easier method for that).


##filter and reject

Filter and reject are two sides of the same coin. Let's filter our list and only get the odd numbers:

```java
	CollectionUtils<Integer> oddNumbers = numberListWithExtensions.filter(new F.Decider<Integer>() {
		public boolean decide(Integer o) {
			return ((o % 2) == 1);
		}
	});
	
	System.out.println(oddNumbers.join(", ")); // 1, 3, 5, 7, 9
```

So if we wanted to have the even numbers we could have used the same decider but would have called reject instead.

##isValidforAll and isValidForAny

Say we want to know if any number in the list is divisible by 7. This is how we would do it:

```java
	boolean anyDivisibleBySeven = numberListWithExtensions.isValidForAny(new F.Decider<Integer>() {
		public boolean decide(Integer i) {
			return ((i % 7)==0);
		}
	});
	
	System.out.println(anyDivisibleBySeven ? "At least 1 of them is divisible by 7" : "Sorry - no");
```
