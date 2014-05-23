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

package main.scala

import scala.collection.mutable.{ListBuffer, Queue}

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import somelibrary.SomeLibrary

case class Person(name: String, age: Int)

object TestApp {

  def test(sc: SparkContext) {
    println("Test started")
    
    SomeLibrary.someDriverMethod()
    val rdd = sc.makeRDD(1 to 10, 10)
    val rdd2 = rdd.map(SomeLibrary.someExecutorMethod)
    val result = rdd2.collect()
    val testFileExists = rdd.map(x => {
      val d = new java.io.File("testfile").exists()
      println(d)
      d
    }).reduce(_ && _)

    println("\n\n===========================================")
    println("\nSecondary JAR found at driver: true") 
    println("\nSecondary JAR found at executors: " + (result.toSet == (1 to 10).map(_ * 2).toSet))
    println("\nJava options set: [ property = " + System.getProperty("property") + " ]")
    println("\nMemory size: " + (Runtime.getRuntime.totalMemory() / (1024 * 1024)) + " MB")
    println("\nClasspath contains testdir: " + System.getProperty("java.class.path").contains("testdir"))
    println("\nCurrent workdir contains testfile: " + testFileExists)
    println("===========================================")
  }

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("TestApp")
    val sc = new SparkContext()
    test(sc)
  }
}
