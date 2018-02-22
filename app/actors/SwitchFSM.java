package actors;

import akka.actor.AbstractLoggingFSM;
import akka.actor.ActorRef;
import akka.actor.Props;

import static actors.State.*;

enum State {
    Off, On
}

final class NoData {}

public class SwitchFSM extends AbstractLoggingFSM<State, NoData> {
    {
        startWith(Off, new NoData());

        when(Off, matchEventEquals("turn_on", (state, data) ->
                goTo(On)
                        .using(new NoData())
                        .replying(new Messages.OutMsg("turned_on"))));

        when(On, matchEventEquals("turn_off", (state, data) ->
                goTo(Off)
                        .using(new NoData())
                        .replying(new Messages.OutMsg("turned_off"))));

        whenUnhandled(matchAnyEvent((state, data) ->
                        stay().replying("received unhandled request" + state.toString())));

        initialize();
    }
}