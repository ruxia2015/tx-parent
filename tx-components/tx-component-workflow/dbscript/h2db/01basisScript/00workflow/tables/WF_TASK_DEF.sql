--****************************************************************************
-- workflow流程任务实例信息表
--****************************************************************************
create table wf_task_def
(
	id varchar(64) not null,              --流程任务定义
	parentId varchar(64) not null,        --流程任务父节点id
	wftkdkey varchar(64) not null,        --流程任务定义:映射实际流程中的key
	wfdId varchar(64) not null,           --流程定义id
	name varchar(64) not null,            --流程环节名
	alise varchar(64),                    --流程环节别名
	taskorder integer default 0,          --流程环节顺序
	taskType varchar(64),                 --节点任务类型
	serviceType varchar(64),              --业务类型
	isViewAble integer default 1,         --流程是否可见：1：可见
 	primary key(id)
);
create unique index idx_wf_task_def_01 on wf_task_def(wfkey,version);
