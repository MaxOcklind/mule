/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.test.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mule.functional.functional.FlowAssert.verify;
import org.mule.functional.junit4.FunctionalTestCase;
import org.mule.runtime.core.MessageExchangePattern;
import org.mule.runtime.core.api.MuleEvent;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NonBlockingFullySupportedFunctionalTestCase extends FunctionalTestCase
{

    public static String FOO = "foo";
    private MessageExchangePattern exchangePattern;

    @Override
    protected String getConfigFile()
    {
        return "non-blocking-fully-supported-test-config.xml";
    }

    public NonBlockingFullySupportedFunctionalTestCase(MessageExchangePattern exchangePattern)
    {
        this.exchangePattern = exchangePattern;
    }

    @Parameters
    public static Collection<Object[]> parameters()
    {
        return Arrays.asList(new Object[][] {
                                             {MessageExchangePattern.REQUEST_RESPONSE},
                                             {MessageExchangePattern.ONE_WAY}});
    }

    @Test
    public void flow() throws Exception
    {
        flowRunner("flow").withPayload(TEST_MESSAGE)
                          .withExchangePattern(getMessageExchnagePattern())
                          .nonBlocking()
                          .run();
    }

    @Test
    public void subFlow() throws Exception
    {
        flowRunner("subFlow").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern()).nonBlocking
                ().run();
    }

    @Test
    public void childFlow() throws Exception
    {
        flowRunner("childFlow").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern())
                .nonBlocking().run();
    }

    @Test
    public void childDefaultFlow() throws Exception
    {
        flowRunner("childDefaultFlow").withPayload(TEST_MESSAGE).nonBlocking().withExchangePattern
                (getMessageExchnagePattern()).run();
        verify("childDefaultFlowChild");
    }

    @Test
    public void childSyncFlow() throws Exception
    {
        flowRunner("childSyncFlow").withPayload(TEST_MESSAGE).nonBlocking().withExchangePattern
                (getMessageExchnagePattern()).run();
        verify("childSyncFlowChild");
    }

    public void childAsyncFlow() throws Exception
    {
        flowRunner("childAsyncFlow").withPayload(TEST_MESSAGE).nonBlocking().withExchangePattern
                (getMessageExchnagePattern()).run();
        verify("childAsyncFlowChild");
    }

    @Test
    public void processorChain() throws Exception
    {
        flowRunner("processorChain").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern())
                .nonBlocking().run();
    }

    @Test
    public void filterAccepts() throws Exception
    {
        flowRunner("filterAccepts").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern())
                .nonBlocking().run();
    }

    @Test
    public void filterRejects() throws Exception
    {
        MuleEvent result = flowRunner("filterRejects").withPayload(TEST_MESSAGE).withExchangePattern
                (getMessageExchnagePattern()).nonBlocking().run();
        assertThat(result, is(nullValue()));
    }

    @Test
    public void filterAfterNonBlockingAccepts() throws Exception
    {
        flowRunner("filterAfterNonBlockingAccepts").withPayload(TEST_MESSAGE).withExchangePattern
                (getMessageExchnagePattern()).nonBlocking().run();
    }

    @Test
    public void filterAfterNonBlockingRejects() throws Exception
    {
        MuleEvent result = flowRunner("filterAfterNonBlockingRejects").withPayload(TEST_MESSAGE).withExchangePattern
                (getMessageExchnagePattern()).nonBlocking().run();
        assertThat(result, is(nullValue()));
    }

    @Test
    public void filterBeforeNonBlockingAccepts() throws Exception
    {
        flowRunner("filterAfterNonBlockingAccepts").withPayload(TEST_MESSAGE).withExchangePattern
                (getMessageExchnagePattern()).nonBlocking().run();
    }

    @Test
    public void filterBeforeNonBlockingRejects() throws Exception
    {
        MuleEvent result = flowRunner("filterAfterNonBlockingRejects").withPayload(TEST_MESSAGE).withExchangePattern
                (getMessageExchnagePattern()).nonBlocking().run();
        assertThat(result, is(nullValue()));
    }

    @Test
    public void filterAfterEnricherBeforeNonBlocking() throws Exception
    {
        MuleEvent result = flowRunner("filterAfterEnricherBeforeNonBlocking").withPayload(TEST_MESSAGE)
                .withExchangePattern(getMessageExchnagePattern()).nonBlocking().run();
        assertThat(result, is(nullValue()));
    }

    @Test
    public void securityFilter() throws Exception
    {
        flowRunner("security-filter").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern())
                .nonBlocking().run();
    }

    @Test
    public void transformer() throws Exception
    {
        flowRunner("transformer").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern())
                .nonBlocking().run();
    }

    @Test
    public void choice() throws Exception
    {
        flowRunner("choice").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern()).nonBlocking()
                .run();
    }

    @Test
    public void enricher() throws Exception
    {
        flowRunner("enricher").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern()).nonBlocking
                ().run();
    }

    @Test
    public void response() throws Exception
    {
        flowRunner("response").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern()).nonBlocking
                ().run();
    }

    @Test
    public void responseWithNullEvent() throws Exception
    {
        MuleEvent result = flowRunner("responseWithNullEvent").withPayload(TEST_MESSAGE).withExchangePattern
                (getMessageExchnagePattern()).nonBlocking().run();
        assertThat(result, is(nullValue()));
    }

    @Test
    public void enricherIssue() throws Exception
    {
        MuleEvent result = flowRunner("enricherIssue").withPayload(TEST_MESSAGE).withExchangePattern
                (getMessageExchnagePattern()).nonBlocking().run();
        assertThat(result.getMessageAsString(), is(equalTo(TEST_MESSAGE)));
    }

    @Test
    public void enricherIssueNonBlocking() throws Exception
    {
        MuleEvent result = flowRunner("enricherIssueNonBlocking").withPayload(TEST_MESSAGE).withExchangePattern
                (getMessageExchnagePattern()).nonBlocking().run();
        assertThat(result.getMessageAsString(), is(equalTo(TEST_MESSAGE)));
    }

    @Test
    public void enricherFlowVar() throws Exception
    {
        MuleEvent result = flowRunner("enricherFlowVar").withPayload(TEST_MESSAGE).withExchangePattern
                (getMessageExchnagePattern()).nonBlocking().run();
        assertThat(result.getFlowVariable(FOO), is(equalTo(TEST_MESSAGE)));
    }

    @Test
    public void async() throws Exception
    {
        flowRunner("async").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern()).nonBlocking()
                .run();
    }

    @Test
    @Ignore("RX")
    public void catchExceptionStrategy() throws Exception
    {
        flowRunner("catchExceptionStrategy").withPayload(TEST_MESSAGE).withExchangePattern(getMessageExchnagePattern()).nonBlocking().run();
        verify("catchExceptionStrategyChild");
    }

    @Test
    public void firstSuccessful() throws Exception
    {
        flowRunner("firstSuccessful").withPayload(TEST_MESSAGE).nonBlocking().run();
    }

    @Test
    public void roundRobin() throws Exception
    {
        flowRunner("roundRobin").withPayload(TEST_MESSAGE).nonBlocking().run();
    }

    @Test
    public void all() throws Exception
    {
        flowRunner("all").withPayload(TEST_MESSAGE).nonBlocking().run();
    }

    private MessageExchangePattern getMessageExchnagePattern()
    {
        return exchangePattern;
    }
}

