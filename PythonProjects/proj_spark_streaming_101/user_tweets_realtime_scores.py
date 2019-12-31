from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils
from afinn import Afinn

if __name__ == "__main__":

    sc = SparkContext()
    ssc = StreamingContext(sc, 1)

    # A sentiment analysis model
    model = Afinn()

    raw_stream = KafkaUtils.createStream(ssc, "localhost:2182", "consumer-group", {"tweets_stream": 1}) \
                           .window(60, 5)

    raw_stream.mapValues(lambda message: (model.score(message), 1)) \
              .reduceByKey(lambda x, y: (x[0] + y[0], x[1] + y[1])) \
              .mapValues(lambda x: x[0] / x[1]) \
              .pprint(20)


    # Start it
    ssc.start()
    ssc.awaitTermination()