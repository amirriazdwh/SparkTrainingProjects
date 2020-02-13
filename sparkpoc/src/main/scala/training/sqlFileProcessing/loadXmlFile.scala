package training.sqlFileProcessing

import org.apache.spark._
import org.apache.spark.sql._

object loadXmlFile {

  def main (args:Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Load Xml File")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    sqlContext.read.format("com.databricks.spark.xml")
                    .option("rootTag", "books")
                    .option("rowTag", "book")
                    .load("/user/hive/warehouse/books.xml")
                    .registerTempTable("XML_DATA")

    val df = sqlContext.sql("select * from XML_DATA")
    df.show(400)

  }
}
