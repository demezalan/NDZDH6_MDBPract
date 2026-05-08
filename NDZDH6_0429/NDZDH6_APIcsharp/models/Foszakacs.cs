using MongoDB . Bson ;
using MongoDB . Bson . Serialization . Attributes ;

public class Foszakacs
{
 [ BsonId ]
public ObjectId Id { get; set; }
 [ BsonElement ( "nev" ) ]
public required string nev { get; set ; }
 [ BsonElement ( "eletkor" ) ]
public int eletkor { get; set; }
 [ BsonElement ( "vegzettseg" ) ]
public required List <string > vegzettseg { get; set; }
 [ BsonElement ( "_fkod" ) ]
public required string _fkod { get ; set; }
 [ BsonElement ( "_e_f" ) ]
public required string _e_f { get; set; }
 }