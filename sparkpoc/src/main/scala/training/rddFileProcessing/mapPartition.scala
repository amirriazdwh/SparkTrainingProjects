package training.rddFileProcessing

import org.apache.spark.{SparkConf, SparkContext}

object mapPartition {
  //If we call mapPartition(func) method on rdd, the func() operation will be called on each partition
  // instead of each row. In this particular case, it will be called 20 times(number of the partition).
  // In this way, you can prevent some processing when it comes to time-consuming application.
  val conf = new SparkConf().setAppName("mapPartition")
  val sc = new SparkContext(conf)

  val rdd1 = sc.parallelize(
    List(
      "yellow", "red",
      "blue", "cyan",
      "black"
    ),
    3
  )
 // function passed to mapPartition is f:Iterator => Iterator[U]
  mapped.mapPartitions(x=> for(i<-x) yield i.toUpperCase)
  // function passed to mapPartitionWinthIndex is f:(index,Interator )=>Iterator[U]
  mapped.mapPartitionsWithIndex((i,x)=> for(element <-x) yield (element,i))
  val mapped = rdd1.mapPartitionsWithIndex {
    // 'index' represents the Partition No
    // 'iterator' to iterate through all elements
    //                         in the partition
    (index, iterator) => {
      println("Called in Partition -> " + index)
      val myList = iterator.toList
      // In a normal user case, we will do the
      // the initialization(ex : initializing database)
      // before iterating through each element
      myList.map(x => x + " -> " + index).iterator
    }
  }
  mapped.collect()

  //sc.parallelize(1 to 9, 3).partitions.size
  //sc.parallelize(1 to 9, 3).mapPartitions(x=>(Array("Hello").iterator)).collect
  //scala> sc.parallelize(1 to 9, 3).mapPartitions(x=>(List(x.next).iterator)).collect

  // val mp = sc.parallelize(List("One","Two","Three","Four","Five","Six","Seven","Eight","Nine"), 3)
  // mp.mapPartitionsWithIndex((index, iterator) => {iterator.toList.map(x => x + "=>" + index ).iterator} ).collect





}
