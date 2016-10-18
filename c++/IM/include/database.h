#include <bson.h>
#include <bcon.h>
#include <mongoc.h>

class DataBase{
 public:
  int ConnectToCollection(char* dbName="users",char* collName="users");
  int AddKeyValueToNewDocument(char* key,char* value);
  int AddKeyValueToExistingDocument(char* key,char* value,char* key_add,char* value_add);
  int AddArrayKeyValueToDocument(char* key,char* value,char* arrayName,char* key_add,char* value_add);
  int RemoveArrayKeyValueFromDocument(char* key,char* value,char* arrayName,char* key,char* value);
  int FindKeyValueInArrayOfDocument(char* key,char* value,char* arrayName,char* key,char* value);
 private:
   mongoc_client_t      *client;
   mongoc_database_t    *database;
   mongoc_collection_t  *collection;
   bson_t               *command,
                         reply,
                        *insert;
   bson_error_t          error;
   char                 *str;
   bool                  retval;
   const bson_t          *doc;
   bson_t                *query;
   mongoc_cursor_t       *cursor;
   bson_oid_t            oid;
   bson_t                *update = NULL;
};

int DataBase::ConnectToCollection(char* dbName="users",char* collName="users"){
  mongoc_init ();
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
