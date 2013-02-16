--****************************************************************************
-- workflow流程实例信息表
--****************************************************************************
create table wf_process_def
(
	id	varchar(64) not null,              --流程定义id:由系统自生成
	wfdId varchar(64) not null,            --流程定义实际id
	wfdkey varchar(64) not null,           --流程定义key:对应activiti中的key
	version varchar(64) not null,          --流程版本号
	name varchar(64) not null,             --流程名
	category varchar(64) not null,         --流程类别
	state varchar(32) not null,            --流程状态:用以支持，测试态，运营态等流程状态
	serviceType varchar(64) not null,      --流程定义对应的业务类型
	primary key(id)
);
create unique index idx_wf_process_01 on wf_process_def(wfkey,version);
