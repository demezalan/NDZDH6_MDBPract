using MongoDB . Bson ;
 using MongoDB . Bson . Serialization . Attributes ;

 namespace MongoTest . Models
 {
 [ BsonIgnoreExtraElements ]
 public class Etterem
 {
 [ BsonId ]
public ObjectId Id { get; set; }
 [ BsonElement ( "nev" ) ]
public required string nev { get; set; }
 [ BsonElement ( "cim" ) ]
public required Cim cim { get; set; }
 [ BsonElement ( "csillag" ) ]
public required int csillag { get; set; }
 }
 }