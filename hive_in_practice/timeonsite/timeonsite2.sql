set mapred.reduce.tasks=2;

SELECT userid, MAX(ts)-MIN(ts) 
FROM 
(
  SELECT userid, unix_timestamp(logtime,'MM/dd/yyyy HH:mm:ss') ts 
  FROM page_views
)src 
GROUP BY userid;

