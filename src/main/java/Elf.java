import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class Elf extends AbstractActor {
    final private String name;
    final private ActorRef santaRef;

    public Elf(String name, ActorRef santaRef){
        this.name = name;
        this.santaRef = santaRef;
    }

    static public Props props(String name, ActorRef santaRef) {
        return Props.create(Elf.class, () -> new Elf(name, santaRef));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.StartWorkingMsg.class, this::addElf)
                .match(Messages.FinishWorkingMsg.class, this::finishWorking)
                .build();
    }

    private void addElf(Messages.StartWorkingMsg msg){
        System.out.println(this.name + " starts to work.");
        this.santaRef.tell(new Messages.AddElfMsg(this.getSelf()), this.getSelf());
    }

    private void finishWorking(Messages.FinishWorkingMsg msg){
        System.out.println(this.name + " has finished working.");
        this.santaRef.tell(new Messages.StoppedElfMsg(), this.getSelf());
        this.context().system().stop(this.getSelf());
    }
}
