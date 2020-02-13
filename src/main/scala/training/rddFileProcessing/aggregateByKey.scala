package training.rddFileProcessing

//aggregateByKey function in Spark accepts total 3 parameters,
//Initial value or Zero value
//It can be 0 if aggregation is type of sum of all values
//We have have this value as Double.MaxValue if aggregation objective is to find minimum value
//We can also use Double.MinValue value if aggregation objective is to find maximum value
//Or we can also have an empty List or Map object, if we just want a respective collection as an output for each key
//Sequence operation function which transforms/merges data of one type [V] to another type [U]
//Combination operation function which merges multiple transformed type [U] to a single type [U]

import org.apache.spark.{SparkConf, SparkContext}

object aggregateByKey {
  val conf = new SparkConf().setAppName("CollectExample")
  val sc = new SparkContext(conf)

  // Bazic aggregateByKey example in scala
  // Creating PairRDD studentRDD with key value pairs
  val studentRDD = sc.parallelize(Array(
    ("Joseph", "Maths", 83), ("Joseph", "Physics", 74), ("Joseph", "Chemistry", 91), ("Joseph", "Biology", 82),
    ("Jimmy", "Maths", 69), ("Jimmy", "Physics", 62), ("Jimmy", "Chemistry", 97), ("Jimmy", "Biology", 80),
    ("Tina", "Maths", 78), ("Tina", "Physics", 73), ("Tina", "Chemistry", 68), ("Tina", "Biology", 87),
    ("Thomas", "Maths", 87), ("Thomas", "Physics", 93), ("Thomas", "Chemistry", 91), ("Thomas", "Biology", 74),
    ("Cory", "Maths", 56), ("Cory", "Physics", 65), ("Cory", "Chemistry", 71), ("Cory", "Biology", 68),
    ("Jackeline", "Maths", 86), ("Jackeline", "Physics", 62), ("Jackeline", "Chemistry", 75), ("Jackeline", "Biology", 83),
    ("Juan", "Maths", 63), ("Juan", "Physics", 69), ("Juan", "Chemistry", 64), ("Juan", "Biology", 60)), 3)
  //Zero Value: Zero value in our case will be 0 as we are finding Maximum Marks
  val zeroVal1 = 0
  val aggrRDD = studentRDD.map(t => (t._1, (t._2, t._3))).aggregateByKey(zeroVal1)(seqOp1, combOp)
  //Zero Value: Zero value in our case will be tuple with blank subject name and 0
  //val zeroVal = ("", 0)
  //val aggrRDD1 = studentRDD.map(t => (t._1, (t._2, t._3))).aggregateByKey(zeroVal)(seqOp1, combOp1)

  //Check the Outout
  aggrRDD.collect foreach println

  // Output
  // (Tina,87)
  // (Thomas,93)
  // (Jackeline,83)
  // (Joseph,91)
  // (Juan,69)
  // (Jimmy,97)
  // (Cory,71)

  ///////////////////////////////////////////////////////
  // Let's Print Subject name along with Maximum Marks //
  ///////////////////////////////////////////////////////

  //Defining Seqencial Operation and Combiner Operations
  //Sequence operation : Finding Maximum Marks from a single partition
  def seqOp1 = (accumulator: Int, element: (String, Int)) =>
    if (accumulator > element._2) accumulator else element._2

  //Combiner Operation : Finding Maximum Marks out Partition-Wise Accumulators
  def combOp = (accumulator1: Int, accumulator2: Int) =>
    if (accumulator1 > accumulator2) accumulator1 else accumulator2

  //Defining Seqencial Operation and Combiner Operations
  def seqOp = (accumulator: (String, Int), element: (String, Int)) =>
    if (accumulator._2 > element._2) accumulator else element

  def combOp1 = (accumulator1: (String, Int), accumulator2: (String, Int)) =>
    if (accumulator1._2 > accumulator2._2) accumulator1 else accumulator2

  //Check the Outout
  aggrRDD.collect foreach println

  // Check the Output
  // (Tina,(Biology,87))
  // (Thomas,(Physics,93))
  // (Jackeline,(Biology,83))
  // (Joseph,(Chemistry,91))
  // (Juan,(Physics,69))
  // (Jimmy,(Chemistry,97))
  // (Cory,(Chemistry,71))
}
