use DoyenSynEmm
go

if (object_id('syn_user', 'table') is not null)
DROP TABLE [dbo].[syn_user]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[syn_user](
	[id] [int] identity(1,1) not null primary key,
	[S_UserID] [varchar](250) NULL,
	[S_UserCode] [varchar](250) NULL,
	[S_UserName] [varchar](250) NULL,
	[S_UserIsUsed] [bit] NULL,
	[S_UserDeptID] [varchar](250) NULL,
	[S_UserPassWord] [varchar](250) NULL,
	[type] [varchar](250) NULL,
	[status] [int] NULL,
	[create_time] [datetime] NULL,
	[syn_time] [datetime] NULL
) 
GO
SET ANSI_PADDING OFF
GO
/* �û�������*/
if (object_id('tr_sysUser_insert', 'tr') is not null)
drop trigger tr_sysUser_insert
go

Create Trigger tr_sysUser_insert  
On s_sysUser       
for  insert          
As                 
begin   
insert into syn_user(S_UserID,S_UserCode,S_UserName,S_UserIsUsed,S_UserDeptID,S_UserPassWord,type,status,create_time) 
select S_UserID,S_UserCode,S_UserName,CASE S_UserIsUsed when 1 then 0 when 0 then 1 ELSE 0 END as S_UserIsUsed,S_UserDeptID,S_UserPassWord,'insert',0,GETDATE() from Inserted
end
go


if (object_id('tr_sysUser_update', 'tr') is not null)
drop trigger tr_sysUser_update
go

Create Trigger tr_sysUser_update  
On s_sysUser       
for  update      
As                 
begin   
insert into syn_user(S_UserID,S_UserCode,S_UserName,S_UserIsUsed,S_UserDeptID,S_UserPassWord,type,status,create_time) 
select S_UserID,S_UserCode,S_UserName,CASE S_UserIsUsed when 1 then 0 when 0 then 1 ELSE 0 END as S_UserIsUsed,S_UserDeptID,S_UserPassWord,'update',0,GETDATE()from Inserted
end
go

if (object_id('tr_sysUser_delete', 'tr') is not null)
drop trigger tr_sysUser_delete
go

Create Trigger tr_sysUser_delete  
On s_sysUser       
for  delete           
As                
begin   
insert into syn_user(S_UserID,type,status,create_time) 
select S_UserID,'delete',0,GETDATE() from deleted  
end
go

if (object_id('syn_project', 'table') is not null)
DROP TABLE [dbo].[syn_project]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[syn_project](
	[id] [int] identity(1,1) not null primary key,
	[Pdp_ID] [varchar](256) NULL,
	[Pdp_Name] [varchar](256) NULL,
	[Pdp_Code] [varchar](256) NULL,
	[Pdp_Custom] [varchar](256) NULL,
	[Pdp_Manager] [varchar](256) NULL,
	[Prj_MainDept] [varchar](256) NULL,
	[type] [varchar](256) NULL,
	[status] [int] NULL,
	[create_time] [datetime] NULL,
	[syn_time] [datetime] NULL
) 
GO
SET ANSI_PADDING OFF
GO

if (object_id('tr_DesignProject_insert', 'tr') is not null)
drop trigger tr_DesignProject_insert
go

Create Trigger tr_DesignProject_insert  
On Pm_DesignProject       
for  insert          
As               
begin   
insert into syn_project(Pdp_ID,Pdp_Name,Pdp_Code,Pdp_Custom,Pdp_Manager,Prj_MainDept,type,status,create_time) 
select Pdp_ID,Pdp_Name,Pdp_Code,Pdp_Custom,Pdp_Manager,Prj_MainDept,'insert',0,GETDATE() from Inserted   
end
go

if (object_id('tr_DesignProject_update', 'tr') is not null)
drop trigger  tr_DesignProject_update
go

Create Trigger  tr_DesignProject_update
On Pm_DesignProject        
for  update    
As                
begin   
insert into syn_project(Pdp_ID,Pdp_Name,Pdp_Code,Pdp_Custom,Pdp_Manager,Prj_MainDept,type,status,create_time) 
select Pdp_ID,Pdp_Name,Pdp_Code,Pdp_Custom,Pdp_Manager,Prj_MainDept,'update',0,GETDATE() from Inserted
end
go


if (object_id('tr_DesignProject_delete', 'tr') is not null)
drop trigger tr_DesignProject_delete
go

