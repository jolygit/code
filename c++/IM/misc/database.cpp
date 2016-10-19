#include <bson.h>
#include <bcon.h>
#include <mongoc.h>
#include "database.h"

// DataBase::~DataBase(){
//   mongoc_collection_destroy (collection);
//   mongoc_database_destroy (database);
//   mongoc_client_destroy (client);
//   mongoc_cleanup ();
//   bson_destroy (query);
//   mongoc_cursor_destroy (cursor);
// }
extern "C"{
  mongoc_init ();
  mongoc_client_t mongoc_client_new (char*);
  mongoc_database_t mongoc_client_get_database (mongoc_client_t,char*);
  mongoc_collection_t mongoc_client_get_collection (mongoc_client_t ,char*,char*);
  bool mongoc_collection_insert (mongoc_collection_t          *collection,
                                 mongoc_insert_flags_t         flags,
                                 const bson_t                 *document,
                                 const mongoc_write_concern_t *write_concern,
                                 bson_error_t                 *error);
  bool mongoc_collection_update (mongoc_collection_t          *collection,
                                 mongoc_update_flags_t         flags,
                                 const bson_t                 *selector,
                                 const bson_t                 *update,
                                 const mongoc_write_concern_t *write_concern,
                                 bson_error_t                 *error);
  mongoc_cursor_t * mongoc_collection_find (mongoc_collection_t       *collection,
                                 mongoc_query_flags_t       flags,
                                 uint32_t                   skip,
                                 uint32_t                   limit,
                                 uint32_t                   batch_size,
                                 const bson_t              *query,
                                 const bson_t              *fields,
                                 const mongoc_read_prefs_t *read_prefs)
  
}
int DataBase::ConnectToCollection(char* dbName="users",char* collName="users"){
  /*
   * Required to initialize libmongoc's internals
   */
  mongoc_init ();

  /*
   * Create a new client instance
   */
  client = mongoc_client_new ("mongodb://localhost:27017");

  /*
   * Get a handle on the database "db_name" and collection "coll_name"
   */
  database = mongoc_client_get_database (client, dbName);
  collection = mongoc_client_get_collection (client, dbName, collName);
  return 0;
}

int AddKeyValueToNewDocument(char* key,char* value){
  insert = BCON_NEW (key,value);
  if (!mongoc_collection_insert (collection, MONGOC_INSERT_NONE, insert, NULL, &error)) {
    fprintf (stderr, "%s\n", error.message);
  }
  bson_destroy (insert);
  return 0;
}
int AddKeyValueToExistingDocument(char* key,char* value,char* key_add,char* value_add){
  query = bson_new ();
  BSON_APPEND_UTF8 (query, key, value);
  // to change/add key value pair for "username":"vova_valueval"
  update = BCON_NEW ("$set", "{",
		     key_add, BCON_UTF8 (value_add),
		     "}");
  if (!mongoc_collection_update (collection, MONGOC_UPDATE_NONE, query, update, NULL, &error)) {
    printf ("%s\n", error.message);
  }
  return 0;
}
int AddArrayKeyValueToDocument(char* key,char* value,char* arrayName,char* key_add,char* value_add){
  query = bson_new ();
  BSON_APPEND_UTF8 (query, key, value);
  update = BCON_NEW ("$push", "{",
		     arrayName,"{",
		     key_add,value_add,
		     "}",
		     "}");
  if (!mongoc_collection_update (collection, MONGOC_UPDATE_NONE, query, update, NULL, &error)) {
    printf ("%s\n", error.message);
  }
  return 0;
}
int RemoveArrayKeyValueFromDocument(char* key,char* value,char* arrayName,char* key,char* value){
  query = bson_new ();
  BSON_APPEND_UTF8 (query, key, value);
  update = BCON_NEW ("$pull", "{",
		     arrayName,"{",
		     key_add,value_add,
		     "}",
		     "}");
  if (!mongoc_collection_update (collection, MONGOC_UPDATE_NONE, query, update, NULL, &error)) {
    printf ("%s\n", error.message);
  }
  return 0;
}
bool FindKeyValueInArrayOfDocument(char* arrayName,char* key,char* value){
  std::string str=arrayName;
  str+="."+key;
  query = bson_new ();
  BSON_APPEND_UTF8 (query,str.c_str(),value);
  cursor = mongoc_collection_find (collection, MONGOC_QUERY_NONE, 0, 0, 0, query, NULL, NULL);
  if (mongoc_cursor_next (cursor, &doc)) 
     return true;
  else
    return false;
}
