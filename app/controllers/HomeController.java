package controllers;

import actors.MyWebSocketActor;
import actors.SwitchFSM;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.Materializer;
import play.libs.streams.ActorFlow;
import play.mvc.*;

import views.html.*;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private final ActorSystem actorSystem;
    private final Materializer materializer;
    private final ActorRef switchFSMRef;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @Inject
    public HomeController(ActorSystem actorSystem,
                          Materializer materializer) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
        this.switchFSMRef = actorSystem.actorOf(Props.create(SwitchFSM.class));
    }

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public WebSocket socket() {
        return WebSocket.Text.accept(request ->
                ActorFlow.actorRef(actorRef -> MyWebSocketActor.props(actorRef, switchFSMRef),
                        actorSystem, materializer
                )
        );
    }
}
