package training.sqlFileProcessing

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object loadJsonFile {
  def main(args: Array[String]): Unit = {
   /*
    if (args.length < 1) {
      println("Usage:  <host>")
      System.exit(1)
    }*/
    val conf = new SparkConf().setAppName("loadJson")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    sqlContext.read.json("/user/hive/warehouse/test").registerTempTable("First")
    val df = sqlContext.sql("select * from First")
    df.show()
    System.exit(0)

  }
}
