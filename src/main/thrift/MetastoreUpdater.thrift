/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
namespace java org.apache.ranger.binding.metastore.thrift

enum MetaStoreUpdateServiceVersion {
  V1
}

enum TErrorCode {
  OK = 0,
  ERROR = 1,
  INVALID = 2
}

enum TTableType {
  TABLE = 0,
  VIEW = 1
}

struct TStatus {
  // Error code
  1: required TErrorCode status;

  // Error message
  2: optional string error_msg;
}

enum TOperation {
  CREATE_DATABASE = 0,
  DROP_DATABASE = 1,
  CREATE_TABLE = 2,
  DROP_TABLE = 3,
  ALTER_TABLE = 4,
  REMAME_TABLE = 5,
  ADD_PARTITION = 6,
  DROP_PARTITION = 7,
  ALTER_PARTITION = 8,
  RENAME_PARTITION = 9,
  INIT_ID = 10
}

// Set of changes to a subscriber, sent from the metastore to a subscriber.
struct TUpdateDelta {
  // global increment id
  1: required i64 id;

  // database name
  2: required string database;

  // table name
  3: required string table;

  // operation type
  4: required TOperation operation;

  // table type， table or view
  5: optional TTableType type;

  // partition name
  6: optional string partition;

  // new name, for rename operation
  7: optional string new_name;
}

struct TUpdateMetadataRequest {
  1: required MetaStoreUpdateServiceVersion protocol_version =
      MetaStoreUpdateServiceVersion.V1;

  // client hostname
  2: required string hostname;

  // Map from topic name to a list of changes for that topic.
  3: required list<TUpdateDelta> deltas;
}

struct TUpdateMetadataResponse {
  1: required MetaStoreUpdateServiceVersion protocol_version =
      MetaStoreUpdateServiceVersion.V1;
  // Whether the call was executed correctly at the application level
  2: required TStatus status;
}

service MetaStoreUpdateService {
  // Called when the metsatore sends a metastore update. The request contains a list of
  // update objects, sent from the metastore to the subscriber.
  TUpdateMetadataResponse updateMetadata(1: TUpdateMetadataRequest params);
}

struct TFetchUpdatesRequest {
  1: required MetaStoreUpdateServiceVersion protocol_version =
      MetaStoreUpdateServiceVersion.V1;
  // start version
  2: required i64 start_version;
  // end version
  3: required i64 end_version;
  // server name
  4: required string server_name;
}

struct TFetchUpdatesResponse {
  1: required MetaStoreUpdateServiceVersion protocol_version =
      MetaStoreUpdateServiceVersion.V1;

  // Map from topic name to a list of changes for that topic.
  2: required list<TUpdateDelta> deltas;
}

service MetaStoreHistoryService {
  // called when the client need fetch updates history.
  TFetchUpdatesResponse fetchUpdates(1: TFetchUpdatesRequest params);
}