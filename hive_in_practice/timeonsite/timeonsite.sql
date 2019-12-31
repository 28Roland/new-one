set mapred.reduce.tasks=2;

ADD FILE /home/cloudera/Desktop/hive_in_practice/timeonsite/timeonsite.py;

CREATE TABLE IF NOT EXISTS timeonsite (userid INT, timeonsite INT)
ROW FORMAT DELIMITED
  FIELDS TERMINATED BY ',';


INSERT OVERWRITE TABLE timeonsite
SELECT TRANSFORM (src.userid, src.logtime) USING './timeonsite.py' AS (userid, timeonsite)
FROM (
  SELECT userid, logtime
  FROM page_views
  DISTRIBUTE BY userid SORT BY userid, logtime
) src;
