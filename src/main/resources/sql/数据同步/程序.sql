/*
-- =============================================  
-- Author:      �޼���  
-- Create date:2018-6-19  
-- Description: �����û�ͬ����
-- =============================================  

if (object_id('tr_sysUser_insert', 'tr') is not null)
drop trigger tr_sysUser_insert
go

Create Trigger tr_sysUser_insert  
On s_sysUser       
for  insert     --���봥��       
As                --�¼���������Ҫ��������      
begin   
insert into syn_user(S_UserID,S_UserCode,S_UserName,S_UserIsUsed,S_UserDeptID,S_UserPassWord,type,status) 
select S_UserID,S_UserCode,S_UserName,S_UserIsUsed,S_UserDeptID,S_UserPassWord,'insert',0 from Inserted--��ʱ��    
end
go
-- =============================================  
-- Author:      �޼���  
-- Create date:2018-6-19  
-- Description: �����û�ͬ����
-- =============================================  

if (object_id('tr_sysUser_update', 'tr') is not null)
drop trigger tr_sysUser_update
go

Create Trigger tr_sysUser_update  
On s_sysUser       
for  update     --���봥��       
As                --�¼���������Ҫ��������      
begin   
insert into syn_user(S_UserID,S_UserCode,S_UserName,S_UserIsUsed,S_UserDeptID,S_UserPassWord,type,status) 
select S_UserID,S_UserCode,S_UserName,S_UserIsUsed,S_UserDeptID,S_UserPassWord,'update',0 from Inserted--��ʱ��    
end
go
-- =============================================  
-- Author:      �޼���  
-- Create date:2018-6-19  
-- Description: ɾ���û�ͬ����
-- =============================================  

if (object_id('tr_sysUser_delete', 'tr') is not null)
drop trigger tr_sysUser_delete
go

Create Trigger tr_sysUser_delete  
On s_sysUser       
for  delete     --���봥��       
As                --�¼���������Ҫ��������      
begin   
insert into syn_user(S_UserID,type,status) 
select S_UserID,'delete',0 from deleted--��ʱ��    
end
go

-- =============================================  
-- Author:      �޼���  
-- Create date:2018-6-19  
-- Description: �����û�ͬ����
-- =============================================  

if (object_id('tr_DesignProject_insert', 'tr') is not null)
drop trigger tr_DesignProject_insert
go

Create Trigger tr_DesignProject_insert  
On Pm_DesignProject       
for  insert     --���봥��       
As                --�¼���������Ҫ��������      
begin   
insert into syn_project(Pdp_ID,Pdp_Name,Pdp_Code,Pdp_Custom,Pdp_Manager,Prj_MainDept,type,status) 
select Pdp_ID,Pdp_Name,Pdp_Code,Pdp_Custom,Pdp_Manager,Prj_MainDept,'insert',0 from Inserted--��ʱ��    
end
go
-- =============================================  
-- Author:      �޼���  
-- Create date:2018-6-19  
-- Description: �����û�ͬ����
-- =============================================  

if (object_id('tr_DesignProject_update', 'tr') is not null)
drop trigger  tr_DesignProject_update
go

Create Trigger  tr_DesignProject_update
On Pm_DesignProject        
for  update     --���봥��       
As                --�¼���������Ҫ��������      
begin   
insert into syn_project(Pdp_ID,Pdp_Name,Pdp_Code,Pdp_Custom,Pdp_Manager,Prj_MainDept,type,status) 
select Pdp_ID,Pdp_Name,Pdp_Code,Pdp_Custom,Pdp_Manager,Prj_MainDept,'update',0 from Inserted--��ʱ��    
end
go
-- =============================================  
-- Author:      �޼���  
-- Create date:2018-6-19  
-- Description: ɾ���û�ͬ����
-- =============================================  

if (object_id('tr_DesignProject_delete', 'tr') is not null)
drop trigger tr_DesignProject_delete
go

Create Trigger tr_DesignProject_delete 
On Pm_DesignProject        
for  delete     --���봥��       
As                --�¼���������Ҫ��������      
begin   
insert into syn_project(Pdp_ID,type,status) 
select Pdp_ID,'delete',0 from Deleted--��ʱ�� 
end
go

IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[proc_syn_user]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)

DROP PROCEDURE [dbo].[proc_syn_user]
GO
create proc proc_syn_user
as 
begin
    --��Ŀ
    declare @id varchar(50),@userId varchar(50),@userCode varchar(50),@userName varchar(50),
             @userDeptId varchar(50),@type varchar(50),@password varchar(50)
    declare @userIsUsed  int,@status int
  
    --�����α�ΪUid
    declare user_cursor cursor 
    for (select id,S_UserID,S_UserCode,S_UserName,S_UserIsUsed,S_UserDeptID,S_UserPassWord,type,status from syn_user where status=0 )
    --���α�--
    open user_cursor
    --��ʼѭ���α����--
    fetch next from user_cursor into  @id,@userId,@userCode,@userName,@userIsUsed,@userDeptId,@password,@type,@status
	while @@FETCH_STATUS = 0    --���ر� FETCH���ִ�е�����α��״̬--
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
                fetch next from user_cursor into  @id,@userId,@userCode,@userName,@userIsUsed,@userDeptId,@password,@type,@status   --ת����һ���α꣬û�л���ѭ��
        end    
    close user_cursor  --�ر��α�
    deallocate user_cursor   --�ͷ��α�
end
go

IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[proc_syn_project]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)

DROP PROCEDURE [dbo].[proc_syn_project]
GO
create proc proc_syn_project
as 
begin
    --��Ŀ
    declare @id varchar(256),@pdpId varchar(256),@code varchar(256),@name varchar(256),
             @custom varchar(256),@manager varchar(256),@dept varchar(256),@type varchar(256)
    declare  @status int
  
    --�����α�ΪUid
    declare prj_cursor cursor 
    for (SELECT id,Pdp_ID,Pdp_Name,Pdp_Code,Pdp_Custom ,Pdp_Manager,Prj_MainDept,status,type FROM dbo.syn_project where status=0 )
    --���α�--
    open prj_cursor
    --��ʼѭ���α����--
    fetch next from prj_cursor into  @id,@pdpId,@code,@name,@custom,@manager,@dept,@status,@type
	while @@FETCH_STATUS = 0    --���ر� FETCH���ִ�е�����α��״̬--
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
                fetch next from prj_cursor into   @id,@pdpId,@code,@name,@custom,@manager,@dept,@status,@type   --ת����һ���α꣬û�л���ѭ��
        end    
    close prj_cursor  --�ر��α�
    deallocate prj_cursor   --�ͷ��α�
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
*/


 
          
 
          


 
          
 
          

 
          
 
          


 
          
 
          