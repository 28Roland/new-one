from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from kafka import KafkaProducer


def output_partition(partition):
    # Create producer
    producer = KafkaProducer(bootstrap_servers=broker_list)
    # Get each (word,count) pair and send it to the topic by iterating the partition (an Iterable object)
    for word, count in partition:
        message = "{},{}".format(word, str(count))
        producer.send(topic, value=bytes(message, "utf8"))

    producer.close()


def output_rdd(rdd):
    rdd.foreachPartition(output_partition)


if __name__ == "__main__":

    topic = "wordcount_result"
    broker_list = 'localhost:9092,localhost:9093'

    # Create a local StreamingContext with two working thread and batch interval of 1 second
    sc = SparkContext()
    ssc = StreamingContext(sc, 5)

    # Create a DStream that will connect to hostname:port, like localhost:9999
    lines = ssc.socketTextStream("localhost", 9999)

    # Split each line into words
    words = lines.flatMap(lambda line: line.split(" "))

    # Count each word in each batch
    pairs = words.map(lambda word: (word, 1))
    word_counts = pairs.reduceByKey(lambda x, y: x + y)

    word_counts.foreachRDD(output_rdd)

    ssc.start()             # Start the computation
    ssc.awaitTermination()  # Wait for the computation to terminate
