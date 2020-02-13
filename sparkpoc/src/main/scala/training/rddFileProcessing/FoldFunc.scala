package training.rddFileProcessing

import org.apache.spark.{SparkConf, SparkContext}

object FoldFunc {
  val conf = new SparkConf().setAppName("CachedPartition")
  val sc = new SparkContext(conf)

  val rdd1 = sc.parallelize(List(
     ("maths", 80), ("science", 90)))
//  rdd1: org.apache.spark.rdd.RDD[(String, Int)] = ParallelCollectionRDD[8] at parallelize at :21

  rdd1.partitions.length
 // res8: Int = 8

  val additionalMarks = ("extra", 4)
 // additionalMarks: (String, Int) = (extra,4)

  val sum = rdd1.fold(additionalMarks){ (acc, marks) =>
       val sum = acc._2 + marks._2
        ("total", sum)
     }
 // sum: (String, Int) = (total,206)


}
