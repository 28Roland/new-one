CREATE EXTERNAL TABLE report_input (
  ad_id STRING,
  create_time TIMESTAMP,
  create_date STRING,
  create_hour STRING,
  log_type INT,
  imei STRING,
  mac_address STRING,
  idfa STRING,
  app_id STRING,
  ip_country STRING,
  ip_city STRING,
  ip_quadkey STRING
)
ROW FORMAT 
  DELIMITED FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION 'hdfs://localhost/user/cloudera/hive_in_practice/audi/report_input';
