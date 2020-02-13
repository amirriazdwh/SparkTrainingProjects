package training.rddFileProcessing

import org.apache.spark.{SparkConf, SparkContext}

package object CollectExample {
  val conf = new SparkConf().setAppName("CollectExample")
  val sc = new SparkContext(conf)

  val inputrdd = sc.parallelize(Seq(("maths", 50), ("maths", 60), ("english", 65)))
  //inputrdd: org.apache.spark.rdd.RDD[(String, Int)] = ParallelCollectionRDD[29] at parallelize at :21
  //val result: RDD[(A, C)] = rdd.map { case (k, v) => (k, f(v)) } v are values, k is key and f is function
  // this works with key values pairs, the key is preserved and value is given for example here
  // mark for math is 50 so mark=50. the key does not come, so its return two value pairs
  val mapped = inputrdd.mapValues(mark => (mark, 1));
  // converts it to ("maths",( 50,1)), ("maths", (60,1)), ("english", (65,1))

  //mapped: org.apache.spark.rdd.RDD[(String, (Int, Int))] = MapPartitionsRDD[30] at mapValues at :23
  // reduce method shuffles the data and creates data as maths 50, 60,  which become x,y for that key
  // just like mapValues key is not accessible here
  val reduced = mapped.reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))

  //reduced: org.apache.spark.rdd.RDD[(String, (Int, Int))] = ShuffledRDD[31] at reduceByKey at :25
  // a map is like an ETL mappings.
  val average = reduced.map { x =>
    val temp = x._2
    val total = temp._1
    val count = temp._2
    (x._1, total / count)
  }
  //average: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[32] at map at :27

  average.collect()
  //res30: Array[(String, Int)] = Array((english,65), (maths,55))


}
