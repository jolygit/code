#include <bson.h>
#include <bcon.h>
#include <mongoc.h>
#include "database.h"
#include        <boost/algorithm/string.hpp>
#include        <vector>
 DataBase::~DataBase(){
   mongoc_collection_destroy (collection);
   mongoc_database_destroy (database);
   mongoc_client_destroy (client);
   mongoc_cleanup ();
   bson_destroy (query);
   mongoc_cursor_destroy (cursor);
 }
using namespace std;
extern "C"{
  void mongoc_init(void);
  mongoc_client_t               *mongoc_client_new(const char*uri_string);
  mongoc_database_t* mongoc_client_get_database(mongoc_client_t*, const char*);
  mongoc_collection_t* mongoc_client_get_collection (mongoc_client_t* ,const char*,const char*);
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
				 const mongoc_read_prefs_t *read_prefs);
  
}
int DataBase::ConnectToCollection(char const* dbName,char const* collName){
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

int DataBase::AddKeyValueToNewDocument(char const* key,char const* value){
  insert = BCON_NEW (key,value);
  if (!mongoc_collection_insert (collection, MONGOC_INSERT_NONE, insert, NULL, &error)) {
    fprintf (stderr, "%s\n", error.message);
  }
  bson_destroy (insert);
  return 0;
}
int DataBase::AddKeyValueToExistingDocument(char const* key,char const* value,char const* key_add,char const* value_add){
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
int DataBase::AddArrayKeyValueToDocument(char const* key,char const* value,char const* arrayName,char const* key_add,char const* value_add){
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
int DataBase::RemoveArrayKeyValueFromDocument(char const* key,char const* value,char const* arrayName,char const* key_rm,char const* value_rm){
  query = bson_new ();
  BSON_APPEND_UTF8 (query, key, value);
  update = BCON_NEW ("$pull", "{",
		     arrayName,"{",
		     key_rm,value_rm,
		     "}",
		     "}");
  if (!mongoc_collection_update (collection, MONGOC_UPDATE_NONE, query, update, NULL, &error)) {
    printf ("%s\n", error.message);
  }
  return 0;
}
bool DataBase::FindKeyValueInArrayOfDocument(char const* key,char const* value,char const* arrayName,char const* key_find,char const* value_find){
  query = bson_new ();
  if(arrayName && key_find && value_find){
    std::string str;
    str=arrayName;
    str+=".";
    str+=key_find;
    BSON_APPEND_UTF8 (query,str.c_str(),value_find);
  }
  BSON_APPEND_UTF8 (query,key,value);
  cursor = mongoc_collection_find (collection, MONGOC_QUERY_NONE, 0, 0, 0, query, NULL, NULL);
  if (mongoc_cursor_next (cursor, &doc)) 
    return true;
  else
    return false;
 }
bool DataBase::Find2KeyValuePair(char const* key,char const* value,char const* key2,char const* value2){
  query = bson_new ();
  // BSON_APPEND_UTF8 (query,key,value);
  BSON_APPEND_UTF8 (query,key2,value2);
  cursor = mongoc_collection_find (collection, MONGOC_QUERY_NONE, 0, 0, 0, query, NULL, NULL);
  if (mongoc_cursor_next (cursor, &doc)) 
    return true;
  else
    return false;
}
bool DataBase::RetreiveValueForUsernameByKey(char const* uname,char const* key,std::vector<std::string>& value){
  query = bson_new ();
  BSON_APPEND_UTF8 (query,"username",uname);
  fields = bson_new ();
  BSON_APPEND_UTF8 (fields, key, "1");
  cursor = mongoc_collection_find (collection, MONGOC_QUERY_NONE, 0, 0, 0, query,fields, NULL);
  if(mongoc_cursor_next (cursor, &doc)){
    str = bson_as_json (doc, NULL);
    string tmpval=str;
    vector<string> vals;
    boost::split(vals,tmpval,boost::is_any_of("[]"));
    if(vals.size()<2)
      return false;
    tmpval=vals[1];
    boost::split(vals,tmpval,boost::is_any_of("\""));
    if(vals.size()<2)
      return false;
    else{
      vector<string> vals;
      boost::split(vals,tmpval,boost::is_any_of("\""));
      if(((vals.size()-1)%4)!=0) {// no friends
	printf("unexpected value field %s\n",tmpval.c_str());
	return 0;
      }
      for (size_t i = 0; i < vals.size()/4; i++){
	value.push_back(vals[4*(i+1)-1]);
      }
      return true;
    }
  }
  return false;
}
