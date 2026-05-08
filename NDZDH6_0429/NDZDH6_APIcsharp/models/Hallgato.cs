using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System.Collections.Generic;

namespace Neptunkod_APIcsharpOWN.Models
{
    [BsonIgnoreExtraElements]
    public class Hallgato
    {
        [BsonId]
        public ObjectId Id { get; set; }
        public string _sid { get; set; }
        public string nev { get; set; }
        public Lakcim lakcim { get; set; }
        public string email { get; set; }
        public string szuletesi_ido { get; set; }
        

        public List<string> kurzusok { get; set; } 
    }
}