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

import org.apache.spark.HashPartitioner
import org.apache.spark.sql.SparkSession


object SomePartitionFunc {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("SomePartitionFunc")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    testRepatition(spark)
    spark.stop()
  }

  private def testRepatition(spark: SparkSession): Unit = {
    val hdfsrdd = spark.sparkContext.textFile("E:/spark-2.3.0/README.md", 2)
    val rddPar1 = hdfsrdd.coalesce(1, true)
    val rddPar2 = hdfsrdd.repartition(2)
    val groupRdd = hdfsrdd.map(x => (x, x)).groupByKey(new org.apache.spark.HashPartitioner(4))
    // scalastyle:off println
    println(hdfsrdd.partitions.size)
    // scalastyle:on println

    // scalastyle:off println
    println(rddPar1.partitions.size)
    // scalastyle:on println

    // scalastyle:off println
    println(rddPar2.partitions.size)
    // scalastyle:on println

    // scalastyle:off println
    println(groupRdd.partitions.size)
    // scalastyle:on println

//    groupRdd.saveAsTextFile("D:/beh/groupRdd")

  }

}
