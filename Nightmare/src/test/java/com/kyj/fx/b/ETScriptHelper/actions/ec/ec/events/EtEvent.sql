select
	e.name
	,e.EventGUID
	,e.Description
	,e.TrueStateText
	,e.FalseStateText
	,e.DefaultState
	,e.EventType
	,e.DisplayExecution
	,e.DisplayEventStates
	,e.TTCInterval --standard time for complete
	/*  TTCIntervalType
		standard time  for complete type
		0:minute
		1:hours
		2:days
		3:weeks
		4:months
		5:years
	*/
	,e.TTCIntervalType 

	/*
		ScheduleType  
		0: do not schedule
		1: scheduled date
		2: performed date
	*/
	, e.ScheduleType 
	, e.ScheduleInterval
	/*
		
		0:hours
		1:days
		2:weeks
		3:months
		4:years
	*/
	, e.ScheduleIntervalType
	,e.*
  from dmi_et.dbo.ET_Events e (nolock)
 WHERE 1=1
 AND e.EquipmentClassGUID = 'BF560681-467A-48C8-812C-2A65E9A85DD4'
 and Disabled = 0