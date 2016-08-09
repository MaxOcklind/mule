/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.db.internal.domain.connection;

import java.sql.Connection;

public class DefaultDbConnectionProvider extends AbstractDbConnectionProvider<DbConnection>
{

    @Override
    protected DbConnection createDbConnection(Connection connection) throws Exception
    {
        return new DefaultDbConnection(connection);
    }
}
