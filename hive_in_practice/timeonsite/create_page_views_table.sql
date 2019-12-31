CREATE EXTERNAL TABLE page_views 
(
  logtime STRING, 
  userid INT, 
  ip STRING, 
  page STRING, 
  ref STRING, 
  os STRING, 
  os_ver STRING, 
  agent STRING
)
ROW FORMAT DELIMITED
  FIELDS TERMINATED BY '\t'
LOCATION 'hdfs://localhost/user/cloudera/hive_in_practice/timeonsite/data';

