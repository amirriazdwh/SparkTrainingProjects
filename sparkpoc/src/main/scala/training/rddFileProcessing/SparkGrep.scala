package training.rddFileProcessing

import org.apache.spark.{SparkConf, SparkContext}

//Spark can create distributed datasets from any storage source supported by Hadoop, including your local
// file system, HDFS, Cassandra, HBase, Amazon S3, etc. Spark supports text files, SequenceFiles, and
// any other Hadoop InputFormat.

//Text file RDDs can be created using SparkContext’s textFile method. This method takes an URI for the file
// (either a local path on the machine, or a hdfs://, s3n://, etc URI) and reads it as a collection of lines.
// Here is an example invocation:
// hdfs// is protocal and /user/data.txt is the path so the whole path is hdfs///user/data.txt
object SparkGrep {
	def main(args: Array[String]) {
		if (args.length < 3) {
			System.err.println("Usage: SparkGrep <host> <input_file> <match_term>")
			System.exit(1)
		}
		/*Error: a secret key must be specified via the spark.authenticate.secret config
It can be confusing when authentication is turned on by default in a cluster, and one
tries to start spark in local mode for a simple test.
*Workaround*: If {{spark.authenticate=true}} is specified as a cluster wide config, then
the following has to be added
if you want to run it on cluster without local mode remove setMaster
		* */
		val conf = new SparkConf().setAppName("SparkGrep").setMaster(args(0))
		val sc = new SparkContext(conf)
		val inputFile = sc.textFile(args(1), 2).cache()
		val matchTerm : String = args(2)
		val numMatches = inputFile.filter(line => line.contains(matchTerm)).count()
		println("%s lines in %s contain %s".format(numMatches, args(1), matchTerm))
		System.exit(0)
	}

}

