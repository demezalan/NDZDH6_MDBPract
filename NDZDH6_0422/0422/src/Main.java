import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {


        // --- 0.c feladat: JSON fa betöltése ---
        ObjectMapper m = new ObjectMapper();
        JsonNode root = m.readTree(new File("JSON.json"));
        JsonNode vendeglatas = root.get("vendeglatas");

        JsonNode foszakacsok = vendeglatas.get("foszakacs");
        JsonNode szakacsok = vendeglatas.get("szakacs");
        JsonNode ettermek = vendeglatas.get("etterem");
        JsonNode rendelesek = vendeglatas.get("rendeles");
        JsonNode vendegek = vendeglatas.get("vendeg");


        // --- 0.d feladat: JSON validálás ---
        JsonNode schemaNode = m.readTree(new File("JSON_SCHEMA.json"));
        JsonSchema schema = JsonSchemaFactory
                .getInstance(SpecVersion.VersionFlag.V4)
                .getSchema(schemaNode);

        Set<ValidationMessage> errors = schema.validate(root);
        if (errors.isEmpty()) {
            System.out.println("Valid JSON\n");
        } else {
            System.out.println("Hibás JSON:");
            errors.forEach(e -> System.out.println(e.getMessage()));
        }


        // --- 1. feladat: főszakácsok adatai ---
        System.out.println("=== Főszakácsok adatai ===");
        if (foszakacsok != null && foszakacsok.isArray()) {
            for (JsonNode szakacs : foszakacsok) {
                String nev = szakacs.get("nev").asText();
                String kor = szakacs.get("eletkor").asText();
                String iskola = szakacs.get("vegzettseg").asText();
                System.out.println("Név: " + nev + " | Kor: " + kor + " | Végzettség: " + iskola);
            }
        }

        // --- 2. feladat: éttermek és szakácsaik ---
        System.out.println("\n=== ÉTTERMEK + SZAKÁCSOK ===");
        for (JsonNode etterem : ettermek) {
            String eKod = etterem.get("_ekod").asText();
            String eNev = etterem.get("nev").asText();
            System.out.println("Étterem: " + eNev + " [" + eKod + "]");
            System.out.println("----------------");

            for (JsonNode szakacs : szakacsok) {
                if (szakacs.get("_e_sz").asText().equals(eKod)) {
                    System.out.println(" - " + szakacs.get("nev").asText() + " (" + szakacs.get("reszleg").asText() + ")");
                }
            }
        }


        // --- 3. feladat: átlagos rendelési érték ---
        System.out.println("\n=== Átlagos rendelési érték ===");
        double osszeg = 0;
        int db = 0;
        for (JsonNode r : rendelesek) {
            osszeg += r.get("osszeg").asDouble();
            db++;
        }
        if (db > 0) {
            System.out.println("AVG: " + (osszeg / db) + " Ft");
        }

        // --- 4. feladat: 5 csillagos éttermek ---
        System.out.println("\n=== 5 csillagos éttermek ===");
        for (JsonNode e : ettermek) {
            if (e.get("csillag").asInt() == 5) {
                System.out.println(" - " + e.get("nev").asText());
            }
        }


        // --- 5. feladat: ki, hol, mit rendelt ---
        System.out.println("\n=== Ki, hol, mit rendelt (JOIN) ===");
        for (JsonNode r : rendelesek) {
            String vKod = r.get("_vkod").asText();
            String eKod = r.get("_ekod").asText();
            String vNev = "";
            String eNev = "";

            for (JsonNode v : vendegek) {
                if (v.get("_vkod").asText().equals(vKod)) vNev = v.get("nev").asText();
            }
            for (JsonNode e : ettermek) {
                if (e.get("_ekod").asText().equals(eKod)) eNev = e.get("nev").asText();
            }
            System.out.println(vNev + " ezt rendelte: " + r.get("etel").asText() + " (" + eNev + ")");
        }


        // --- 6. feladat: JSON adatok manipulációja ---
        System.out.println("\n=== JSON adatok manipulációja ===");
        for (JsonNode e : ettermek) {
            ObjectNode obj = (ObjectNode) e;
            obj.put("ellenorzott", true);
            obj.remove("csillag");
        }
        System.out.println("Sikeresen hozzáadva az 'ellenorzott' mező, és törölve a 'csillag'!");

        // --- 7. Feladat: VIP Vendég (legtöbbet fizető) ---
        System.out.println("\n=== VIP Vendég (legtöbbet költő) ===");
        Map<String, Double> koltesek = new HashMap<>();
        for (JsonNode r : rendelesek) {
            String vKod = r.get("_vkod").asText();
            double ar = r.get("osszeg").asDouble();
            koltesek.put(vKod, koltesek.getOrDefault(vKod, 0.0) + ar);
        }

        String maxVkod = "";
        double maxOsszeg = 0;
        for (Map.Entry<String, Double> entry : koltesek.entrySet()) {
            if (entry.getValue() > maxOsszeg) {
                maxOsszeg = entry.getValue();
                maxVkod = entry.getKey();
            }
        }

        String vipNev = "";
        for (JsonNode v : vendegek) {
            if (v.get("_vkod").asText().equals(maxVkod)) {
                vipNev = v.get("nev").asText();
                break;
            }
        }
        System.out.println("VIP guest: " + vipNev + " (Total spent: " + maxOsszeg + " HUF)");

        // --- 8. Feladat: Új JSON fájl készítése és mentése ---
        System.out.println("\n=== Új JSON fájl készítése és mentése ===");
        ArrayNode ujLista = m.createArrayNode();

        for (JsonNode e : ettermek) {
            String eKod = e.get("_ekod").asText();
            double bevetel = 0;

            for (JsonNode r : rendelesek) {
                if (r.get("_ekod").asText().equals(eKod)) {
                    bevetel += r.get("osszeg").asDouble();
                }
            }

            ObjectNode csomopont = m.createObjectNode();
            csomopont.put("etterem_nev", e.get("nev").asText());
            csomopont.put("osszes_bevetel", bevetel);
            ujLista.add(csomopont);
        }

        m.writerWithDefaultPrettyPrinter().writeValue(new File("uj_bevetel.json"), ujLista);
        System.out.println("Fájl kiírva: uj_bevetel.json");


    }
}