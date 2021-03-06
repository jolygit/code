#include <bson.h>
#include <bcon.h>
#include <mongoc.h>
#include <string>
#include <vector>
#include <iostream>
#include <cstring>
using namespace std;
class DataBase{
 public:
  ~DataBase();
  int ConnectToCollection(char const* dbName,char const* collName);
  int AddKeyValueToNewDocument(char const* key,char const* value);
  int AddKeyValueToExistingDocument(char const* key,char const* value,char const* key_add,char const* value_add);
  int AddArrayKeyValueToDocument(char const* key,char const* value,char const* arrayName,char const* key_add,char const* value_add);
  int RemoveArrayKeyValueFromDocument(char const* key,char const* value,char const* arrayName,char const* key_remove,char const* value_remove);
  bool FindKeyValueInArrayOfDocument(char const* key,char const* value,char const* arrayName,char const* key_find,char const* value_find);
   bool Find2KeyValuePair(char const* key,char const* value,char const* key2,char const* value2);
   bool RetreiveValueForUsernameByKey(char const* uname,char const* key,std::vector<std::string>& val);
   bool RetreiveValueForUsernameByKeySimple(char const* uname,char const* key,string& val);
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
   bson_t                *fields;
   mongoc_cursor_t       *cursor;
   bson_oid_t            oid;
   bson_t                *update = NULL;
};

