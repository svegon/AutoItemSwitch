package net.autoitemswitch.utils;

import java.util.ArrayList;

public final class CollectionUtils {
	private CollectionUtils() {
		throw new AssertionError();
	}
	
	public static <O> ArrayList<O> copy(ArrayList<O> list){
		ArrayList<O> newList = new ArrayList<O>();
		
		newList.addAll(list);
		
		return newList;
	}
}
