/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.examples.zero

import org.apache.spark.sql.SparkSession


object SomeControlFunc {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("SomeControlFunc")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    persistenceRDD(spark)
    spark.stop()
  }

  private def persistenceRDD(spark: SparkSession): Unit = {
    val filePath = "hdfs://172.16.31.231:9000/sogou/first.filter"
    val sogou = spark.sparkContext
      .textFile(filePath)
    val split = sogou.map(x => x.split("\\s+")) // split with one or more " ", x result is list
//    split.cache()
    spark.sparkContext.setCheckpointDir("E:/beh/tmp/data/checkpoint/")
    split.checkpoint()
    // scalastyle:off println
//    split.foreach(x => println(x(1)))
    // scalastyle:on println
    // scalastyle:off println
//    println(s"split count is ${split.count()}")
    // scalastyle:on println
    // scalastyle:off println
        println(s"split head rdd is ${split.dependencies.head.rdd}")
    // scalastyle:on println


  }

}
