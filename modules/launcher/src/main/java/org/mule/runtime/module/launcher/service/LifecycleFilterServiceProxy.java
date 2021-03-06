/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.module.launcher.service;

import static org.mule.runtime.core.util.ClassUtils.findImplementedInterfaces;
import static org.mule.runtime.core.util.Preconditions.checkArgument;
import org.mule.runtime.api.service.Service;
import org.mule.runtime.core.api.lifecycle.Startable;
import org.mule.runtime.core.api.lifecycle.Stoppable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Proxies a {@link Service} instance to filter invocations of lifecycle methods from {@link Startable} and {@link Stoppable}
 * interfaces.
 */
public class LifecycleFilterServiceProxy implements InvocationHandler {

  private final Service service;

  /**
   * Creates a new proxy for the provided service instance.
   *
   * @param service service instance to wrap. Non null.
   */
  public LifecycleFilterServiceProxy(Service service) {
    checkArgument(service != null, "service cannot be null");
    this.service = service;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (method.getDeclaringClass() == Startable.class || method.getDeclaringClass() == Stoppable.class) {
      throw new UnsupportedOperationException("Cannot invoke lifecycle methods on a service instance");
    }

    return method.invoke(service, args);
  }

  /**
   * Creates a proxy for the provided service instance.
   *
   * @param service service to wrap. Non null.
   * @return a new proxy instance.
   */
  public static Service createServiceProxy(Service service) {
    checkArgument(service != null, "service cannot be null");
    InvocationHandler handler = new LifecycleFilterServiceProxy(service);

    return (Service) Proxy.newProxyInstance(service.getClass().getClassLoader(), findImplementedInterfaces(service.getClass()),
                                            handler);
  }
}
