USE [master]
GO

/****** Object:  LinkedServer [mysqltest]    Script Date: 2018/6/19 11:02:24 ******/
EXEC master.dbo.sp_addlinkedserver @server = N'mysqltest', @srvproduct=N'MySql', @provider=N'MSDASQL', @datasrc=N'mysqltest'
 /* For security reasons the linked server remote logins password is changed with ######## */
EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname=N'mysqltest',@useself=N'False',@locallogin=NULL,@rmtuser=N'root',@rmtpassword='########'

GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'collation compatible', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'data access', @optvalue=N'true'
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'dist', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'pub', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'rpc', @optvalue=N'true'
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'rpc out', @optvalue=N'true'
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'sub', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'connect timeout', @optvalue=N'0'
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'collation name', @optvalue=null
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'lazy schema validation', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'query timeout', @optvalue=N'0'
GO

EXEC master.dbo.sp_serveroption @server=N'mysqltest', @optname=N'use remote collation', @optvalue=N'true'
GO


