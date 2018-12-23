import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Main {
    public static void main(String... args){
        final ActorSystem system = ActorSystem.create("christmas");
        final ActorRef santaRef = system.actorOf(Santa.props());

        new Thread(() -> {
            for(int i = 0; i < Conf.elfSize; i++){
                ActorRef ref = system.actorOf(Elf.props("elf-" + i, santaRef));
                ref.tell(new Messages.StartWorkingMsg(), null);
            }
        }).start();

        new Thread(() -> {
            for(int i = 0; i < Conf.reindeerSize; i++){
                ActorRef ref = system.actorOf(Reindeer.props("reindeer-" + i, santaRef));
                ref.tell(new Messages.StartWorkingMsg(), null);
            }
        }).start();
    }
}
