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

package org.apache.spark.examples.sql

import java.util.Properties

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.sql.SparkSession

class SydFirst {

}


object SydFirst{
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Spark SQL data sources example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://172.16.31.91:3306")
      .option("dbtable", "hive231.TBLS")
      .option("user", "root")
      .option("password", "123456")
      .load()

    jdbcDF.show()

//    val connectionProperties = new Properties()
//    connectionProperties.put("user", "root")
//    connectionProperties.put("password", "123456")
//
//    val jdbcDF2 = spark.read
//      .jdbc("jdbc:mysql://172.16.31.91:3306", "hive231.TBLS", connectionProperties)
//
//    jdbcDF2.show()
//    // choose df Column and create Table Column Types if table not exists
//    jdbcDF2.select("OWNER", "TBL_NAME").write.mode("append")
//      .option("createTableColumnTypes", "OWNER CHAR(64), TBL_NAME VARCHAR(1024)")
//      .jdbc("jdbc:mysql://172.16.31.91:3306", "test.test2", connectionProperties)

//    val jdbcDF2 = spark.read
//      .format("jdbc")
//      .option("url", "jdbc:mysql://172.16.31.91:3306")
//      .option("dbtable", "hive231.VERSION")
//      .option("user", "root")
//      .option("password", "123456")
//      .load()

//    jdbcDF.show()
//    jdbcDF2.show()
    // join查询
//    val unionDF = jdbcDF.join(jdbcDF2).where("TBL_ID == VER_ID")
//
//    unionDF.write
//      .format("jdbc")
//      .option("url", "jdbc:mysql://172.16.31.91:3306")
//      .option("dbtable", "test.test")
//      .option("user", "root")
//      .option("password", "123456")
//      .save()





  }
}
