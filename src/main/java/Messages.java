import akka.actor.ActorRef;

public class Messages {
    public static class AddElfMsg {
        public ActorRef ref;

        public AddElfMsg(ActorRef ref){
            this.ref = ref;
        }
    }

    public static class AddReindeerMsg {
        public ActorRef ref;

        public AddReindeerMsg(ActorRef ref){
            this.ref = ref;
        }
    }

    public static class StartWorkingMsg {
    }

    public static class FinishWorkingMsg {
    }

    public static class StoppedElfMsg {
    }

    public static class StoppedReindeerMsg {
    }
}
