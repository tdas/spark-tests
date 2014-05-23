from pyspark import SparkContext
from secondary import someExecutorMethod
from secondary import someDriverMethod

import os.path

def test(sc):
  print "Test started"
  rdd = sc.parallelize(range(10), 10)
  rdd2 = rdd.map(lambda x: someExecutorMethod(x))
  result = rdd2.collect()
  testFileExists = rdd.map(lambda x: os.path.isfile("testfile")).reduce(lambda x, y: x and y)
  
  print "\n\n========================================"
  print "\nSecondary python file found at driver:", someDriverMethod() 
  print "\nSecondary python file found at executor:", result == map(lambda x: x * 2, range(10))
  print "\nCurrent workdir contains testfile:", testFileExists
  print "\n\n========================================"

if __name__ == "__main__":
  sc = SparkContext(appName="TestApp")
  test(sc)
