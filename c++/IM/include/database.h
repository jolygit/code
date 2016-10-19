#include <bson.h>
#include <bcon.h>
#include <mongoc.h>
#include <string>
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

