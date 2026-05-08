using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace Neptunkod_APIcsharpOWN.Models
{
    [BsonIgnoreExtraElements]
    public class Kurzus
    {
        [BsonId]
        public ObjectId Id { get; set; }
        public string _kurzuskod { get; set; }
        public string nyelv { get; set; }
        public string tipus { get; set; }
        public string idotartam { get; set; }
        public int max_fo { get; set; }
    }
}