package training.rddFileProcessing

import org.apache.spark.{SparkConf, SparkContext}

package object ActionMis {
  val conf = new SparkConf().setAppName("ActionMis")
  val sc = new SparkContext(conf)

  val inputRDD = sc.parallelize(1 to 10)
  //inputRDD: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[9] at parallelize at :21

  val filteredRDD = inputRDD.filter(x => x != 5)
  //filteredRDD: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[10] at filter at :23

  // Collect (Action) - Return all the elements of the dataset as an array at the driver program.
  // This is usually useful after a filter or other operation that returns a sufficiently small subset of the data.
  filteredRDD.collect()

  /// res15: Array[Int] = Array(1, 2, 3, 4, 6, 7, 8, 9, 10)
  //  select(*cols) (transformation) - Projects a set of expressions and returns a new DataFrame.
  //
  //  Parameters: cols – list of column names (string) or expressions (Column). If one of the column names is ‘*’, that column is expanded to include all columns in the current DataFrame.**
  //
  //  df.select('*').collect()
  //    [Row(age=2, name=u'Alice'), Row(age=5, name=u'Bob')]
  //  df.select('name', 'age').collect()
  //    [Row(name=u'Alice', age=2), Row(name=u'Bob', age=5)]
  //  df.select(df.name, (df.age + 10).alias('age')).collect()
  //    [Row(name=u'Alice', age=12), Row(name=u'Bob', age=15)]

  filteredRDD.take(2)
  //res16: Array[Int] = Array(1, 2)
  filteredRDD.saveAsTextFile("./result")

}
