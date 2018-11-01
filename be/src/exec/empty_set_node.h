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

#pragma once

#include "exec/exec_node.h"

namespace palo {

/// Node that returns an empty result set, i.e., just sets eos_ in GetNext().
/// Corresponds to EmptySetNode.java in the FE.
class EmptySetNode : public ExecNode {
public:
    EmptySetNode(ObjectPool* pool, const TPlanNode& tnode, const DescriptorTbl& descs);
    virtual Status get_next(RuntimeState* state, RowBatch* row_batch, bool* eos) override;
};

}

