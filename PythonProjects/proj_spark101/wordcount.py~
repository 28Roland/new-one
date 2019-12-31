from pyspark import SparkContext

if __name__ == "__main__":
    sc = SparkContext()

    lines = sc.textFile("hdfs://localhost/user/cloudera/spark101/")

    words = lines.flatMap(lambda x: x.split(" "))

    word_counts = words.map(lambda x: (x, 1)).reduceByKey(lambda x, y: x + y)

    word_counts.saveAsTextFile("hdfs://localhost/user/cloudera/spark101/wordcount/output")
