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


object SomeActionFunc {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("SomeControlFunc")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    action(spark)
    spark.stop()
  }
  private def action(spark: SparkSession): Unit = {
    var rdd1 = spark.sparkContext.makeRDD(Array(("A", 1), ("B", 2), ("C", 3)), 2)
    // scalastyle:off println
    println(s"first rdd element is ${rdd1.first()}")
    // scalastyle:on println
    // scalastyle:off println
    println(s"rdd1's elements count is ${rdd1.count()}")
    // scalastyle:on println
    // scalastyle:off println
    println(s"rdd1's reduce result is ${rdd1.reduce((x, y) => (x._1 + y._1, x._2 + y._2))}")
    // scalastyle:on println
    // scalastyle:off println
    println(s"rdd1's elements take is ${rdd1.take(1)}")
    // scalastyle:on println
    // scalastyle:off println
    println(s"rdd1's elements top is ${rdd1.top(1).apply(0)}")
    // scalastyle:on println
    var rdd2 = spark.sparkContext.makeRDD(1 to 10, 2)
    var result = rdd2.aggregate(1)({(x: Int, y: Int) => x + y}, {(a: Int, b: Int) => a + b})
    // scalastyle:off println
    println(s"the result is $result")
    // scalastyle:on println
    var fold = rdd2.fold(1)((x, y) => x + y)
    // scalastyle:off println
    println(s"the fold is $fold")
    // scalastyle:on println

  }

}
