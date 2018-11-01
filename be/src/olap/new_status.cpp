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

#include "olap/new_status.h"

#include <stdio.h>
#include "common/logging.h"

namespace palo {

std::string ErrnoToString(int err) {
    char errmsg[128];
    char* ret = strerror_r(err, errmsg, 128);
    if (ret != errmsg) {
        strncpy(errmsg, ret, 128);
        errmsg[127] = '\0';
    }   
    return std::string(errmsg);
}

NewStatus IOError(const std::string& context, int err) {
    switch (err) {
    case ENOENT:
        return NewStatus::NotFound(context, ErrnoToString(err), err);
    case EEXIST:
        return NewStatus::AlreadyExist(context, ErrnoToString(err), err);
    case EOPNOTSUPP:
        return NewStatus::NotSupported(context, ErrnoToString(err), err);
    case EIO:
        return NewStatus::DiskFailure(context, ErrnoToString(err), err);
    case ENODEV:
        return NewStatus::DiskFailure(context, ErrnoToString(err), err);
    case ENXIO:
        return NewStatus::DiskFailure(context, ErrnoToString(err), err);
    case EROFS:
        return NewStatus::DiskFailure(context, ErrnoToString(err), err);
    }
    return NewStatus::IOError(context, ErrnoToString(err), err);
}

NewStatus::NewStatus(Code code, const StringSlice& msg, const StringSlice& msg2, int32_t posix_code)
        : _code(code), _posix_code(posix_code) {
    DCHECK(code != kOk);
    const uint32_t len1 = msg.size;
    const uint32_t len2 = msg2.size;
    const uint32_t size = len1 + (len2 ? (2 + len2) : 0);
    char* result = new char[size + 4];
    memcpy(result, &size, sizeof(size));
    memcpy(result + 4, msg.data, len1);
    if (len2) {
        result[len1 + 4] = ':';
        result[len1 + 5] = ' ';
        memcpy(result + len1 + 6, msg2.data, len2);
    }
    _state = result;
}

const char* NewStatus::CopyState(const char* state) {
    uint32_t size;
    memcpy(&size, state, sizeof(size));
    char* result = new char[size + 4];
    memcpy(result, state, size + 4);
    return result;
}

std::string NewStatus::CodeAsString() const {
    const char* type = nullptr;
    switch (_code) {
    case kOk:
        type = "OK";
        break;
    case kNotFound:
        type = "NotFound";
        break;
    case kCorruption:
        type = "Corruption";
        break;
    case kNotSupported:
        type = "Not implemented";
        break;
    case kInvalidArgument:
        type = "Invalid argument";
        break;
    case kAlreadyExist:
        type = "Already Exist";
        break;
    case kNoSpace:
        type = "No Space";
        break;
    case kEndOfFile:
        type = "End Of File";
        break;
    case kDiskFailure:
        type = "Disk Failure";
        break;
    case kIOError:
        type = "IO error";
        break;
    case kTimedOut:
        type = "Timed Out";
        break;
    case kMemoryLimitExceeded:
        type = "Memory Limit Exceeded";
        break;
    case kDeadLock:
        type = "Dead Lock";
        break;
    }

    return std::string(type);
}

std::string NewStatus::ToString() const {
    std::string result(CodeAsString());
    if (code() == kOk) {
        return result;
    }

    result.append(": ");
    uint32_t length;
    memcpy(&length, _state, sizeof(length));
    result.append(_state + 4, length);

    if (_posix_code != -1) {
        char buf[64];
        snprintf(buf, sizeof(buf), ", errno=%d", _posix_code);
        result.append(buf);
    }

    return result;
}

}  // namespace palo
