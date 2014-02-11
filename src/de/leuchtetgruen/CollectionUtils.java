package de.leuchtetgruen;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.leuchtetgruen.F;

public class CollectionUtils<T> extends AbstractCollection<T> {

	private Collection<T> collection;
	
	public CollectionUtils(Collection<T> collection) {
		this.collection = collection;
	}
	
	public CollectionUtils() {
		this.collection = new ArrayList<T>();
	}
	
	@Override
	public Iterator<T> iterator() {
		return collection.iterator();
	}

	@Override
	public int size() {
		return collection.size();
	}
	
	public String join(String delimiter) {
		StringBuilder build = new StringBuilder("");
		for (T element : this) {
			build.append(element);
			build.append(delimiter);
		}
		if (build.length() > 0) {
			build.replace(build.length() - delimiter.length(), build.length(), ""); // remove the last delimiter
		}
		return build.toString();
	}
	
	public void each(F.Runner<T> runner) {
		F.each(this, runner);
	}
	
	public CollectionUtils<T> filter(F.Decider<T> decider) {
		return new CollectionUtils<T>(F.filter(this, decider));
	}
	
	public T find(F.Decider<T> decider) {
		return F.find(this, decider);
	}
	
	public <U> HashMap<U, List<T>> group(F.Mapper<T, U> mapper) {
		return F.group(this, mapper);
	}
	
	public boolean isValidForAll(F.Decider<T> decider) {
		return F.isValidForAll(this, decider);
	}
	
	public boolean isValidForAny(F.Decider<T> decider) {
		return F.isValidForAny(this, decider);
	}
	
	public <U> CollectionUtils<U> map(F.Mapper<T, U> mapper) {
		return new CollectionUtils<U>(F.map(this, mapper));
	}
	
	public T max(F.Comparator<T> comparator) {
		return F.max(this, comparator);
	}
	
	public T min(F.Comparator<T> comparator) {
		return F.min(this, comparator);
	}
	
	public <U> U reduce(F.Reducer<T, U> reducer, U memo) {
		return F.reduce(this, reducer, memo);
	}
	
	public CollectionUtils<T> reject(F.Decider<T> decider) {
		return new CollectionUtils<T>(F.reject(this, decider));
	}

	
	public Set<T> toSet() {
		return new HashSet<T>(this);
	}
	
	public List<T> toList() {
		return new ArrayList<T>(this);
	}
	
	public CollectionUtils<T> withoutEmpty() {
		return reject(new F.Decider<T>() {
			public boolean decide(T o) {
				return (o==null);
			}
		});
	}
	
}
