import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class Reindeer extends AbstractActor{
    final private String name;
    final private ActorRef santaRef;

    public Reindeer(String name, ActorRef santaRef){
        this.name = name;
        this.santaRef = santaRef;
    }

    static public Props props(String name, ActorRef santaRef) {
        return Props.create(Reindeer.class, () -> new Reindeer(name, santaRef));
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Messages.StartWorkingMsg.class, this::addReindeer)
                .match(Messages.FinishWorkingMsg.class, this::finishWorking)
                .build();
    }

    private void addReindeer(Messages.StartWorkingMsg msg){
       System.out.println(this.name + " starts to work.");
       this.santaRef.tell(new Messages.AddReindeerMsg(this.getSelf()), this.getSelf());
    }

    private void finishWorking(Messages.FinishWorkingMsg msg){
        System.out.println(this.name + " has finished working.");
        this.santaRef.tell(new Messages.StoppedReindeerMsg(), this.getSelf());
        this.context().system().stop(this.getSelf());
    }
}
