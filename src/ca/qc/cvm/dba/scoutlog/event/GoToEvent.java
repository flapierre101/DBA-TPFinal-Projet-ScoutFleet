package ca.qc.cvm.dba.scoutlog.event;

import ca.qc.cvm.dba.scoutlog.view.FrameMain.Views;

/**
 * Événement pour passer d'un panel à un autre
 */
public class GoToEvent extends CommonEvent {
	private Views destination;
	
	public GoToEvent(Views destination) {
		super(CommonEvent.Type.GoTo);
		
		this.destination = destination;
	}
	
	public Views getDestination() {
		return destination;
	}
}
