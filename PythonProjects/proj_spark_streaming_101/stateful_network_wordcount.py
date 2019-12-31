import sys

from pyspark import SparkContext
from pyspark.streaming import StreamingContext

def update_function(new_values, running_count):
    if running_count is None:
        running_count = 0
    return sum(new_values, running_count)  # add the new values with the previous running count to get the new count

if __name__ == "__main__":
      # Create a local StreamingContext with two working thread and batch interval of 5 second
    sc = SparkContext()
    ssc = StreamingContext(sc, 5)

    # configure checkpoint dir
    ssc.checkpoint("hdfs://localhost/user/cloudera/spark_streaming_101/checkpoint")

    # Create a DStream that will connect to hostname:port, like localhost:9999
    lines = ssc.socketTextStream("localhost", 9999)

    # Split each line into words
    words = lines.flatMap(lambda line: line.split(" "))

    # Count each word in each batch
    pairs = words.map(lambda word: (word, 1))
    word_counts = pairs.reduceByKey(lambda x, y: x + y)


    running_counts = word_counts.updateStateByKey(update_function)


    # Print the first ten elements of each RDD generated in this DStream to the console
    word_counts.pprint()
    running_counts.pprint()

    ssc.start()             # Start the computation
    ssc.awaitTermination()  # Wait for the computation to terminate