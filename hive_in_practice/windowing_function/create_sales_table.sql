create table sales
(
  id int,
  item string,
  day date,
  amount float
)
row format delimited fields terminated by ','
stored as textfile
location 'hdfs://localhost/user/cloudera/hive_in_practice/windowing_function/data';
