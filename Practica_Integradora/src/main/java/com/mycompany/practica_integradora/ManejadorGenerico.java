package com.mycompany.practica_integradora;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashSet;

/**
 * Manejador de eventos SAX para procesar secuencialmente el archivo XML.
 * Se encarga de interceptar la etiqueta CategoriaMantenimiento y almacenar
 * sus valores únicos en un conjunto Set.
 *
 * @author vicen
 */
public class ManejadorGenerico extends DefaultHandler {

    /** Conjunto que almacena las categorías únicas leídas del XML garantizando no duplicados. */
    private HashSet<String> categorias = new HashSet<>();

    /** Bandera de control para saber si el cursor de lectura está dentro de la etiqueta objetivo. */
    private boolean leyendoCategoria = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if("CategoriaMantenimiento".equals(qName)){
            leyendoCategoria = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(leyendoCategoria){
            String texto = new String(ch, start, length).trim();
            if (!texto.isEmpty()) {
                categorias.add(texto);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("CategoriaMantenimiento".equals(qName)){
            leyendoCategoria = false;
        }
    }

    /**
     * Obtiene el listado de categorías procesadas.
     *
     * @return HashSet con los textos de las categorías.
     */
    public HashSet<String> getCategorias() {
        return categorias;
    }
}