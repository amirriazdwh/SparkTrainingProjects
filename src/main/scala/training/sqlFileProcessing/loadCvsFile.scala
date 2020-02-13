package training.sqlFileProcessing
//SPARK_CLASSPATH=postgresql-9.3-1102-jdbc41.jar bin/spark-shell
import org.apache.spark._
import org.apache.spark.sql._
import com.databricks.spark._

object loadCvsFile {
  // method calls are case sentives
def main(args:Array[String] ){
  // sparkconfig is just an object which contains the spark configuration parameters.
  val conf = new SparkConf().setAppName("Load Cvs File")
  // spark context is an object which creates spark scheduler, task, listners and executors.
  // InputStreamReader read data from HDFS file system and compress it and copy it to cache.
  // in case of the sqlcontext inputerstreamreader is used in DataSource object
   val sc = new SparkContext(conf)
  // sql context create parser object,  which parse sql statement
  // execute sql statements first parse,  then send it to catalist for optimation and then cache the execution plan
  // sql context create data frame.  a data frame is just a table in memory.  when hive tables are read their memory
  //  version is created as data frame.
  // a data frame contains row object and data types.  it help read data from cache
  val sqlContext = new SQLContext(sc)
  // load the cvs file
  // DataFrameReader reads sources using DataSource
  // DataSource is source dependent and will be part of com.databrick.spark.csv class
  // its get data, create schema from com.databrick.spark.csv class.
  // Spark SQL supports operating on a variety of data sources through the DataFrame interface
  //
  // once sqlcontext is created parser, optimizer are created and executor are connected
  // through sparkcontext.   now we have to read the file.
  //for that DataFrameReader is being created with read function and datasource is passed through
  // DataFrameReader contains all data loading functions.
  // Try(loader.loadClass(provider)).orElse(Try(loader.loadClass(provider2))) match {
  sqlContext.read.format("com.databricks.spark.csv")
            .option("header", "true")
            .option("inferSchema", "true")
            // load function returns a data frame since data is loaded and now operation
            // needs to be performed. it contains all sum, join etc functions
            .load("/user/hive/warehouse/newcars.csv")
            // registers dataframe with New_CARS name, that datafram is created by load function
            .registerTempTable("NEW_CARS")
  // generic load function
  // val df = sqlContext.read.load("examples/src/main/resources/users.parquet")
  // sql function returns dataframe, it parses the sql text
  val df= sqlContext.sql("select * from NEW_CARS")
  df.show
  // in case we dont registerTempTable the load function will return dataframe object and
  // we can use different methods in it for transformations
  // writing the databack to data we use DataFrameWriter which is bieng created from DataFrame
  //peopleDF.select("category_id", "category_name").write.format("parquet").save("cidAndcname.parquet")

}
}

