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
    println("\nMemory size: " + (Runtime.getRuntime.maxMemory / (1024 * 1024)) + " MB")
    println("\nClasspath contains testdir: " + System.getProperty("java.class.path").contains("testdir"))
    println("\nCurrent workdir contains testfile: " + testFileExists)
    println("===========================================")
  }

  def main(args: Array[String]) {
    println("spark.jars = " + System.getProperty("spark.jars"))
    val conf = new SparkConf().setAppName("TestApp")
    val sc = new SparkContext()
    test(sc)
  }
}
