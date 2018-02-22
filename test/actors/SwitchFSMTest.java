package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SwitchFSMTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("SwitchFSMTest");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testIt(){
        new TestKit(system){{
            final ActorRef fsm = system.actorOf(Props.create(SwitchFSM.class));
            final ActorRef probe = getRef();

            fsm.tell("turn_on", probe);
            expectMsgEquals("turned_on");
            fsm.tell("turn_off", probe);
            expectMsgEquals("turned_off");
            system.stop(fsm);
        }};
    }
}