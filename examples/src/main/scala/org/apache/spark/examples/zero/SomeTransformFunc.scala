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


object SomeTransformFunc {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("SomePartitionFunc")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    kvTransform(spark)
//    testBasicTransform(spark)
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
    val rdd = spark.sparkContext.makeRDD(1 to 20, 2)
    val rdd1 = spark.sparkContext.makeRDD(Array((1, "A"), (2, "B"), (3, "C")))
    val rdd2 = spark.sparkContext.makeRDD(Array(("A", 1), ("A", 2), ("C", 3)))
    var transV = rdd1.map(x => (x._1, x._2 + "_"))
    // scalastyle:off println
    println("---------------this is a function of map----------------")
    transV.collect().foreach(x => println(x))
    // scalastyle:on println

    val combine = rdd2.combineByKey((v: Int) => v + "_",
      (c: String, v: Int) => c + "@" + v, (c1: String, c2: String) => c1 + "$" + c2)
    // scalastyle:off println
    println("---------------this is a function of combineByKey----------------")
    combine.collect().foreach(x => println(x))
    // scalastyle:on println

    val fold = rdd2.foldByKey(0)(_ + _)// fold
    // scalastyle:off println
    println("---------------this is a function of foldByKey----------------")
    fold.collect().foreach(x => println(x))
    // scalastyle:on println

    val group = rdd2.groupByKey()
    // scalastyle:off println
    println("---------------this is a function of groupByKey----------------")
    group.collect().foreach(x => println(x))
    // scalastyle:on println

    val reduce = rdd2.reduceByKey(new HashPartitioner(2), (x, y) => x + y)// x,y all is v
    // scalastyle:off println
    println("---------------this is a function of reduceByKey----------------")
    reduce.collect().foreach(x => println(x))
    // scalastyle:on println

    val map = rdd2.reduceByKeyLocally((x, y) => x + y)// result is a map
    // scalastyle:off println
    println("---------------this is a function of reduceByKeyLocally----------------")
    map.foreach(x => println(x))
    // scalastyle:on println

    val rdd3 = spark.sparkContext.makeRDD(Array(("A", 2), ("B", 5)))
    val cogroup = rdd2.cogroup(rdd3, rdd2) // RDD[K,V] use K make Equivalent connection
    // scalastyle:off println
    println("---------------this is a function of cogroup----------------")
    cogroup.foreach(x => println(x))
    // scalastyle:on println

    var join = rdd2.join(rdd3)// RDD[K,V] use K make Equivalent connection
    // scalastyle:off println
    println("---------------this is a function of join----------------")
    join.foreach(x => println(x))
    // scalastyle:on println

    val leftjoin = rdd2.leftOuterJoin(rdd3)// RDD[K,V] use K make Equivalent connection
    // scalastyle:off println
    println("---------------this is a function of leftOuterJoin----------------")
    leftjoin.foreach(x => println(x))
    // scalastyle:on println

  }

}
