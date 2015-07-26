/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import javax.activation.DataHandler;


public class MailTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    private MockEndpoint mockResult;

    @EndpointInject(uri = "mock:error-reply")
    private MockEndpoint mockErrorReply;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new MyRouteBuilder("mock:result", "mock:error-reply");
    }

    @Test
    public void testSendWithoutAttachment() throws Exception {
        mockErrorReply.expectedMessageCount(1);

        template.sendBodyAndHeader("smtp://jon@localhost?password=secret&to=claus@localhost", "something", "subject", "Betreff...");

        mockErrorReply.assertIsSatisfied();
    }

    @Test
    public void testSendWithAttachment() throws Exception {
        mockResult.expectedBodiesReceived("Yes, Camel rocks!");

        Exchange exchange = context.getEndpoint("imap://claus@localhost").createExchange(ExchangePattern.InOnly);
        exchange.getIn().setBody("Yes, Camel rocks!");
        exchange.getIn().addAttachment("foo", new DataHandler("", "application/pdf"));

        template.send("smtp://jon@localhost?password=secret&to=claus@localhost", exchange);

        mockResult.assertIsSatisfied();
    }


}
