package training.sqlFileProcessing

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object loadAvroFile {
  def main (args:Array[String]): Unit = {
    val config  = new SparkConf().setAppName("Load Avro Data")
    val sc = new SparkContext(config)
    val sqlContext = new SQLContext(sc)
    sqlContext.read.format("com.databricks.spark.avro")
                    .load("/user/hive/warehouse/userdata1.avro")
                    .registerTempTable("AVRO_DATA")
    val df = sqlContext.sql("select * from AVRO_DATA")
    df.show(10)

  }

}
