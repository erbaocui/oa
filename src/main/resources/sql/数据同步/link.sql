/*
exec sp_addlinkedserver  
@server='mysqltest',--ODBC����data source name  
@srvproduct='MySql',--�Լ����  
@provider='MSDASQL',--�̶����  
@datasrc='mysqltest', ----ODBC����data source name  
@location=NULL,  
--@provstr='DRIVER={MySQL ODBC 5.3 ANSI Driver};SERVER=172.17.29.33;DATABASE=bi;UID=zhaowenzhong;PORT=3306;', --��@datasrc���Զ�ѡһ  
@catalog=NULL 


exec sp_addlinkedsrvlogin  
@rmtsrvname='mysqltest',----ODBC����data source name  
@useself='false',  
@rmtuser='root',---mysql�˺�  
@rmtpassword='root';--mysql�˺�������  
*/
