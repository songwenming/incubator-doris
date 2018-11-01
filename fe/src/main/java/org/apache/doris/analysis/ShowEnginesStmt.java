// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.analysis;

import org.apache.doris.catalog.Column;
import org.apache.doris.catalog.ColumnType;
import org.apache.doris.qe.ShowResultSetMetaData;

public class ShowEnginesStmt extends ShowStmt {
    private static final ShowResultSetMetaData META_DATA =
            ShowResultSetMetaData.builder()
                    .addColumn(new Column("Engine", ColumnType.createVarchar(64)))
                    .addColumn(new Column("Support", ColumnType.createVarchar(8)))
                    .addColumn(new Column("Comment", ColumnType.createVarchar(80)))
                    .addColumn(new Column("Transactions", ColumnType.createVarchar(3)))
                    .addColumn(new Column("XA", ColumnType.createVarchar(3)))
                    .addColumn(new Column("Savepoints", ColumnType.createVarchar(3)))
                    .build();

    @Override
    public void analyze(Analyzer analyzer) {

    }

    @Override
    public String toSql() {
        return "SHOW ENGINES";
    }

    @Override
    public String toString() {
        return toSql();
    }

    @Override
    public ShowResultSetMetaData getMetaData() {
        return META_DATA;
    }
}
