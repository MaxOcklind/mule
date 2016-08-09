/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tck.testmodels.fruit;

import org.mule.runtime.core.api.MuleException;

import java.util.HashMap;

/**
 * A specialisation of Orange
 */
public class BloodOrange extends Orange {

  public BloodOrange() {}

  public BloodOrange(HashMap props) throws MuleException {
    super(props);
  }

  public BloodOrange(Integer segments, Double radius, String brand) {
    super(segments, radius, brand);
  }
}
