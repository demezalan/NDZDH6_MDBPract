import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.File;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            // ObjectMapper inicializálása és a JSON fájl beolvasása
            ObjectMapper m = new ObjectMapper();
            JsonNode root = m.readTree(new File("JSON.json"));
            JsonNode egyetemiRendszer = root.get("egyetemi_rendszer");

            // Különböző listák (tömbök) kinyerése a memóriába
            JsonNode kurzusok = egyetemiRendszer.get("kurzus");
            JsonNode targyak = egyetemiRendszer.get("targy");
            JsonNode oktatok = egyetemiRendszer.get("eloado");
            JsonNode oktatKapcsolo = egyetemiRendszer.get("oktat");
            JsonNode hallgatok = egyetemiRendszer.get("hallgato");
            JsonNode beiratkozasok = egyetemiRendszer.get("beiratkozas");

            System.out.println("=== 2. FELADAT: JSON VALIDÁLÁSA ===");
            JsonNode schemaNode = m.readTree(new File("JSON_SCHEMA.json"));
            JsonSchema schema = JsonSchemaFactory
                    .getInstance(SpecVersion.VersionFlag.V4)
                    .getSchema(schemaNode);

            Set<ValidationMessage> errors = schema.validate(root);
            if (errors.isEmpty()) {
                System.out.println("Sikeres validáció! A JSON fájl megfelel a sémának.\n");
            } else {
                System.out.println("Hibás JSON:");
                errors.forEach(e -> System.out.println(e.getMessage()));
                return;
            }

            System.out.println("=== 3. FELADAT: 5 EGYEDI LEKÉRDEZÉS ===");

            // --- 1. Lekérdezés (szűrés): 15 főnél nagyobb kapacitású kurzusok ---
            System.out.println("\n1. Szűrés: 15 főnél nagyobb kapacitású kurzusok");
            for (JsonNode k : kurzusok) {
                if (k.get("max_fo").asInt() > 15) {
                    System.out.println(" - " + k.get("tipus").asText() + " (" + k.get("nyelv").asText() + "), Max fő: " + k.get("max_fo").asText());
                }
            }

            // --- 2. Lekérdezés (aggregáció): Tárgyak átlagos kreditértéke ---
            System.out.println("\n2. Aggregáció: Átlagos kreditérték a tárgyaknál");
            double osszKredit = 0;
            int targyDb = 0;
            for (JsonNode t : targyak) {
                osszKredit += t.get("kredit").asDouble();
                targyDb++;
            }
            if (targyDb > 0) {
                System.out.println(" - Az egyetemen a tárgyak átlagos kreditértéke: " + (osszKredit / targyDb));
            }

            // --- 3. Lekérdezés (JOIN): Ki mit tanít ---
            System.out.println("\n3. Összekapcsolás (JOIN): Oktatók és Tárgyak");
            for (JsonNode o : oktatKapcsolo) {
                String lid = o.get("_lid").asText();
                String targykod = o.get("_targykod").asText();
                
                String oktatoNev = "Ismeretlen oktató";
                String targyNev = "Ismeretlen tárgy";

                // Oktató nevének megkeresése
                for (JsonNode eloado : oktatok) {
                    if (eloado.get("_lid").asText().equals(lid)) {
                        oktatoNev = eloado.get("nev").asText();
                        break;
                    }
                }

                // Tárgy nevének megkeresése
                for (JsonNode t : targyak) {
                    if (t.get("_targykod").asText().equals(targykod)) {
                        targyNev = t.get("nev").asText();
                        break;
                    }
                }
                System.out.println(" - " + oktatoNev + " oktatja a következőt: " + targyNev);
            }

            // --- 4. Lekérdezés (beágyazott objektum szűrése): Kazincbarcikai hallgatók ---
            System.out.println("\n4. Beágyazott objektum szűrése: Kazincbarcikán élő hallgatók");
            for (JsonNode h : hallgatok) {
                if (h.get("lakcim").get("varos").asText().equals("Kazincbarcika")) {
                    System.out.println(" - " + h.get("nev").asText() + " (" + h.get("email").asText() + ")");
                }
            }

            // --- 5. Lekérdezés (adatmódosítás): Beiratkozások módosítása ---
            System.out.println("\n5. Adatmódosítás: Beiratkozás manipulálása (teljesitve = false hozzáadása, statusz törlése)");
            for (JsonNode b : beiratkozasok) {
                ObjectNode obj = (ObjectNode) b;
                obj.put("teljesitve", false); // Új boolean mező hozzáadása
                obj.remove("statusz");        // Régi mező törlése
            }
            System.out.println(" - Módosítás sikeres! Az első módosított beiratkozás tartalma:");
            System.out.println(beiratkozasok.get(0).toPrettyString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}