/*
exec sp_addlinkedserver  
@server='mysqltest',--ODBC里面data source name  
@srvproduct='MySql',--自己随便  
@provider='MSDASQL',--固定这个  
@datasrc='mysqltest', ----ODBC里面data source name  
@location=NULL,  
--@provstr='DRIVER={MySQL ODBC 5.3 ANSI Driver};SERVER=172.17.29.33;DATABASE=bi;UID=zhaowenzhong;PORT=3306;', --和@datasrc属性二选一  
@catalog=NULL 


exec sp_addlinkedsrvlogin  
@rmtsrvname='mysqltest',----ODBC里面data source name  
@useself='false',  
@rmtuser='root',---mysql账号  
@rmtpassword='root';--mysql账号其密码  
*/
