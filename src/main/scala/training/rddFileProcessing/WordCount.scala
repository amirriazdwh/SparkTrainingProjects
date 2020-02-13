package training.rddFileProcessing

//RDDs transformations are broadly classified into two categories - Narrow & Wide transformation.
//In narrow transformation like map & filter, all the elements that are required to compute the records in single partition
// live in the single partition of parent RDD. A limited subset of partition is used to calculate the result.
//In wide transformation like groupByKey and reduceByKey, all the elements that are required to compute the records
// in the single partition may live in many partitions of parent RDD. The partition may live in many partitions of parent RDD.

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]) {

    println("In main : " + args(0) + "," + args(1))

    //Create Spark Context
    val conf = new SparkConf().setAppName("WordCountApp")
    val sc = new SparkContext(conf)

    //Load Data from File
    val input = sc.textFile(args(0))

    //Split into words, flatmap take one element and produces one or more elements in seq format
    // while map takes one element and output one element
    //===========
    // map
    //==========
    //val rdd = sc.parallelize(Seq("Where is Mount Everest","Himalayas India"))
    //rdd.map(x => x.split(" ")).collect
    //res21: Array[Array[String]] = Array(Array(Where, is, Mount, Everest), Array(Himalayas, India))
    //=============
    // flatmap
    //==============
    //rdd.flatMap(x => x.split(" ")).collect
    //res23: Array[String] = Array(Where, is, Mount, Everest, Himalayas, India)
    //rdd.filter(x=>x.contains("Himalayas")).collect
    //sc.parallelize(1 to 9, 3).map(x=>(x, "Hello")).collect
    //sc.parallelize(1 to 9, 3).partitions.size
    //================
    // mappartition
    //================
    //sc.parallelize(1 to 9, 3).mapPartitions(x=>(List(x.next,x.next, "|").iterator)).collect
    //sc.parallelize(1 to 9, 3).mapPartitions(x=>(List(x.next, x.hasNext).iterator)).collect
    //sc.parallelize(1 to 9, 3).mapPartitions(x=>(List(x.next, x.next, x.next, x.hasNext).iterator)).collect
    //=============
    // union
    //=============
    // val rdd1 = sc.parallelize(List("apple","orange","grapes","mango","orange"))
    ///val rdd2 = sc.parallelize(List("red","green","yellow"))
    //rdd1.union(rdd2).collect
    //-----------------
    //intersection
    //----------------
    //val rdd1 = sc.parallelize(-5 to 5)
    //val rdd2 = sc.parallelize(1 to 10)
    //rdd1.intersection(rdd2).collect
    //=============
    // distinct
    //=============
    //val rdd = sc.parallelize(List("apple","orange","grapes","mango","orange"))
    //rdd.distinct.collect
    //res121: Array[String] = Array(grapes, orange, apple, mango)

    val words = input.flatMap(line => line.split(" "))

    //Assign unit to each word
    val units = words.map(word => (word, 1))

    //Reduce each key
    val counts = units.reduceByKey((x, y) => x + y)

    //Write output to Disk
    counts.saveAsTextFile(args(1))

    //Shutdown spark. System.exit(0) or sys.exit() can also be used
    sc.stop();
  }
}