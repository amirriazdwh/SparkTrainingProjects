package training.rddFileProcessing

import org.apache.spark.{SparkConf, SparkContext}


object ReduceByKey {

  object CollectExample {
    val conf = new SparkConf().setAppName("CollectExample")
    val sc = new SparkContext(conf)

    val inputrdd = sc.parallelize(Seq(("maths", 50), ("maths", 60), ("english", 65)))
    //inputrdd: org.apache.spark.rdd.RDD[(String, Int)] = ParallelCollectionRDD[29] at parallelize at :21

    //    When called on a dataset of (K, V) pairs, returns a dataset of (K, V) pairs where the
    //    values for each key are aggregated using the given reduce function func, which must
    //    be of type (V,V) => V. Like in groupByKey, the number of reduce tasks is configurable
    //    through an optional second argument.

    // reduce first group the data at partition or executor level like (maths,(50,60) and then
    // calculate applies calculate function.  then its broad cast the aggregated values and perform
    // calculation to give one values
    val mapped = inputrdd.reduceByKey((x, y) => x + y)
    val mapped2 = inputrdd.reduceByKey(_ + _)

    mapped.collect()
    //res30: Array[(String, Int)] = Array((english,65), (maths,55))

  }

}
