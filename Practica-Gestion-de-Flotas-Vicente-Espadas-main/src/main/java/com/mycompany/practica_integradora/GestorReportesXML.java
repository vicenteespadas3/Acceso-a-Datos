package com.mycompany.practica_integradora;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.ArrayList;

/**
 * Clase encargada de generar y leer informes en formato XML.
 * Utiliza DOM para la creación estructurada del árbol en memoria y su posterior volcado,
 * y SAX para la lectura secuencial y extracción de categorías.
 *
 * @author vicen
 */
public class GestorReportesXML {

    /** Ruta base donde se guardará y leerá el informe XML. */
    private final String rutaReporte = "datos/reports/informe_flota.xml";

    /**
     * Genera un archivo XML estructurado a partir de la lista de vehículos en memoria.
     * Construye el árbol DOM completo y lo transforma a un archivo físico.
     *
     * @param listaVehiculos Colección ArrayList de vehículos a procesar.
     */
    public void generarInformeDOM(ArrayList<Vehiculo> listaVehiculos){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.newDocument();

            Element flota = d.createElement("Flota");
            d.appendChild(flota);

            for(Vehiculo v : listaVehiculos){
                Element vehiculo = d.createElement("Vehiculo");
                flota.appendChild(vehiculo);

                Element matricula = d.createElement("Matricula");
                matricula.setTextContent(v.getMatricula());
                vehiculo.appendChild(matricula);

                Element marca = d.createElement("Marca");
                marca.setTextContent(String.valueOf(v.getMarca()));
                vehiculo.appendChild(marca);

                Element anio = d.createElement("Anio");
                anio.setTextContent(String.valueOf(v.getAnio()));
                vehiculo.appendChild(anio);

                Element kilometraje = d.createElement("Kilometraje");
                kilometraje.setTextContent(String.valueOf(v.getKilometraje()));
                vehiculo.appendChild(kilometraje);

                Element catMantenimiento = d.createElement("CategoriaMantenimiento");
                catMantenimiento.setTextContent(String.valueOf(v.getCatMantenimiento()));
                vehiculo.appendChild(catMantenimiento);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource source = new DOMSource(d);
            StreamResult result = new StreamResult(rutaReporte);

            t.transform(source, result);

        } catch (ParserConfigurationException | TransformerFactoryConfigurationError | TransformerException e) {
            System.out.println("Error al generar el informe XML: " + e.getMessage());
        }
    }

    /**
     * Lee el archivo XML de forma secuencial utilizando SAX.
     * Emplea el ManejadorGenerico para extraer las categorías de mantenimiento sin duplicados.
     */
    public void leerAuditoriaSAX(){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ManejadorGenerico manejador = new ManejadorGenerico();
            parser.parse(rutaReporte, manejador);

            System.out.println("Categorías de mantenimiento registradas (sin duplicados): " + manejador.getCategorias());

        } catch(Exception e) {
            System.out.println("Error al leer Auditoria: " + e.getMessage());
        }
    }
}