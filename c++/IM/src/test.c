#include <bson.h>
#include <bcon.h>
#include <mongoc.h>

int
main (int   argc,
      char *argv[])
{
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
   database = mongoc_client_get_database (client, "users");
   collection = mongoc_client_get_collection (client, "users", "users");

   
     /* insert = BCON_NEW ("username", BCON_UTF8 ("vova")); */
     
     /* if (!mongoc_collection_insert (collection, MONGOC_INSERT_NONE, insert, NULL, &error)) { */
     /*    fprintf (stderr, "%s\n", error.message); */
     /* } */
   
   //bson_destroy (insert);
 
   // query particular user name, change the value of the username and add more fields
    
     query = bson_new ();
     BSON_APPEND_UTF8 (query, "username", "alex");
      // to change/add key value pair for "username":"vova_valueval"
     /* 
      * update = BCON_NEW ("$set", "{",
      * 			"username", BCON_UTF8 ("vova"),
      * 			"updated",BCON_BOOL (false),
      * 		      "}");
      */
     // to remove key value pair for "updated":BCON_BOOL (false)
     /* 
      * update = BCON_NEW ("$unset", "{",
      * 			"updated","1",
      * 		      "}");
      */
      // to add to an array
      
       /* update = BCON_NEW ("$push", "{", */
       /* 			"friends","{", */
       /* 			             "username","alex", */
       /* 			          "}", */
       /* 		               	"}"); */
      
     // to remove particular elemet of an array
       /* 
        * update = BCON_NEW ("$pull", "{",
        * 			"friends","{",
        * 			             "username","aj476",
        * 			          "}",
        * 		               	"}");
        */
       
     /* if (!mongoc_collection_update (collection, MONGOC_UPDATE_NONE, query, update, NULL, &error)) { */
     /*   printf ("%s\n", error.message); */
     /* } */
    
    
  
   //find all the docs in colleciton
   //query = bson_new ();
   /* BSON_APPEND_UTF8 (query, "friends.username", "alex"); */
   /* cursor = mongoc_collection_find (collection, MONGOC_QUERY_NONE, 0, 0, 0, query, NULL, NULL); */

   /* while (mongoc_cursor_next (cursor, &doc)) { */
   /*   str = bson_as_json (doc, NULL); */
   /*   printf ("%s\n", str); */
   /*   bson_free (str); */
   /* } */

     //  BSON_APPEND_UTF8 (query, "lastName", "1");
     fields = bson_new ();
     BSON_APPEND_UTF8 (fields, "lastName", "1");
     cursor = mongoc_collection_find (collection, MONGOC_QUERY_NONE, 0, 0, 0, query,fields, NULL);
     while (mongoc_cursor_next (cursor, &doc)) { 
       str = bson_as_json (doc, NULL); 
       printf ("%s\n", str); 
       bson_free (str); 
     }
     //  cursor = mongo_find(&client, “db.users”, query, fields, 0, 0, 0);
     // fprintf(stderr, “cursor:%p\n”, cursor);
     
  
  
   /* printf ("%s\n", str); */
   /*  while (mongoc_cursor_next (cursor, &doc)) { */
   /*    str=doc.get("lastName").toString(); */
   /*    // str = bson_as_json (doc, NULL);  */
   /*    printf ("%s\n", str);  */
   /*    bson_free (str);  */
   /*  }  */
   
   /*
    * Release our handles and clean up libmongoc
    */
   mongoc_collection_destroy (collection);
   mongoc_database_destroy (database);
   mongoc_client_destroy (client);
   mongoc_cleanup ();
   bson_destroy (query);
   mongoc_cursor_destroy (cursor);
   return 0;
}

/* 
 * #include <mongoc.h>
 * #include <stdio.h>
 * #include <stdlib.h>
 * 
 * int
 * main (int   argc,
 *       char *argv[])
 * {
 *    mongoc_client_t *client;
 *    mongoc_collection_t *collection;
 *    mongoc_cursor_t *cursor;
 *    bson_error_t error;
 *    const bson_t *doc;
 *    const char *uristr = "mongodb://127.0.0.1:27017";///?appname=client-example
 *    const char *collection_name = "restaurants";
 *    bson_t query;
 *    char *str;
 * 
 *    mongoc_init (); 
 * 
 *    if (argc > 1) {
 *       uristr = argv [1];
 *    }
 * 
 *    if (argc > 2) {
 *       collection_name = argv [2];
 *    }
 * 
 *    client = mongoc_client_new (uristr);
 * 
 *    if (!client) {
 *       fprintf (stderr, "Failed to parse URI.\n"); 
 *       return EXIT_FAILURE;
 *    }
 * 
 *    // mongoc_client_set_error_api (client, 2);
 * 
 *    bson_init (&query);
 * 
 * #if 0
 *    bson_append_utf8 (&query, "hello", -1, "world", -1);
 * #endif
 * 
 *    collection = mongoc_client_get_collection (client, "users", collection_name);
 *    cursor = mongoc_collection_find (collection,
 *                                     MONGOC_QUERY_NONE,
 *                                     0,
 *                                     0,
 *                                     0,
 *                                     &query,
 *                                     NULL,  /\* Fields, NULL for all. *\/
 *                                     NULL); /\* Read Prefs, NULL for default *\/
 * 
 *    while (mongoc_cursor_next (cursor, &doc)) {
 *       str = bson_as_json (doc, NULL);
 *       fprintf (stdout, "%s\n", str);
 *       bson_free (str);
 *    }
 * 
 *    if (mongoc_cursor_error (cursor, &error)) {
 *       fprintf (stderr, "Cursor Failure: %s\n", error.message);
 *       return EXIT_FAILURE;
 *    }
 * 
 *    bson_destroy (&query);
 *    mongoc_cursor_destroy (cursor);
 *    mongoc_collection_destroy (collection);
 *    mongoc_client_destroy (client);
 * 
 *    mongoc_cleanup ();
 * 
 *    return EXIT_SUCCESS;
 * }
 */
