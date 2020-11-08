# SpringTcpsDatabaseConnection


sample project for spring boot to connects securly with two methods:

 1 - spring data jpa jdbc template
 2 - pure java jdbc connection
 
 sample url connection for tcps:
  
  jdbc:oracle:thin:@(description=(address=(protocol=tcps)(host=localhost)(port=2484))(connect_data=(service_name=orcl)))
  
 secure tcps must be enabled in database and both wallet and p12 files should be created. also some may need jks file. 
