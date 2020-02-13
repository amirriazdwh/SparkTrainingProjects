package training.rddFileProcessing

//Similar to combiner in MapReduce, when working with key/value pairs, combineByKey() interface can be used to
// customize the combiner functionality. Methods like reduceByKey() by default use their own combiner to combine the
// data locally in each Partition, for a given key

//We are able to see how our action(count) run is bifurcated into sub components (Job -> Stages -> Tasks).
// So any action is converted into Job which in turn is again divided into Stages, with each stage having its
// own set of Tasks.




import org.apache.spark.{SparkConf, SparkContext}

package object CombineByKey {

  val conf = new SparkConf().setAppName("CombineByKey")
  val sc = new SparkContext(conf)

  val inputrdd = sc.parallelize(Seq(
    ("maths", 50), ("maths", 60),
    ("english", 65),
    ("physics", 66), ("physics", 61), ("physics", 87)),
    1)
  //inputrdd: org.apache.spark.rdd.RDD[(String, Int)] = ParallelCollectionRDD[41] at parallelize at <console>:27

  inputrdd.getNumPartitions
  //res55: Int = 1

  val reduced = inputrdd.combineByKey(
    (mark) => { println(s"Create combiner -> ${mark}")
      (mark, 1)
    },
    (acc: (Int, Int), v) => {
      println(s"""Merge value : (${acc._1} + ${v}, ${acc._2} + 1)""")
      (acc._1 + v, acc._2 + 1)
    },
    (acc1: (Int, Int), acc2: (Int, Int)) => {
      println(s"""Merge Combiner : (${acc1._1} + ${acc2._1}, ${acc1._2} + ${acc2._2})""")
      (acc1._1 + acc2._1, acc1._2 + acc2._2)
    }
  )
  //   reduced: org.apache.spark.rdd.RDD[(String, (Int, Int))] = ShuffledRDD[42] at combineByKey at <console>:29

  reduced.collect()
  //      Create combiner -> 50
  //      Merge value : (50 + 60, 1 + 1)
  //      Create combiner -> 65
  //      Create combiner -> 66
  //      Merge value : (66 + 61, 1 + 1)
  //      Merge value : (127 + 87, 2 + 1)
  //      res56: Array[(String, (Int, Int))] = Array((maths,(110,2)), (physics,(214,3)), (english,(65,1)))

  val result = reduced.mapValues(x => x._1 / x._2.toFloat)
  //result: org.apache.spark.rdd.RDD[(String, Float)] = MapPartitionsRDD[43] at mapValues at <console>:31

  result.collect()
  ///   res57: Array[(String, Float)] = Array((maths,55.0), (physics,71.333336), (english,65.0))
}
