package training.rddFileProcessing

import org.apache.spark.{SparkConf, SparkContext}
// spark shared variables are of two types,   broadcast and accoumlator
//An RDD Operation can be either be actions or transformations
//action returns result to the Driver Program or write it to the Storage.
// An action normally starts a Computation to provide result and always return some other
// data type other than RDD
//transformation returns Pointer to new RDD

package object BroadCastVariables {
  // Sending a value from Driver to Worker Nodes without
  // using Broadcast variable
  val conf = new SparkConf().setAppName("BroadCastVariables")
  val sc = new SparkContext(conf)

  //Parallelized collections are created by calling SparkContext’s parallelize method on an existing
  // collection in your driver program (a Scala Seq). The elements of the collection are copied to form a
  // distributed dataset that can be operated on in parallel. For example, here is how to create a
  // parallelized collection holding the numbers 1 to 5:

  val input = sc.parallelize(List(1, 2, 3))
  // input: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[17]
  // at parallelize at <console>:27
  val localVal = 2
  val addedlocal = input.map(x => x + localVal)
  // added: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[18] at map at <console>:31
  addedlocal.foreach(println)
  //   4
  //   3
  //   5
  //** Local variable is once again transferred to worked nodes
  //   for the next operation
  val multipliedlocal = input.map(x => x * 2)
  //multiplied: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[19] at map at <console>:29
  multipliedlocal.foreach(println)
  //     4
  //     6
  //     2
  // Sending a read-only value using Broadcast variable
  // Can be used to send large read-only values to all worker
  // nodes efficiently
  val broadcastVar = sc.broadcast(2)
  //broadcastVar: org.apache.spark.broadcast.Broadcast[Int] = Broadcast(14)

  val added = input.map(x => broadcastVar.value + x)
  //added: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[20] at map at <console>:31

  //added.foreach(println)
  // 5
  // 3
  //4
  val multiplied = input.map(x => broadcastVar.value * x)
  //multiplied: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[21] at map at <console>:31

  multiplied.foreach(println)
  //   6
  //   4
  //   2
// accumlator variable,  accumlates values from all executors sums and then displays the final result
  val accum = sc.accumulator(0, "My Accumulator")
  sc.parallelize(Array(1, 2, 3, 4)).foreach(x => accum += x)
  accum.value
}
