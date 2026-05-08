using MongoDB.Driver;
using MongoTest.Models;

class Program
{
    static void Main(string[] args)
    {
        var client = new MongoClient(
        "mongodb+srv://asd:asd@cluster0.zewoi03.mongodb.net/");
        var database = client.GetDatabase("vendeglatas");
        var etteremCollection =
        database.GetCollection<Etterem>("ettermek");
        var foszakacsCollection =
        database.GetCollection<Foszakacs>("foszakacsok");

        //---------- 1. feladat ------------
        //Etterem kiiratasa
        var ettermek = etteremCollection.Find(_ => true).ToList();

        foreach (var e in ettermek)
        {
            Console.WriteLine("-------");
            Console.WriteLine($"Név: {e.nev}");
            Console.WriteLine($"Város: {e.cim?.varos}");
            Console.WriteLine($"Utca: {e.cim?.utca}");
            Console.WriteLine($"Házszám: {e.cim?.hazszam}");
            Console.WriteLine($"Csillag: {e.csillag}");
        }

        //Foszakacsok kiiratasa
        var foszakacsok = foszakacsCollection.Find(_ => true).ToList();

        foreach (var f in foszakacsok)
        {
            Console.WriteLine("-------");
            Console.WriteLine($"Név: {f.nev}");
            Console.WriteLine($"Életkor: {f.eletkor}");
            Console.WriteLine($"Fkód: {f._fkod}");
            Console.WriteLine($"E_F: {f._e_f}");
            Console.WriteLine("Végzettség:");
            foreach (var v in f.vegzettseg)
            {
                Console.WriteLine("- " + v);
            }
        }



        //---------- 2. feladat ------------
        //Új étterem beszúrása
        var ujEtterem = new Etterem
        {
            nev = "Valhalla",
            cim = new Cim
            {
                varos = "Nyíregyháza",
                utca = "Sas",
                hazszam = 3
            },
            csillag = 5
        };
        etteremCollection.InsertOne(ujEtterem);
        Console.WriteLine("Sikeres beszúrás!");


        //Új főszakács beszúrása
        var ujFoszakacs = new Foszakacs
        {
            nev = "Hegedűs Lajos",
            eletkor = 25,
            vegzettseg = new List<string> { "Le Cordon Bleu" },
            _fkod = "f3",
            _e_f = "e1"
        };
        foszakacsCollection.InsertOne(ujFoszakacs);
        Console.WriteLine("Sikeres beszúrás!");


        //Csillag módosítása
        var filter = Builders<Etterem>.Filter.Eq(e => e.nev, "Valhalla");
        var update = Builders<Etterem>.Update.Set(e => e.csillag, 3);
        etteremCollection.UpdateOne(filter, update);
        Console.WriteLine("Sikeres módosítás!");


        // 30 életkor alatti törlés
        var deleteFilter = Builders<Foszakacs>.Filter.Lt(f => f.eletkor, 30);
        foszakacsCollection.DeleteMany(deleteFilter);
        Console.WriteLine("Sikeres törlés!");

        //Műszak hozzáadása gyakornokhoz
        var addFilter = Builders<Gyakornok>.Filter.Eq(g => g.nev, "S z i l g y i I s t v n ");
        var addUpdate = Builders<Gyakornok>.Update.Push(g => g.muszak, " jszaka ");
        gyakornokCollection.UpdateOne(addFilter, addUpdate);
        Console.WriteLine(" Sikeres hozzadas !");
    }
}