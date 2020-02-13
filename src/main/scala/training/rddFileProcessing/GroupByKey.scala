package training.rddFileProcessing

import org.apache.spark.{SparkConf, SparkContext}

// iternator is defined in scala as val v = Iterator(5, 1, 2, 3, 6, 4)
//checking for availability of next element
//while(v.hasNext)
//printing the element
//println(v.next)
//////////////////////////////////////////////////////
//object GFG
//{
//  // Main method
//  def main(args:Array[String])
//  {
//    val v = Array(5,1,2,3,6,4)
//    //val v = List(5,1,2,3,6,4)
//
//    // defining an iterator
//    // for a collection
//    val i = v.iterator
//
//    while (i.hasNext)
//      print(i.next + " ")
//  }
//}
//////////////////////////////////////////////////
//object GFG
//{
//  // Main method
//  def main(args:Array[String])
//  {
//    // defining iterator
//    val i = Iterator(5, 1, 2, 3, 6, 4)
//
//    /*
//    OR
//    val v = List(5, 1, 2, 3, 6, 4)
//    val i = v.iterator
//    */
//
//    // accessing elements using foreach
//    i foreach println
//    // same as i.foreach(println)
//  }
//}
////////////////////////////////////////////////////
//object GFG
//{
//  // Main method
//  def main(args:Array[String])
//  {
//    // defining iterator
//    val i = Iterator(5, 1, 2, 3, 6, 4)
//
//    /*
//    OR
//    val v = List(5, 1, 2, 3, 6, 4)
//    val i = v.iterator
//    */
//
//    // accessing elements using for loop
//    for(k <- i) println(k)
//  }
//}
////////////////////////////////////////////////////
object GroupByKey {
  val conf = new SparkConf().setAppName("GroupByKey")
  val sc = new SparkContext(conf)

  // Bazic groupByKey example in scala
  val x = sc.parallelize(Array(("USA", 1), ("USA", 2), ("India", 1),
    ("UK", 1), ("India", 4), ("India", 9),
    ("USA", 8), ("USA", 3), ("India", 4),
    ("UK", 6), ("UK", 9), ("UK", 5)), 3)
  // x: org.apache.spark.rdd.RDD[(String, Int)] =
  //  ParallelCollectionRDD[0] at parallelize at <console>:24
  //==============================================
  // groupByKey with default partitions
  //=========================================
  val y1 = x.groupByKey
  // y: org.apache.spark.rdd.RDD[(String, Iterable[Int])] =
  //  ShuffledRDD[1] at groupByKey at <console>:26

  // Check partitions
  y1.getNumPartitions
  // res0: Int = 3
  //=====================================
  // With Predefined Partitions
  //=================================
  val y2 = x.groupByKey(2)
  // y: org.apache.spark.rdd.RDD[(String, Iterable[Int])] =
  //  ShuffledRDD[3] at groupByKey at <console>:26

  y2.getNumPartitions
  // res1: Int = 2

  y2.collect
  // res2: Array[(String, Iterable[Int])] =
  //  Array((UK,CompactBuffer(1, 6, 9, 5)),
  //        (USA,CompactBuffer(1, 2, 8, 3)),
  //        (India,CompactBuffer(1, 4, 9, 4)))


}
