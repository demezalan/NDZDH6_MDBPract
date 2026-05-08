from pymongo import MongoClient

# ==========================================
# 1. ADATBÁZIS KAPCSOLAT ÉS INICIALIZÁLÁS
# ==========================================
DB_URI = "mongodb://localhost:27017/"
kliens = MongoClient(DB_URI)

# A saját egyetemi adatbázisod használata
db = kliens["egyetemi_rendszer"]

# Kollekciók (táblák) definiálása
targy_koll = db["targyak"]
eloado_koll = db["eloadok"]

# Korábbi adatok törlése a tiszta futtatás érdekében
targy_koll.delete_many({})
eloado_koll.delete_many({})

# ==========================================
# 2. ADATOK BETÖLTÉSE (XML ADATOK ALAPJÁN)
# ==========================================
targyak_lista = [
    {
        "_id": "t1",
        "nev": "Adatbázisok",
        "kredit": 5,
        "kovetelmeny": "GEMAL342B"
    },
    {
        "_id": "t2",
        "nev": "Algoritmusok",
        "kredit": 4,
        "kovetelmeny": "GEMAL342A"
    },
    {
        "_id": "t3",
        "nev": "Operációs rendszerek",
        "kredit": 6,
        "kovetelmeny": "GEMAL342C"
    }
]

eloadok_lista = [
    {
        "_id": "l1",
        "nev": "Dr. Kovács Ágnes",
        "lakcim": "Budapest, Rákóczi út 10",
        "elerhetoseg": {"telefon": "+36 20 123-4567", "email": "kovacs.agnes@bme.hu"},
        "irodaszam": 324,
        "targykod": "t1"  # Idegen kulcs a t1 (Adatbázisok) tárgyhoz
    },
    {
        "_id": "l2",
        "nev": "Dr. Szabó Péter",
        "lakcim": "Debrecen, Kossuth Lajos utca 5",
        "elerhetoseg": {"telefon": "+36 30 987-6543", "email": "szabo.peter@de.edu"},
        "irodaszam": 215,
        "targykod": "t2"  # Idegen kulcs a t2 (Algoritmusok) tárgyhoz
    },
    {
        "_id": "l3",
        "nev": "Dr. Nagy Eszter",
        "lakcim": "Szeged, Rákóczi Ferenc utca 15",
        "elerhetoseg": {"telefon": "+36 70 555-1234", "email": "nagy.eszter@szeged.edu"},
        "irodaszam": 108,
        "targykod": "t3"  # Idegen kulcs a t3 (Operációs rendszerek) tárgyhoz
    }
]

# Feltöltés végrehajtása
targy_koll.insert_many(targyak_lista)
eloado_koll.insert_many(eloadok_lista)
print("Tárgyak és Előadók sikeresen feltöltve!\n")


# ==========================================
# 3. LEKÉRDEZÉSEK ÉS AGGREGÁCIÓK
# ==========================================

print("--- 2.a) Összes tárgy listája ---")
for targy in targy_koll.find():
    print(f"[{targy['_id']}] {targy['nev']} - {targy['kredit']} kredit")

print("\n--- 2.a) Összes előadó listája ---")
for eloado in eloado_koll.find():
    print(f"[{eloado['_id']}] {eloado['nev']} (Iroda: {eloado['irodaszam']})")

print("\n--- 2.b) Konkrét tárgy keresése (ID: t1) ---")
t1_adat = targy_koll.find_one({"_id": "t1"})
print(f"Találat: {t1_adat['nev']} | Kredit: {t1_adat['kredit']} | Követelmény: {t1_adat['kovetelmeny']}")

print("\n--- 2.b) Konkrét előadó keresése (ID: l2) ---")
l2_adat = eloado_koll.find_one({"_id": "l2"})
print(f"Találat: {l2_adat['nev']} | Cím: {l2_adat['lakcim']} | Email: {l2_adat['elerhetoseg']['email']}")

print("\n--- 2.c) Szűrés: Magas kreditértékű tárgyak (kredit >= 5) ---")
for targy in targy_koll.find({"kredit": {"$gte": 5}}):
    print(f"- {targy['nev']} ({targy['kredit']} kredit)")

print("\n--- 2.d) Tárgyak átlagos kreditértéke (Aggregáció) ---")
atlag_kredit_lekerdezes = [
    {
        "$group": {
            "_id": None,
            "atlagKredit": {"$avg": "$kredit"}
        }
    }
]
eredmeny = list(targy_koll.aggregate(atlag_kredit_lekerdezes))
if eredmeny:
    print(f"Kiszámolt átlag: {eredmeny[0]['atlagKredit']:.2f} kredit")

print("\n--- 2.e) Előadók és az általuk oktatott tárgy neve (Lookup / JOIN) ---")
join_lekerdezes = [
    {
        "$lookup": {
            "from": "targyak",
            "localField": "targykod",
            "foreignField": "_id",
            "as": "oktatott_targy"
        }
    }
]
for sor in eloado_koll.aggregate(join_lekerdezes):
    targy_nev = sor["oktatott_targy"][0]["nev"] if sor.get("oktatott_targy") else "Nincs adat"
    print(f"Előadó: {sor['nev']:<18} -> Oktatja: {targy_nev}")


# ==========================================
# 4. ADATMÓDOSÍTÁS ÉS TÖRLÉS
# ==========================================

print("\n--- 3.a) Módosítás: Dr. Kovács Ágnes (l1) új irodát kap (324 -> 400) ---")
eloado_koll.update_one({"_id": "l1"}, {"$set": {"irodaszam": 400}})
frissitett_l1 = eloado_koll.find_one({"_id": "l1"})
print(f"Sikeres módosítás! {frissitett_l1['nev']} új irodaszáma: {frissitett_l1['irodaszam']}.")

print("\n--- 4.a) Törlés: Dr. Nagy Eszter (l3) törlése a rendszerből ---")
eloado_koll.delete_one({"_id": "l3"})
print("Az l3 azonosítójú előadó törölve az adatbázisból.")

print("\n--- 4.b) Feltételes törlés: 5 kreditnél kisebb tárgyak eltávolítása ---")
torles_eredmeny = targy_koll.delete_many({"kredit": {"$lt": 5}})
print(f"Összesen {torles_eredmeny.deleted_count} db tárgy lett törölve.")

print("\n--- Végleges adatbázis állapot (Megmaradt tárgyak) ---")
for targy in targy_koll.find():
    print(f"- {targy['nev']} ({targy['kredit']} kredit)")