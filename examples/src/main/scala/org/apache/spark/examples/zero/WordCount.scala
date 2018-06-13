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
// rdd对象 split之后为元组

package org.apache.spark.examples.zero

import org.apache.spark.sql.SparkSession

object WordCount {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("wordCount")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val filePath = "E:/spark-2.3.0/README.md"
//    read file from hadoop
    val hdfsrdd = spark.sparkContext.textFile("hdfs://172.16.31.231:9000/input/file1.txt")
    hdfsrdd.foreach(print(_))
    val rdd = spark.sparkContext.textFile(filePath)
    val wordCount = rdd.flatMap(_.split(",")).map(x => (x, 1)).reduceByKey(_ + _)
    val wordSort = wordCount.map(x => (x._2, x._1)).sortByKey(false).map(x => (x._2, x._1))
    val wordSort1 = wordSort.repartition(4)
//    the func collect() is make rdd to array
    val collect = wordSort1.collect()
    print(s"patitions number is ${wordSort1.partitions.size}")
    print(s"patitions weizhi is ${wordSort.preferredLocations(wordSort.partitions(0))}")
    wordSort1.saveAsTextFile("D:/beh/wordSort1")
  }

}
