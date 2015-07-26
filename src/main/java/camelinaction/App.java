package camelinaction;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

@ContextName("myCamel")
public class App extends RouteBuilder {

    @EndpointInject(uri = "imap://claus@localhost?password=secret")
    private Endpoint endpointIMap;

    @EndpointInject(uri = "smtp://claus@localhost?password=secret&to=jon@localhost")
    private Endpoint endpointErrorReply;


    @Override
    public void configure() throws Exception {
        getContext().addRoutes(new MyRouteBuilder(endpointIMap.getEndpointUri(), endpointErrorReply.getEndpointUri()));
    }

}
