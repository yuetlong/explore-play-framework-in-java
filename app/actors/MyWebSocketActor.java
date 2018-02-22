package actors;

import akka.actor.*;

public class MyWebSocketActor extends AbstractActor {

    private final ActorRef fsmRef;
    private final ActorRef out;

    public static Props props(ActorRef out, ActorRef fsmRef) {
        return Props.create(MyWebSocketActor.class, out, fsmRef);
    }

    public MyWebSocketActor(ActorRef out, ActorRef fsmRef) {
        this.out = out;
        this.fsmRef = fsmRef;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("wsactor", _arg -> out.tell("spooky dabs", self()))
                .match(String.class, message -> fsmRef.forward(message, getContext()))
                .match(Messages.OutMsg.class, message -> out.tell(message.s, self()))
                .build();
    }
}