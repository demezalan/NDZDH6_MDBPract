using System.Xml.Linq;

// Adatok betöltése
XDocument doc = XDocument.Load("etterem.xml");
XElement gyoker = doc.Descendants("vendeglatas").First();

Console.WriteLine("--- Dokumentum tartalma ---\n" + gyoker);

// 1. Ötcsillagos éttermek szűrése
Console.WriteLine("\nÖtcsillagos éttermek:");
var otCsillagosok = gyoker.Descendants("etterem")
    .Where(e => e.Descendants("csillag").First().Value == "5")
    .ToList();

otCsillagosok.ForEach(e => Console.WriteLine(" - " + e.Descendants("nev").First().Value));

// 2. Összetett lekérdezés (Rendelések, vendégek és éttermek összekapcsolása)
Console.WriteLine("\nRendelési adatok:");
var rendelesLista = gyoker.Descendants("rendeles")
    .Select(r => {
        var vId = r.Attribute("e_v_v").Value;
        var vNev = gyoker.Descendants("vendeg")
            .First(v => v.Attribute("vkod").Value == vId)
            .Descendants("nev").First().Value;

        var eId = r.Attribute("e_v_e").Value;
        var eNev = gyoker.Descendants("etterem")
            .First(e => e.Attribute("ekod").Value == eId)
            .Descendants("nev").First().Value;

        return new { 
            Vendeg = vNev, 
            Etterem = eNev, 
            Etel = r.Descendants("etel").First().Value, 
            Ar = r.Descendants("osszeg").First().Value 
        };
    }).ToList();

rendelesLista.ForEach(item => Console.WriteLine($"- {item.Vendeg} ({item.Etterem}): {item.Etel}, {item.Ar} Ft"));

// 3. Átlagos összeg számítása
var avg = gyoker.Descendants("rendeles")
    .Select(r => double.Parse(r.Descendants("osszeg").First().Value))
    .Average();
Console.WriteLine($"\nÁtlagos rendelési érték: {avg:F0} Ft");

// 4. Értékek módosítása és mentése
gyoker.Descendants("rendeles").ToList().ForEach(r => {
    var arElem = r.Descendants("osszeg").First();
    var ujAr = double.Parse(arElem.Value) * 2;
    arElem.Value = ujAr.ToString();
});
new XDocument(gyoker).Save("etterem_modositott.xml");

// 5. Elemek törlése feltétel alapján
gyoker.Descendants("etterem")
    .Where(e => e.Descendants("csillag").First().Value == "3")
    .ToList()
    .ForEach(e => e.Remove());
new XDocument(gyoker).Save("etterem_torolt.xml");

// 6. Új XML struktúra létrehozása programból
XElement konyvtar = new XElement("konyvtar",
    new XElement("konyv",
        new XAttribute("isbn", "123456"),
        new XElement("cim", "LINQ alapok"),
        new XElement("szerzo", "Teszt Elek"),
        new XElement("ar", "3000")
    )
);
new XDocument(konyvtar).Save("konyvtar.xml");