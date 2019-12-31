package com.iii.udf;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.hive.serde2.io.DoubleWritable;

import java.util.ArrayList;
import java.util.List;

public class StdDiviation extends UDAF {

	public static class StdEvaluator implements UDAFEvaluator {

		public void init() {
			partialResult = null;
		}

		public static class PartialResult {
			int numberOfItems;
			double sumOfItems;
			List<Double> allItems = new ArrayList<Double>();
		}

		private PartialResult partialResult;

		public boolean iterate(DoubleWritable value) {
			if (value == null) {
				return true;
			}
			if (partialResult == null) {
				partialResult = new PartialResult();
			}
			partialResult.numberOfItems += 1;
			partialResult.sumOfItems = partialResult.sumOfItems + value.get();
			partialResult.allItems.add(value.get());
			return true;
		}

		public PartialResult terminatePartial() {
			return partialResult;
		}

		public boolean merge(PartialResult other) {
			if (other == null) {
				return true;
			}
			if (partialResult == null) {
				partialResult = new PartialResult();
			}

			partialResult.allItems.addAll(other.allItems);
			partialResult.numberOfItems += other.numberOfItems;
			partialResult.sumOfItems += other.sumOfItems;

			return true;
		}

		public DoubleWritable terminate() {
			if (partialResult == null) {
				return null;
			}
			double mean = partialResult.sumOfItems
					/ partialResult.numberOfItems;
			double sum_of_squares = 0;
			for (double term : partialResult.allItems) {
				sum_of_squares += (term - mean) * (term - mean);
			}

			return new DoubleWritable(Math.sqrt(sum_of_squares
					/ partialResult.numberOfItems));

		}

	}
}
