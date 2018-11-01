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
import org.apache.doris.catalog.PrimitiveType;
import org.apache.doris.qe.ShowResultSetMetaData;

public class ShowCollationStmt extends ShowStmt {
    private static final ShowResultSetMetaData META_DATA =
            ShowResultSetMetaData.builder()
                    .addColumn(new Column("Collation", ColumnType.createVarchar(20)))
                    .addColumn(new Column("Charset", ColumnType.createVarchar(20)))
                    .addColumn(new Column("Id", ColumnType.createType(PrimitiveType.BIGINT)))
                    .addColumn(new Column("Default", ColumnType.createVarchar(20)))
                    .addColumn(new Column("Compiled", ColumnType.createVarchar(20)))
                    .addColumn(new Column("Sortlen", ColumnType.createType(PrimitiveType.BIGINT)))
                    .build();

    private String pattern;

    public ShowCollationStmt(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void analyze(Analyzer analyzer) {
    }

    @Override
    public ShowResultSetMetaData getMetaData() {
        return META_DATA;
    }
}
