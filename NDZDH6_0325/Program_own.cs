/*using System.Xml.Linq;

// Dokumentum beolvasása
XDocument xml = XDocument.Load("egyetem.xml");
XElement root = xml.Descendants("egyetemi_rendszer").First();

Console.WriteLine("--- Egyetemi adatbázis ---\n" + root);

// 1. Tárgyak szűrése kredit alapján
Console.WriteLine("\nLegalább 5 kredites tárgyak:");
var targyak = root.Descendants("targy")
    .Where(t => int.Parse(t.Descendants("kredit").First().Value) >= 5)
    .ToList();

targyak.ForEach(t => Console.WriteLine(" - " + t.Descendants("nev").First().Value));

// 2. Beiratkozási adatok lekérdezése (Hallgató és Egyetem join)
Console.WriteLine("\nBeiratkozások:");
var beiratkozasok = root.Descendants("beiratkozas")
    .Select(b => {
        var sId = b.Attribute("sid").Value;
        var hNev = root.Descendants("hallgato")
            .First(h => h.Attribute("sid").Value == sId)
            .Descendants("nev").First().Value;

        var omId = b.Attribute("om").Value;
        var eNev = root.Descendants("egyetem")
            .First(e => e.Attribute("om").Value == omId)
            .Descendants("nev").First().Value;

        return new { 
            Hallgato = hNev, 
            Egyetem = eNev, 
            Szak = b.Descendants("kepzes_neve").First().Value 
        };
    }).ToList();

beiratkozasok.ForEach(i => Console.WriteLine($"- {i.Hallgato} | {i.Egyetem} | {i.Szak}"));

// 3. Statisztika: Átlagos kreditérték
var atlagKredit = root.Descendants("targy")
    .Select(t => double.Parse(t.Descendants("kredit").First().Value))
    .Average();
Console.WriteLine($"\nÁtlagos kreditérték: {atlagKredit:F2}");

// 4. Adatok frissítése (Kredit növelés)
root.Descendants("targy").ToList().ForEach(t => {
    var krElem = t.Descendants("kredit").First();
    var ertek = int.Parse(krElem.Value) + 1;
    krElem.Value = ertek.ToString();
});
new XDocument(root).Save("egyetem_modositott.xml");

// 5. Kis létszámú kurzusok törlése
root.Descendants("kurzus")
    .Where(k => int.Parse(k.Descendants("max_fo").First().Value) < 30)
    .ToList()
    .ForEach(k => k.Remove());
new XDocument(root).Save("egyetem_torolt.xml");

// 6. Új XML struktúra generálása (Intézeti adatok)
XElement intezet = new XElement("intezet",
    new XElement("tanszek",
        new XAttribute("kod", "INF01"),
        new XElement("nev", "Szoftverfejlesztés"),
        new XElement("vezeto", "Kovács János"),
        new XElement("letszam", "12")
    )
);
new XDocument(intezet).Save("intezet.xml");*/