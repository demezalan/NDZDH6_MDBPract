using MongoDB . Bson . Serialization . Attributes ;

namespace MongoTest . Models
{
public class Cim
{
 [ BsonElement ( "varos" ) ]
public required string varos { get; set; }
 [ BsonElement ( "utca" ) ]
public required string utca { get; set; }
 [ BsonElement ( "hazszam" ) ]
public int hazszam { get; set; }
 }
 }