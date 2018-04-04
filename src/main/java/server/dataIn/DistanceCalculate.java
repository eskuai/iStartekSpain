
package server.dataIn;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class DistanceCalculate {

	public static LineString generateLine(Coordinate[] coords) {
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

		// Coordinate[] coords = new Coordinate[] {new Coordinate(0, 2), new
		// Coordinate(2, 0), new Coordinate(8, 6) };

		LineString line = geometryFactory.createLineString(coords);
		return line;
	}

	private static Geometry buffer(Geometry geom, double dist) {

		Geometry buffer = geom.buffer(dist);

		return buffer;

	}

	public static void crossPoligon(Geometry polygon, LineString line) {
		Geometry intersection = polygon.intersection(line);
		System.out.println("distance -> " + polygon.getEnvelope().distance(line));
		System.out.println("salida-> " + intersection.isEmpty());
		System.out.println("salida-> " + intersection.getNumPoints());
	}

	public static void main(String[] args) {
		Coordinate[] coords = new Coordinate[] { new Coordinate(-3.688488006591797, 40.40702606373308),
				new Coordinate(-3.6877584457397456, 40.406764637084855),
				new Coordinate(-3.686685562133789, 40.4065032094212),
				new Coordinate(-3.6846256256103516, 40.40653588793468),
				new Coordinate(-3.6828231811523438, 40.40689535053587),
				new Coordinate(-3.680891990661621, 40.407287489365885),
				new Coordinate(-3.6792612075805664, 40.407581591989015),
				new Coordinate(-3.6786174774169917, 40.40777765969047) };

		Coordinate[] coords2 = new Coordinate[] { new Coordinate(-3.688509464263916, 40.40710775935241),
				new Coordinate(-3.6881232261657715, 40.40678097628011),
				new Coordinate(-3.687136173248291, 40.4065032094212),
				new Coordinate(-3.686535358428955, 40.40653588793468),
				new Coordinate(-3.685977458953858, 40.40633981661579),
				new Coordinate(-3.6849904060363765, 40.406437852346635),
				new Coordinate(-3.684110641479492, 40.40665026260694),
				new Coordinate(-3.6830592155456547, 40.40668294104902) };

		Coordinate[] coords3 = new Coordinate[] { new Coordinate(-3.688509464263916, 40.40710775935241),
				new Coordinate(-3.6881232261657715, 40.40678097628011),
				new Coordinate(-3.687136173248291, 40.4065032094212),
				new Coordinate(-3.686535358428955, 40.40653588793468),
				new Coordinate(-3.685977458953858, 40.40633981661579),
				new Coordinate(-3.6849904060363765, 40.406437852346635),
				new Coordinate(-3.684110641479492, 40.40665026260694),
				new Coordinate(-3.6830592155456547, 40.40668294104902),
				new Coordinate(-3.682007789611816, 40.40707508111658),
				new Coordinate(-3.681793212890625, 40.40627445938256),
				new Coordinate(-3.681578636169433, 40.40553918613568),
				new Coordinate(-3.68117094039917, 40.405604544082884),
				new Coordinate(-3.6805915832519527, 40.40645419162122),
				new Coordinate(-3.680269718170166, 40.406862672196915),
				new Coordinate(-3.680055141448974, 40.40723847213706),
				new Coordinate(-3.6797118186950684, 40.40745087987071),
				new Coordinate(-3.6790251731872554, 40.40756525298811),
				new Coordinate(-3.67866039276123, 40.40769596488429) };

		Coordinate[] coords4 = new Coordinate[] { new Coordinate(-3.682007789611816, 40.40707508111658),
				new Coordinate(-3.681793212890625, 40.40627445938256),
				new Coordinate(-3.681578636169433, 40.40553918613568),
				new Coordinate(-3.680269718170166, 40.406862672196915),
				new Coordinate(-3.680055141448974, 40.40723847213706),
				new Coordinate(-3.6797118186950684, 40.40745087987071),
				new Coordinate(-3.6790251731872554, 40.40756525298811),
				new Coordinate(-3.67866039276123, 40.40769596488429) };

		LineString lineActual;

		LineString lineRoute, lineRoute2, lineRoute3;

		lineActual = generateLine(coords);
		lineRoute = generateLine(coords2);

		lineRoute2 = generateLine(coords3);
		lineRoute3 = generateLine(coords4);

		Geometry polygon = buffer(lineRoute, 0.0005);

		crossPoligon(polygon, lineActual);
		crossPoligon(polygon, lineRoute2);

		crossPoligon(polygon, lineRoute3);

	}

}