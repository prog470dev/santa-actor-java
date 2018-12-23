import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashSet;
import java.util.Set;

public class Santa extends AbstractActor {
    private static final int elfMax = 3;
    private static final int reindeerMax = 9;

    private int stoppedElfCount;
    private int stoppedReindeerCount;

    private Set<ActorRef> reindeerSet;
    private Set<ActorRef> elfSet;

    public Santa(){
        this.stoppedElfCount = 0;
        this.stoppedReindeerCount = 0;
        this.reindeerSet = new HashSet<>();
        this.elfSet = new HashSet<>();
    }

    static public Props props() {
        return Props.create(Santa.class, () -> new Santa());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.AddElfMsg.class, this::addElfMsg)
                .match(Messages.AddReindeerMsg.class, this::addReindeerMsg)
                .match(Messages.StoppedElfMsg.class, this::stoppedElf)
                .match(Messages.StoppedReindeerMsg.class, this::stoppedReindeer)
                .build();
    }

    private void addElfMsg(Messages.AddElfMsg msg){
        this.elfSet.add(msg.ref);
        if(this.elfSet.size() == elfMax){
            develop();
        }
    }

    private void addReindeerMsg(Messages.AddReindeerMsg msg){
        this.reindeerSet.add(msg.ref);
        if(this.reindeerSet.size() == reindeerMax){
            ship();
        }
    }

    private void develop(){
        System.out.println("Santa starts to develop.");
        for (ActorRef ref : this.elfSet) {
            ref.tell(new Messages.FinishWorkingMsg(), this.getSelf());
        }
        this.elfSet = new HashSet<>();
        System.out.println("Santa starts to sleep..");
    }

    private void ship(){
        System.out.println("Santa starts to ship.");
        for (ActorRef ref : this.reindeerSet) {
            ref.tell(new Messages.FinishWorkingMsg(), this.getSelf());
        }
        this.reindeerSet = new HashSet<>();
        System.out.println("Santa starts to sleep..");
    }

    private void stoppedElf(Messages.StoppedElfMsg msg){
        this.stoppedElfCount++;
        terminateSystem();
    }

    private void stoppedReindeer(Messages.StoppedReindeerMsg msg){
        this.stoppedReindeerCount++;
        terminateSystem();
    }

    private void terminateSystem(){
        if(this.stoppedElfCount == Conf.elfSize && this.stoppedReindeerCount == Conf.reindeerSize){
            try{
                Thread.sleep(100);
            }catch (Exception e){}
            this.getContext().getSystem().terminate();
        }
    }
}
