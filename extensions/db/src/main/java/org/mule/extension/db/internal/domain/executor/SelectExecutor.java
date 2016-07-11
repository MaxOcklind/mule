/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.db.internal.domain.executor;

import org.mule.extension.db.internal.domain.autogeneratedkey.AutoGeneratedKeyStrategy;
import org.mule.extension.db.internal.domain.connection.DbConnection;
import org.mule.extension.db.internal.domain.query.Query;
import org.mule.extension.db.internal.domain.statement.StatementFactory;
import org.mule.extension.db.internal.result.resultset.ResultSetHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Executes queries that return a resultSet
 */
public class SelectExecutor extends AbstractSingleQueryExecutor
{

    private final ResultSetHandler resultHandler;

    public SelectExecutor(StatementFactory statementFactory, ResultSetHandler resultHandler)
    {
        super(statementFactory);
        this.resultHandler = resultHandler;
    }

    @Override
    protected Object doExecuteQuery(DbConnection connection, Statement statement, Query query) throws SQLException
    {
        ResultSet resultSet;

        try
        {
            if (statement instanceof PreparedStatement)
            {
                resultSet = ((PreparedStatement) statement).executeQuery();
            }
            else
            {
                resultSet = statement.executeQuery(query.getQueryTemplate().getSqlText());
            }

            return resultHandler.processResultSet(connection, resultSet);
        }
        finally
        {
            if (!resultHandler.requiresMultipleOpenedResults())
            {
                statement.close();
            }
        }
    }

    @Override
    protected Object doExecuteQuery(DbConnection connection, Statement statement, Query query, AutoGeneratedKeyStrategy autoGeneratedKeyStrategy) throws SQLException
    {
        // Ignores autoGeneratedKeyStrategy which makes no sense in a select
        return doExecuteQuery(connection, statement, query);
    }
}
