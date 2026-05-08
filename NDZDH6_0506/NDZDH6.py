from pymongo import MongoClient

# ==========================================
# 1. ADATBÁZIS KAPCSOLAT ÉS INICIALIZÁLÁS
# ==========================================
DB_URI = "mongodb://localhost:27017/"
kliens = MongoClient(DB_URI)
db = kliens["vendeglatas_db"]

# Kollekciók (táblák) definiálása
etterem_koll = db["etterem"]
szakacs_koll = db["foszakacs"]

# Korábbi adatok törlése a tiszta kezdéshez
etterem_koll.delete_many({})
szakacs_koll.delete_many({})

# ==========================================
# 2. ADATOK BETÖLTÉSE (SEED)
# ==========================================
etterem_lista = [
    {
        "_id": "e1",
        "nev": "Aranyhal Étterem",
        "cim": {"varos": "Miskolc", "utca": "Széchenyi u.", "hazszam": 107},
        "csillag": 3
    },
    {
        "_id": "e2",
        "nev": "Aranyhal Étterem",
        "cim": {"varos": "Miskolc", "utca": "Széchenyi u.", "hazszam": 107},
        "csillag": 4
    },
    {
        "_id": "e3",
        "nev": "Creppy Palacsintaház Étterem",
        "cim": {"varos": "Miskolc", "utca": "Mélyvölgy utca", "hazszam": 15},
        "csillag": 5
    }
]

szakacs_lista = [
    {
        "_id": "f1",
        "e_f": "e1",
        "nev": "Fő István",
        "eletkor": 45,
        "vegzettseg": ["Szakközépiskola", "Főiskola"]
    },
    {
        "_id": "f2",
        "e_f": "e2",
        "nev": "Kovács János",
        "eletkor": 38,
        "vegzettseg": ["Szakközépiskola", "Főiskola"]
    },
    {
        "_id": "f3",
        "e_f": "e3",
        "nev": "Nemes Géza",
        "eletkor": 28,
        "vegzettseg": ["Főiskola"]
    }
]

# Feltöltés végrehajtása
etterem_koll.insert_many(etterem_lista)
szakacs_koll.insert_many(szakacs_lista)
print("Adatbázis sikeresen feltöltve a kezdőadatokkal!\n")


# ==========================================
# 3. LEKÉRDEZÉSEK ÉS AGGREGÁCIÓK
# ==========================================

print("--- Összes étterem listája ---")
for e in etterem_koll.find():
    print(f"[{e['_id']}] {e['nev']} - {e['csillag']} csillag")

print("\n--- Összes főszakács listája ---")
for sz in szakacs_koll.find():
    print(f"[{sz['_id']}] {sz['nev']} ({sz['eletkor']} éves)")

print("\n--- Konkrét étterem keresése (ID: e2) ---")
e2_adat = etterem_koll.find_one({"_id": "e2"})
print(f"Találat: {e2_adat['nev']} | Cím: {e2_adat['cim']['varos']}, {e2_adat['cim']['utca']} {e2_adat['cim']['hazszam']}.")

print("\n--- Maximum 4 csillagos éttermek ---")
for e in etterem_koll.find({"csillag": {"$lte": 4}}):
    print(f"- {e['nev']} ({e['csillag']}*)")

print("\n--- Főszakácsok átlagéletkora ---")
atlag_kor_lekerdezes = [
    {
        "$group": {
            "_id": None,
            "atlag": {"$avg": "$eletkor"}
        }
    }
]
eredmeny = list(szakacs_koll.aggregate(atlag_kor_lekerdezes))
if eredmeny:
    print(f"Kiszámolt átlag: {eredmeny[0]['atlag']:.1f} év")

print("\n--- Szakközépiskolát végzett szakácsok és munkahelyük (JOIN) ---")
join_lekerdezes = [
    {"$match": {"vegzettseg": "Szakközépiskola"}},
    {
        "$lookup": {
            "from": "etterem",
            "localField": "e_f",
            "foreignField": "_id",
            "as": "munkahely"
        }
    }
]
for sor in szakacs_koll.aggregate(join_lekerdezes):
    etterem_nev = sor["munkahely"][0]["nev"] if sor.get("munkahely") else "Ismeretlen"
    print(f"Szakács: {sor['nev']} -> Munkahely: {etterem_nev}")


# ==========================================
# 4. ADATMÓDOSÍTÁS ÉS TÖRLÉS
# ==========================================

print("\n--- Értékelés módosítása: e1 étterem 3-ról 4 csillagra ---")
etterem_koll.update_one({"_id": "e1"}, {"$set": {"csillag": 4}})
frissitett_e1 = etterem_koll.find_one({"_id": "e1"})
print(f"Sikeres módosítás! Az e1 étterem új értékelése: {frissitett_e1['csillag']} csillag.")

print("\n--- Főszakács törlése (ID: f2) ---")
szakacs_koll.delete_one({"_id": "f2"})
print("Az f2 azonosítójú szakács törölve az adatbázisból.")

print("\n--- Fiatal (30 év alatti) főszakácsok csoportos törlése ---")
torles_eredmeny = szakacs_koll.delete_many({"eletkor": {"$lt": 30}})
print(f"Összesen {torles_eredmeny.deleted_count} db szakács lett eltávolítva.")

print("\n--- Végleges adatbázis állapot (Megmaradt szakácsok) ---")
for sz in szakacs_koll.find():
    print(f"- {sz['nev']}")