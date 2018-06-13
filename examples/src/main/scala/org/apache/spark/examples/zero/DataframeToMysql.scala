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

import java.util.Properties

import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object DataframeToMysql {

  def main(args: Array[String]): Unit = {
    val url = "jdbc:mysql://172.16.31.91:3306/test_dw"
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("DataframeToMysql")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    val schema = StructType(
      StructField("name", StringType) ::
        StructField("age", IntegerType)
        :: Nil)
    val connectionProperties = new Properties()
    connectionProperties.put("user", "root")
    connectionProperties.put("password", "123456")
    val data = spark.sparkContext.parallelize(List(("iteblog", 30),
      ("iteblog", 29),("com", 40), ("bt", 33), ("www", 23)))
      .map(item => Row.apply(item._1, item._2))
    val df = spark.createDataFrame(data, schema)

    try {
      df.write.mode(SaveMode.Append).jdbc(url, "dftomysql", connectionProperties)
    } catch {
      // scalastyle:off println
      case e: Exception => println(e)
      // scalastyle:on println
    }
    spark.sparkContext.stop
  }

}
