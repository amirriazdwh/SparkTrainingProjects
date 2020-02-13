package pl.jaceklaskowski.spark

import org.apache.spark.scheduler.{SparkListenerStageCompleted, SparkListener, SparkListenerJobStart}

class CustomSparkListener extends SparkListener {
  override def onJobStart(jobStart: SparkListenerJobStart) {
    super.onJobStart(jobStart)
    println(s"Job started with ${jobStart.stageInfos.size} stages: $jobStart")
  }

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = {
    super.onStageCompleted(stageCompleted)
    println(s"Stage ${stageCompleted.stageInfo.stageId} completed with ${stageCompleted.stageInfo.numTasks} tasks.")
  }
}
