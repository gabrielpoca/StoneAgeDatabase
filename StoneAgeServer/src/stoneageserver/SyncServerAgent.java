package stoneageserver;


public class SyncServerAgent extends Thread {
    
    private ServerState state;
    private boolean run;
    
    public SyncServerAgent(ServerState state) {
        this.state = state;
    }
    
    public void run() {
        run = true;
        while(run) {
            
        }
    }
    
    public void end() {
        run = false;
    }
    
}
