package ca.qc.cvm.dba.scoutlog.event;

/**
 * Événement utilisé lorsque l'on veut supprimer toute les données
 */
public class DeleteAllEvent extends CommonEvent {
	
	public DeleteAllEvent() {
		super(CommonEvent.Type.DeleteAll);
	}
}
