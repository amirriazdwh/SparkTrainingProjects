package training.rddFileProcessing

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

// Based on the code above, we would infer that the file 'words.txt' will be read during the execution of
// Transformation operation (1). But this never happens in Spark. Instead, the file will only be read during the
// execution of action operation (2). The benefit of this Lazy Evaluation is, we only need to read the first line
// from the File instead of the whole file & also there is no need to store the complete file content in Memory
//Thus we can say that, Transformations in Spark is Lazily evaluated and Spark will not evaluate the Transformations
// until it sees an action.

//Lineage Graph
//When we create new RDDs based on the existing RDDs, Spark manage these dependencies using Lineage Graph.

package object CachedPartition {
  val conf = new SparkConf().setAppName("CachedPartition")
  val sc = new SparkContext(conf)
  val lines = sc.textFile("hdfs:///user/raj/data.txt", 3)
  //lines: org.apache.spark.rdd.RDD[String] = hdfs:///user/raj/data.txt MapPartitionsRDD[1] at textFile at <console>:28
  //lines.partitions.size
  // flatMap() : One of many transformation

  val words = lines.flatMap(x => x.split(" "))
  //  words: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[2] at flatMap at <console>:30
  // Persist the data
  val units = words.map(word => (word, 1)).persist(StorageLevel.MEMORY_ONLY)
  //units: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[3] at map at <console>:32
  val counts = units.reduceByKey((x, y) => x + y)
  //counts: org.apache.spark.rdd.RDD[(String, Int)] = ShuffledRDD[4] at reduceByKey at <console>:34
  // Text file is read to compute the 'counts' RDD
  counts.toDebugString

  // First Action
  counts.collect()
  //res2: Array[(String, Int)] = Array((another,1), (This,2), (is,2), (a,1), (test,2))

  val counts2 = units.reduceByKey((x, y) => x * y)
  //counts2: org.apache.spark.rdd.RDD[(String, Int)] = ShuffledRDD[5] at reduceByKey at <console>:34

  // Cache value is read to compute the 'counts2' RDD
  counts2.toDebugString

  // Second Action
  counts2.collect()
  //res4: Array[(String, Int)] = Array((another,1), (This,1), (is,1), (a,1), (test,1))


}

//The textFile method also takes an optional second argument for controlling the number of
// partitions of the file. By default, Spark creates one partition for each block of the
// file (blocks being 64MB by default in HDFS), but you can also ask for a higher number
// of partitions by passing a larger value. Note that you cannot have fewer partitions
// than blocks.

