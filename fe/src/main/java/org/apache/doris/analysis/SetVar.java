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

import org.apache.doris.catalog.Catalog;
import org.apache.doris.common.AnalysisException;
import org.apache.doris.common.ErrorCode;
import org.apache.doris.common.ErrorReport;
import org.apache.doris.common.UserException;
import org.apache.doris.mysql.privilege.PrivPredicate;
import org.apache.doris.mysql.privilege.UserResource;
import org.apache.doris.qe.ConnectContext;
import org.apache.doris.qe.SessionVariable;

import com.google.common.base.Strings;

// change one variable.
public class SetVar {
    private String variable;
    private LiteralExpr value;
    private SetType type;

    public String getVariable() {
        return variable;
    }

    public LiteralExpr getValue() {
        return value;
    }

    public SetType getType() {
        return type;
    }

    public SetVar() {
    }

    public SetVar(SetType type, String variable, LiteralExpr value) {
        this.type = type;
        this.variable = variable;
        this.value = value;
    }

    public SetVar(String variable, LiteralExpr value) {
        this.type = SetType.DEFAULT;
        this.variable = variable;
        this.value = value;
    }

    public void setType(SetType type) {
        this.type = type;
    }

    // Value can be null. When value is null, means to set variable to DEFAULT.
    public void analyze(Analyzer analyzer) throws AnalysisException, UserException {
        if (type == null) {
            type = SetType.DEFAULT;
        }
        if (Strings.isNullOrEmpty(variable)) {
            throw new AnalysisException("No variable name in set statement.");
        }
        if (type == SetType.GLOBAL) {
            if (!Catalog.getCurrentCatalog().getAuth().checkGlobalPriv(ConnectContext.get(), PrivPredicate.ADMIN)) {
                ErrorReport.reportAnalysisException(ErrorCode.ERR_SPECIFIC_ACCESS_DENIED_ERROR,
                                                    "ADMIN");
            }
        }
        if (value == null) {
            return;
        } else {
            value.analyze(analyzer);
        }
        // Need to check if group is valid
        if (variable.equalsIgnoreCase(SessionVariable.RESOURCE_VARIABLE)) {
            if (value != null && !UserResource.isValidGroup(value.getStringValue())) {
                throw new AnalysisException("Invalid resource group, now we support {low, normal, high}.");
            }
        }
    }

    public String toSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(type.toSql());
        sb.append(" ").append(variable).append(" = ").append(value.toSql());
        return sb.toString();
    }

    @Override
    public String toString() {
        return toSql();
    }
}
