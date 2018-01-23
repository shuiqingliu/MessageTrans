package com.jyt.util;

import java.util.Random;

public class UniqueStringGenerator {

	private UniqueStringGenerator() {
	}

	public static synchronized String getUniqueString() {
		
		if (generateCount > 99999)
			generateCount = 0;
		String uniqueNumber = (Long.toString(System.currentTimeMillis()%1000))
				+"_"+ Integer.toString(generateCount)+"_"+random.nextInt(1000);
		generateCount++;
		return uniqueNumber;
	}

	private static int generateCount = 0;
	private static Random random = new Random();

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(getUniqueString());
		}
	}
}
