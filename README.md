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

The last call is using the utility class. We could have also used the each method of the list:

```java
		numberListWithExtensions.each(new F.Runner<Integer>() {
			public void run(Integer o) {
				System.out.println(o);
			}
		});
```