Create Trigger tr_DesignProject_delete 
On Pm_DesignProject        
for  delete     
As                
begin   
insert into syn_project(Pdp_ID,type,status,create_time) 
select Pdp_ID,'delete',0,GETDATE() from Deleted
end
go

IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[proc_syn_user]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
DROP PROCEDURE [dbo].[proc_syn_user]
GO
create proc proc_syn_user
as 
begin
    declare @id varchar(50),@userId varchar(50),@userCode varchar(50),@userName varchar(50),
             @userDeptId varchar(50),@type varchar(50),@password varchar(50)
    declare @userIsUsed  int,@status int
  
 
    declare user_cursor cursor 
    for (select id,S_UserID,S_UserCode,S_UserName,S_UserIsUsed,S_UserDeptID,S_UserPassWord,type,status from syn_user where status=0 order by create_time )
    open user_cursor
    fetch next from user_cursor into  @id,@userId,@userCode,@userName,@userIsUsed,@userDeptId,@password,@type,@status
	while @@FETCH_STATUS = 0   
        begin            
                 SET NOCOUNT ON  
				    if @type='insert'
						Begin
							INSERT into OPENQUERY(mysqltest,'select * from sys_user')(id,office_id,login_name,name,password,del_flag,create_by,create_date,update_by,update_date) 
							 values (@userId,@userDeptId,@userCode,@userName,'1',@userIsUsed,'1',GETDATE(),'1',GETDATE());  			
						     update syn_user set status=1 ,syn_time=GETDATE() where id=@id;
						End
                    if @type='update'
						Begin
						   UPDATE OPENQUERY(mysqltest,'select * from sys_user') SET office_id=@userDeptId,login_name=@userCode,name=@userName,password=@password,del_flag=@userIsUsed,update_by='1',update_date=GETDATE()  WHERE id=@userId			
						   update syn_user set status=1 ,syn_time=GETDATE() where id=@id;
						End	
					if @type='delete'
						Begin
						 DELETE OPENQUERY(mysqltest,'select * from sys_user') WHERE id=@userId;
						 update syn_user set status=1 ,syn_time=GETDATE() where id=@id;		
						End
                             
                 SET NOCOUNT OFF  
                fetch next from user_cursor into  @id,@userId,@userCode,@userName,@userIsUsed,@userDeptId,@password,@type,@status   
        end    
    close user_cursor  
    deallocate user_cursor 
end
go
/*��Ŀͬ���洢����*/
IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[proc_syn_project]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
DROP PROCEDURE [dbo].[proc_syn_project]
GO
create proc proc_syn_project
as 
begin
    declare @id varchar(256),@pdpId varchar(256),@code varchar(256),@name varchar(256),
             @custom varchar(256),@manager varchar(256),@dept varchar(256),@type varchar(256)
    declare  @status int
    declare prj_cursor cursor 
    for (SELECT id,Pdp_ID,Pdp_Name,Pdp_Code,Pdp_Custom ,Pdp_Manager,Prj_MainDept,status,type FROM dbo.syn_project where status=0 order by create_time)
    open prj_cursor
    fetch next from prj_cursor into  @id,@pdpId,@code,@name,@custom,@manager,@dept,@status,@type
	while @@FETCH_STATUS = 0    
        begin            
                 SET NOCOUNT ON  
				    if @type='insert'
						Begin
							INSERT into OPENQUERY(mysqltest,'select * from bm_project')(id,code,name,custom,manager,office_id) 
							 values (@pdpId,@code,@name,@custom,@manager,@dept);  			
						     update syn_project set status=1 ,syn_time=GETDATE() where id=@id;
						End
                    if @type='update'
						Begin
						   UPDATE OPENQUERY(mysqltest,'select * from bm_project') SET code=@code,name=@name,custom=@custom,manager=@manager,office_id=@dept WHERE id=@pdpId			
						   update syn_project set status=1 ,syn_time=GETDATE() where id=@id;
						End	
					if @type='delete'
						Begin
						 DELETE OPENQUERY(mysqltest,'select * from bm_project') WHERE id=@pdpId;
						 update syn_project set status=1 ,syn_time=GETDATE() where id=@id;		
						End
                             
                 SET NOCOUNT OFF  
                fetch next from prj_cursor into   @id,@pdpId,@code,@name,@custom,@manager,@dept,@status,@type   --?????????|???��??????
        end    
    close prj_cursor 
    deallocate prj_cursor  
end
go

IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[proc_syn]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
DROP PROCEDURE [dbo].[proc_syn]
GO
create proc proc_syn
as 
begin
   exec proc_syn_user;
   exec proc_syn_project;
end
go

