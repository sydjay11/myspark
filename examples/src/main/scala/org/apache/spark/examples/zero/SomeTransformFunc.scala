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


object SomeTransformFunc {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("SomePartitionFunc")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    testBasicTransform(spark)
    spark.stop()
  }

  private def testBasicTransform(spark: SparkSession): Unit = {
    val rdd = spark.sparkContext.textFile("E:/spark-2.3.0/README.md", 3)
    val glomRdd = rdd.glom()
    // scalastyle:off println
    println(glomRdd)// glom() every partition put into an array
    // scalastyle:on println

    val rdd1 = spark.sparkContext.makeRDD(1 to 2, 1)
    val rdd2 = spark.sparkContext.makeRDD(2 to 3, 1)
    val unionRdd = rdd1.union(rdd2)
    // scalastyle:off println
    unionRdd.collect().foreach(x => println(x))// union do not remove repeat
    // scalastyle:on println

    val interRdd = rdd1.intersection(rdd2)
    // scalastyle:off println
    interRdd.collect().foreach(x => println(x))// intersection remove repeat
    // scalastyle:on println

    val subRdd = rdd1.subtract(rdd2)
    // scalastyle:off println
    subRdd.collect().foreach(x => println(x))// subtract only in rdd1 without rdd2,
    // not remove repeat
    // scalastyle:on println

    val mapPart = rdd1.mapPartitions(x => {  // param x is an iterator  return an iterator
      var result = List[Int]()
      var i = 0
      while(x.hasNext) {
        i += x.next()
      }
      result.::(i).iterator
    })
    // scalastyle:off println
    mapPart.collect().foreach(x => println(x))
    // scalastyle:on println

    val mapIndexPart = rdd1.mapPartitionsWithIndex((index, iter) => { // partition index
      var result = List[String]()
      var i = 0
      while(iter.hasNext) {
        i += iter.next()
      }
      result.::(index + "|" + i).iterator
    })
    // scalastyle:off println
    mapIndexPart.collect().foreach(x => println(x))
    // scalastyle:on println


    val zipRdd = rdd1.zip(rdd2)// result is RDD(k, v)
    // scalastyle:off println
    zipRdd.collect().foreach(x => println(x))
    // scalastyle:on println

  }

  private def kvTransform(spark: SparkSession): Unit = {

  }

}
