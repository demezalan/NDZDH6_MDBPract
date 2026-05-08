using System;
using System.Collections.Generic;
using System.Linq;
using MongoDB.Bson;
using MongoDB.Driver;
using Neptunkod_APIcsharpOWN.Models;

namespace Neptunkod_APIcsharpOWN
{
    class Program
    {
        static void Main(string[] args)
        {
            // 1. Feladat: Kapcsolódás az adatbázishoz
            var client = new MongoClient("mongodb://localhost:27017");
            var database = client.GetDatabase("egyetemi_rendszer");
            
            var hallgatoCollection = database.GetCollection<Hallgato>("hallgato");
            var kurzusCollection = database.GetCollection<Kurzus>("kurzus");

            // --- Alap kiíratás (Read) ---
            Console.WriteLine("=== KURZUSOK LISTÁJA ===");
            var kurzusok = kurzusCollection.Find(_ => true).ToList();
            foreach (var k in kurzusok)
            {
                Console.WriteLine($"Kód: {k._kurzuskod} | Típus: {k.tipus} | Max fő: {k.max_fo}");
            }


            // ==========================================
            // 2. Feladat: DML MŰVELETEK
            // ==========================================

            // Új kurzus beszúrása
            var ujKurzus = new Kurzus
            {
                _kurzuskod = "k4",
                nyelv = "német",
                tipus = "előadás",
                idotartam = "90 perc",
                max_fo = 15
            };
            kurzusCollection.InsertOne(ujKurzus);
            Console.WriteLine("\nSikeres beszúrás (Új kurzus)!");

            // Max férőhely módosítása (Update)
            var updateFilter = Builders<Kurzus>.Filter.Eq(k => k._kurzuskod, "k4");
            var updateAction = Builders<Kurzus>.Update.Set(k => k.max_fo, 20);
            kurzusCollection.UpdateOne(updateFilter, updateAction);
            Console.WriteLine("Sikeres módosítás (Max fő frissítve)!");

            // 20 fő alatti kurzusok törlése (Delete)
            var deleteFilter = Builders<Kurzus>.Filter.Lt(k => k.max_fo, 20);
            kurzusCollection.DeleteMany(deleteFilter);
            Console.WriteLine("Sikeres törlés (20 főnél kisebb kurzusok eltávolítva)!");

            // Új elem hozzáadása tömbhöz (Push) - Pl. hallgató felvesz egy kurzust
            var pushFilter = Builders<Hallgato>.Filter.Eq(h => h.nev, "Kovács László");
            var pushAction = Builders<Hallgato>.Update.Push(h => h.kurzusok, "k1");
            hallgatoCollection.UpdateOne(pushFilter, pushAction);
            Console.WriteLine("Sikeres tömb-bővítés ($push használatával)!");


            // ==========================================
            // 3. Feladat: LEKÉRDEZÉSEK (Queries)
            // ==========================================

            // Előadás típusú kurzusok
            Console.WriteLine("\n=== ELŐADÁSOK ===");
            var eloadasok = kurzusCollection.Find(k => k.tipus == "előadás").ToList();
            foreach (var k in eloadasok)
            {
                Console.WriteLine($"{k._kurzuskod} ({k.max_fo} fő)");
            }

            // Budapestiek VAGY Debreceniek ($or)
            Console.WriteLine("\n=== BUDAPESTI VAGY DEBRECENI HALLGATÓK ===");
            var orFilter = Builders<Hallgato>.Filter.Eq(h => h.lakcim.varos, "Budapest") | 
                           Builders<Hallgato>.Filter.Eq(h => h.lakcim.varos, "Debrecen");
            var varosiHallgatok = hallgatoCollection.Find(orFilter).ToList();
            foreach (var h in varosiHallgatok)
            {
                Console.WriteLine($"{h.nev} - Város: {h.lakcim.varos}");
            }

            // 20 és 50 fő közötti kurzusok (Range)
            Console.WriteLine("\n=== 20-50 FŐ KÖZÖTTI KURZUSOK ===");
            var rangeFilter = kurzusCollection.Find(k => k.max_fo >= 20 && k.max_fo <= 50).ToList();
            foreach (var k in rangeFilter)
            {
                Console.WriteLine($"{k.tipus} ({k.max_fo} fő)");
            }


            // ==========================================
            // 4. Feladat: AGGREGÁCIÓS PIPELINE
            // ==========================================

            // Városonkénti hallgatók száma (Group + Count)
            Console.WriteLine("\n=== HALLGATÓK SZÁMA VÁROSONKÉNT ===");
            var varosStat = hallgatoCollection.Aggregate()
                .Group(h => h.lakcim.varos, g => new
                {
                    Varos = g.Key,
                    Darab = g.Count()
                })
                .ToList();

            foreach (var r in varosStat)
            {
                Console.WriteLine($"Város: {r.Varos} | Hallgatók: {r.Darab} fő");
            }

            // Legnagyobb kapacitású kurzus nyelvenként (Sort + Group + First)
            Console.WriteLine("\n=== LEGNAGYOBB KAPACITÁSÚ KURZUS NYELVENKÉNT ===");
            var maxKapacitas = kurzusCollection.Aggregate()
                .SortByDescending(k => k.max_fo)
                .Group(k => k.nyelv, g => new
                {
                    Nyelv = g.Key,
                    LegnagyobbTipus = g.First().tipus,
                    MaxFo = g.First().max_fo
                })
                .ToList();

            foreach (var r in maxKapacitas)
            {
                Console.WriteLine($"Nyelv: {r.Nyelv} | Típus: {r.LegnagyobbTipus} ({r.MaxFo} fő)");
            }

            // Lookup: Oktatók és Tárgyak összekapcsolása az "oktat" gyűjteményen keresztül
            Console.WriteLine("\n=== OKTATÁSI KAPCSOLATOK (LOOKUP) ===");
            var oktatCollection = database.GetCollection<BsonDocument>("oktat");
            var lookupResult = oktatCollection.Aggregate()
                .Lookup("targy", "_targykod", "_targykod", "targy_adatok")
                .ToList();

            foreach (var r in lookupResult)
            {
                var oktatoId = r["_lid"].AsString;
                var targyAdatok = r["targy_adatok"].AsBsonArray;
                
                if(targyAdatok.Count > 0)
                {
                    var targyNev = targyAdatok[0]["nev"].AsString;
                    Console.WriteLine($"Oktató azonosító: {oktatoId} -> Oktatja: {targyNev}");
                }
            }

            Console.ReadLine();
        }
    }
}