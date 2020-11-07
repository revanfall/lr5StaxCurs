import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SAXException, IOException, XMLStreamException {

        String fileName = "src/main/resources/lr5xml.xml";
        List<CdAlbum> Catalog = XMLStaxCursorParser.parseXMLfile(fileName);
        for (CdAlbum album : Catalog) {
            System.out.println(album.toString());
        }
        XMLStaxCursorParser.validateDTD(fileName);
     //   XMLStaxCursorParser.validateXSD(fileName);
    }
}

