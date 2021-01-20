package ca.qc.cvm.dba.scoutlog.event;

import ca.qc.cvm.dba.scoutlog.entity.LogEntry;

/**
 * Événement utilisé lorsque l'on veut sauvegarder une nouvelle
 * entrée dans le journal
 */
public class AddLogEvent extends CommonEvent {
	private LogEntry log;
	
	public AddLogEvent(LogEntry log) {
		super(CommonEvent.Type.AddLog);
		
		this.log = log;
	}
	
	public LogEntry getLog() {
		return log;
	}
}
