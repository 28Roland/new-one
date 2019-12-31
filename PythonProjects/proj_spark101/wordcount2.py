from pyspark import SparkContext

if __name__ == "__main__":
    sc = SparkContext()

    lines = sc.textFile("hdfs://localhost/user/cloudera/spark101/war_and_peace.txt")

    words = lines.flatMap(lambda x: x.split(" "))

    word_counts = words.countByValue()

    for (w, c) in word_counts.items():
        print(w, c)
