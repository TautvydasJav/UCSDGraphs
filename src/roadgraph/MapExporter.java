/**
 * 
 */
package roadgraph;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import geography.GeographicPoint;

/**
 * @author Jure Koren
 * export route from MapGraph to xml (gpx) route file
 */
public class MapExporter {

	/** Return xml gpx string from route
	 * 
	 * @param route List of geographic points for route
	 * @return string from route
	 */	
	private DOMSource exportToGpx(String name, List<GeographicPoint> route) {
		
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            // create root element
            Element rootElement =
                doc.createElementNS("http://www.topografix.com/GPX/1/1", "gpx");
            doc.appendChild(rootElement);
 
            // append metadata
            rootElement.appendChild(getMetaDataNode(doc, name) );
            
            // append rte - route
            rootElement.appendChild(getRteNode(doc, name, route) );
            
            DOMSource source = new DOMSource(doc);
 
            return source;
 
        } catch (Exception e) {
            e.printStackTrace();
    		return null;            
        }
	}
	
	/** Write xml gpx string from route
	 * 
	 * @param name for route
	 * @param route List of geographic points for route
	 * @param filename for writing
	 * @return string from route
	 */	
	public void exportToGpxFile(String name, List<GeographicPoint> route, String filename) {
		
		// get dom
		DOMSource source = exportToGpx(name, route);
		
        try {
	        //for output to file, console
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        
	        //for pretty print
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");            
	        
	        //write to console or file
	        StreamResult console = new StreamResult(System.out);
	        StreamResult file = new StreamResult(new File(filename));
	
	        //write data
	        transformer.transform(source, console);
	        transformer.transform(source, file);
	        System.out.println("exportToGpxFile: Saved to " + filename);		
        
        } catch (Exception e) {
            e.printStackTrace();          
        }
	}

    
    
	/** Return xml node for route
	 * 
	 * @param doc xml document
	 * @param name name for route
	 * @return xml node
	 */		
    private static Node getMetaDataNode(Document doc, String name) {
        Element metadata = doc.createElement("metadata");
        
        //create name element
        metadata.appendChild(getNewTextNode(doc, metadata, "name", name));
        
        return metadata;
    }	  
    
	/** Return xml node for metadata
	 * 
	 * @param doc xml document
	 * @param route List of geographic points for route
	 * @return xml node
	 */		
    private static Node getRteNode(Document doc, String name, List<GeographicPoint> route) {
        Element rte = doc.createElement("rte");
        
        // create name element
        rte.appendChild(getNewTextNode(doc, rte, "name", name));
        
        // add route elements
        int i = 0;
        for(GeographicPoint point: route) {
        	i++;
        	// for now we assume all elevations are 0 and auto generate the name
        	rte.appendChild(getRteNodeElement(doc, rte, point, "Position " + i, 0.0 ));
        }
        
        return rte;
    }	    
    
	/** Return xml node for metadata
	 * 
	 * @param doc xml document
	 * @param route List of geographic points for route
	 * @return xml node
	 */		
    private static Node getRteNodeElement(Document doc, Element rte, GeographicPoint point, String name, Double elevation ) {
        Element rtept = doc.createElement("rtept");
        
        // set lon & lat
        rtept.setAttribute("lon", Double.toString(point.x) );
        rtept.setAttribute("lat", Double.toString(point.y) );
        
        // elevation
        rtept.appendChild(getNewTextNode(doc, rtept, "ele", Double.toString(elevation) ));
        
        // name
        rtept.appendChild(getNewTextNode(doc, rtept, "name", name ));
        
        return rtept;
    }    
    
    
	/** Return xml node with simple string element
	 * 
	 * @param name for new text node
	 * @param value for new text node 
	 * @return xml node
	 */	
    private static Node getNewTextNode(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }    
	
}