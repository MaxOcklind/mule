/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.module.db.integration.update;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.module.db.integration.DbTestUtil.selectData;
import static org.mule.runtime.module.db.integration.TestRecordUtil.assertRecords;
import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.MuleMessage;
import org.mule.runtime.module.db.integration.AbstractDbIntegrationTestCase;
import org.mule.runtime.module.db.integration.TestDbConfig;
import org.mule.runtime.module.db.integration.model.AbstractTestDatabase;
import org.mule.runtime.module.db.integration.model.Field;
import org.mule.runtime.module.db.integration.model.Record;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runners.Parameterized;

public class UpdateNameParamOverrideTestCase extends AbstractDbIntegrationTestCase {

  public UpdateNameParamOverrideTestCase(String dataSourceConfigResource, AbstractTestDatabase testDatabase) {
    super(dataSourceConfigResource, testDatabase);
  }

  @Parameterized.Parameters
  public static List<Object[]> parameters() {
    return TestDbConfig.getResources();
  }

  @Override
  protected String[] getFlowConfigurationResources() {
    return new String[] {"integration/update/update-name-param-override-config.xml"};
  }

  @Test
  public void usesDefaultParams() throws Exception {
    final MuleEvent responseEvent = flowRunner("defaultParams").withPayload(TEST_MESSAGE).run();

    final MuleMessage response = responseEvent.getMessage();
    assertThat(response.getPayload(), equalTo(1));
    List<Map<String, String>> result = selectData("select * from PLANET where POSITION=4", getDefaultDataSource());
    assertRecords(result, new Record(new Field("NAME", "Mercury"), new Field("POSITION", 4)));
  }

  @Test
  public void usesOverriddenParams() throws Exception {
    final MuleEvent responseEvent = flowRunner("overriddenParams").withPayload(TEST_MESSAGE).run();

    final MuleMessage response = responseEvent.getMessage();
    assertThat(response.getPayload(), equalTo(1));
    List<Map<String, String>> result = selectData("select * from PLANET where POSITION=2", getDefaultDataSource());
    assertRecords(result, new Record(new Field("NAME", "Mercury"), new Field("POSITION", 2)));
  }

  public void usesInlineOverriddenParams() throws Exception {
    final MuleEvent responseEvent = flowRunner("inlineOverriddenParams").withPayload(TEST_MESSAGE).run();

    final MuleMessage response = responseEvent.getMessage();
    assertThat(response.getPayload(), equalTo(1));
    List<Map<String, String>> result = selectData("select * from PLANET where POSITION=3", getDefaultDataSource());
    assertRecords(result, new Record(new Field("NAME", "Mercury"), new Field("POSITION", 3)));
  }

  @Test
  public void usesParamsInInlineQuery() throws Exception {
    final MuleEvent responseEvent = flowRunner("inlineQuery").withPayload(TEST_MESSAGE).run();

    final MuleMessage response = responseEvent.getMessage();
    assertThat(response.getPayload(), equalTo(1));
    List<Map<String, String>> result = selectData("select * from PLANET where POSITION=4", getDefaultDataSource());
    assertRecords(result, new Record(new Field("NAME", "Mercury"), new Field("POSITION", 4)));
  }

  @Test
  public void usesExpressionParam() throws Exception {
    final MuleEvent responseEvent = flowRunner("expressionParam").withPayload(TEST_MESSAGE).withInboundProperty("type", 3).run();

    final MuleMessage response = responseEvent.getMessage();
    assertThat(response.getPayload(), equalTo(1));
    List<Map<String, String>> result = selectData("select * from PLANET where POSITION=3", getDefaultDataSource());
    assertRecords(result, new Record(new Field("NAME", "Mercury"), new Field("POSITION", 3)));
  }
}
