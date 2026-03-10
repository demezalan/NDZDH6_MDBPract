package ndzdh6;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DomReadNDZDH6Own {
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        File xmlFile = new File("NDZDH6_XDM1.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        //-------- egyetemek --------------
        NodeList nList = doc.getElementsByTagName("egyetem");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("Current element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String om = elem.getAttribute("om");
                String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                String varos = elem.getElementsByTagName("varos").item(0).getTextContent();
                String utca = elem.getElementsByTagName("utca").item(0).getTextContent();
                String hazszam = elem.getElementsByTagName("hazszam").item(0).getTextContent();
                String telefon = elem.getElementsByTagName("telefon").item(0).getTextContent();
                String email = elem.getElementsByTagName("email").item(0).getTextContent();
                String fax = elem.getElementsByTagName("fax").item(0).getTextContent();

                String adr = varos + ", " + utca + " " + hazszam + ".";

                System.out.println("OM azonosító: " + om);
                System.out.println("Név: " + nev);
                System.out.println("Cím: " + adr);
                System.out.println("Elérhetőség: Tel: " + telefon + ", Email: " + email + ", Fax: " + fax + "\n");
            }
        }


        // --------- eloadok ------------
        nList = doc.getElementsByTagName("eloado");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("Current element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String lid = elem.getAttribute("lid");
                String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                String lakcim = elem.getElementsByTagName("lakcim").item(0).getTextContent();
                String telefon = elem.getElementsByTagName("telefon").item(0).getTextContent();
                String email = elem.getElementsByTagName("email").item(0).getTextContent();
                String irodaszam = elem.getElementsByTagName("irodaszam").item(0).getTextContent();

                System.out.println("Előadó ID: " + lid);
                System.out.println("Név: " + nev);
                System.out.println("Lakcím: " + lakcim);
                System.out.println("Elérhetőség: Tel: " + telefon + ", Email: " + email);
                System.out.println("Irodaszám: " + irodaszam + "\n");
            }
        }


        // --------- hallgatok ------------
        nList = doc.getElementsByTagName("hallgato");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("Current element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String sid = elem.getAttribute("sid");
                String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                String varos = elem.getElementsByTagName("varos").item(0).getTextContent();
                String utca = elem.getElementsByTagName("utca").item(0).getTextContent();
                String hazszam = elem.getElementsByTagName("hazszam").item(0).getTextContent();
                String email = elem.getElementsByTagName("email").item(0).getTextContent();
                String szuletesi_ido = elem.getElementsByTagName("szuletesi_ido").item(0).getTextContent();

                String adr = varos + ", " + utca + " " + hazszam + ".";

                System.out.println("Hallgató ID: " + sid);
                System.out.println("Név: " + nev);
                System.out.println("Cím: " + adr);
                System.out.println("Email: " + email);
                System.out.println("Születési idő: " + szuletesi_ido + "\n");
            }
        }


        // --------- kurzusok ------------
        nList = doc.getElementsByTagName("kurzus");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("Current element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String kurzuskod = elem.getAttribute("kurzuskod");
                String nyelv = elem.getElementsByTagName("nyelv").item(0).getTextContent();
                String tipus = elem.getElementsByTagName("tipus").item(0).getTextContent();
                String idotartam = elem.getElementsByTagName("idotartam").item(0).getTextContent();
                String max_fo = elem.getElementsByTagName("max_fo").item(0).getTextContent();

                System.out.println("Kurzuskód: " + kurzuskod);
                System.out.println("Nyelv: " + nyelv);
                System.out.println("Típus: " + tipus);
                System.out.println("Időtartam: " + idotartam);
                System.out.println("Max fő: " + max_fo + "\n");
            }
        }


        // --------- targyak ------------
        nList = doc.getElementsByTagName("targy");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("Current element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String targykod = elem.getAttribute("targykod");
                String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                String kredit = elem.getElementsByTagName("kredit").item(0).getTextContent();
                String kovetelmeny = elem.getElementsByTagName("kovetelmeny").item(0).getTextContent();

                System.out.println("Tárgykód: " + targykod);
                System.out.println("Név: " + nev);
                System.out.println("Kredit: " + kredit);
                System.out.println("Követelmény: " + kovetelmeny + "\n");
            }
        }


        // --------- szerzodesek ------------
        nList = doc.getElementsByTagName("szerzodes");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("Current element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String lid = elem.getAttribute("lid");
                String om = elem.getAttribute("om");
                String statusz = elem.getElementsByTagName("statusz").item(0).getTextContent();
                String szervezeti_egyseg = elem.getElementsByTagName("szervezeti_egyseg").item(0).getTextContent();

                System.out.println("Előadó ID: " + lid + " | egyetem OM: " + om);
                System.out.println("Státusz: " + statusz);
                System.out.println("Kar: " + szervezeti_egyseg + "\n");
            }
        }


        // --------- beiratkozasok ------------
        nList = doc.getElementsByTagName("beiratkozas");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("Current element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String sid = elem.getAttribute("sid");
                String om = elem.getAttribute("om");
                String kepzes_neve = elem.getElementsByTagName("kepzes_neve").item(0).getTextContent();
                String statusz = elem.getElementsByTagName("statusz").item(0).getTextContent();
                String tagozat = elem.getElementsByTagName("tagozat").item(0).getTextContent();
                String szervezeti_egyseg = elem.getElementsByTagName("szervezeti_egyseg").item(0).getTextContent();

                System.out.println("Hallgató ID: " + sid + " | egyetem OM: " + om);
                System.out.println("Képzés: " + kepzes_neve);
                System.out.println("Tagozat" + tagozat);
                System.out.println("Státusz: " + statusz);
                System.out.println("Kar: " + szervezeti_egyseg + "\n");
            }
        }


        // --------- oktatasok (kapcsolo) ------------
        nList = doc.getElementsByTagName("oktat");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("Current element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String lid = elem.getAttribute("lid");
                String targykod = elem.getAttribute("targykod");

                System.out.println("Előadó ID: " + lid + " | tárgykód: " + targykod + "\n");
            }
        }


        // --------- resztvevok (kapcsolo) ------------
        nList = doc.getElementsByTagName("reszt_vesz");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("Current element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String sid = elem.getAttribute("sid");
                String kurzuskod = elem.getAttribute("kurzuskod");

                System.out.println("Hallgató ID: " + sid + " | kurzuskód: " + kurzuskod + "\n");
            }
        }


        // --------- eloadasok ------------
        nList = doc.getElementsByTagName("eloadas");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("Current element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String targykod = elem.getAttribute("targykod");
                String kurzuskod = elem.getAttribute("kurzuskod");
                String idopont = elem.getElementsByTagName("idopont").item(0).getTextContent();

                System.out.println("Tárgykód: " + targykod + " | kurzuskód: " + kurzuskod);
                System.out.println("Időpont: " + idopont + "\n");
            }
        }
    }
}