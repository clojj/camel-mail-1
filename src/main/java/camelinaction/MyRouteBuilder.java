package camelinaction;

import org.apache.camel.builder.RouteBuilder;

class MyRouteBuilder extends RouteBuilder {

    private String uriTo;
    private String uriErrorReply;

    MyRouteBuilder(String uriTo, String uriErrorReply) {
        this.uriTo = uriTo;
        this.uriErrorReply = uriErrorReply;
    }

    @Override
    public void configure() throws Exception {
        from("imap://claus@localhost?password=secret")
                .log("IMAP incoming:\n${headers}")
                .bean(MailInspector.class)
                .log("BODY ${body}")
                .choice()
                .when(body().isEqualTo("NOT Ok")).to("direct:send-error-reply")
                .otherwise().to(uriTo);

        from("direct:send-error-reply")
                .log("Send error-reply")
                .log("\n${headers}")
                .to(uriErrorReply);
    }
}
