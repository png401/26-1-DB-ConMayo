package controller;

import service.CancellationService;
import view.CancellationView;

public class CancellationController {
	
	private final CancellationService cancellationService;
    private final CancellationView cancellationView;
    
    public CancellationController(CancellationService cancellationService,
            CancellationView cancellationView) {
    	this.cancellationService = cancellationService;
    	this.cancellationView = cancellationView;
    }
    
    public void cancel() {}
}
