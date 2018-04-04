package server.dataIn;

import java.util.ArrayList;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GeneratorSamples {
	  public static SimpleFeature createSimplePointFeature(SimpleFeatureType schema) {
	    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
	    double latitude = (Math.random() * 180.0) - 90.0;
	    double longitude = (Math.random() * 360.0) - 180.0;
	    String name = "thing" + Math.random();
	    int number = (int) Math.round(Math.random() * 10.0);

	    GeometryFactory geometryFactory = new GeometryFactory();
	    /* Longitude (= x coord) first ! */
	    com.vividsolutions.jts.geom.Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

	    featureBuilder.add(point);
	  /*  featureBuilder.add(name);
	    featureBuilder.add(number);*/
	    SimpleFeature feature = featureBuilder.buildFeature(null);
	    return feature;
	  }

	  public static SimpleFeature createSimpleLineFeature(SimpleFeatureType schema) {
	    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
	    LineString line = createRandomLineString(5);
	    featureBuilder.add(line);

	    SimpleFeature feature = featureBuilder.buildFeature(null);
	    return feature;
	  }

	  /**
	   * @return
	   */
	  public static LineString createRandomLineString(int n) {
	    double latitude = (Math.random() * 180.0) - 90.0;
	    double longitude = (Math.random() * 360.0) - 180.0;
	    GeometryFactory geometryFactory = new GeometryFactory();
	    /* Longitude (= x coord) first ! */
	    ArrayList<Coordinate> points = new ArrayList<Coordinate>();
	    points.add(new Coordinate(longitude, latitude));
	    for (int i = 1; i < n; i++) {
	      double deltaX = (Math.random() * 10.0) - 5.0;
	      double deltaY = (Math.random() * 10.0) - 5.0;
	      longitude += deltaX;
	      latitude += deltaY;
	      points.add(new Coordinate(longitude, latitude));
	    }
	    LineString line = geometryFactory.createLineString((Coordinate[]) points.toArray(new Coordinate[] {}));
	    return line;
	  }

	  public static SimpleFeature createSimplePolygonFeature(SimpleFeatureType schema) {
	    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
	    Polygon poly = createRandomPolygon(5);
	    featureBuilder.add(poly);

	    SimpleFeature feature = featureBuilder.buildFeature(null);
	    return feature;
	  }

	  
	  public static Polygon createRandomPolygon(int n) {
	    double latitude = (Math.random() * 180.0) - 90.0;
	    double longitude = (Math.random() * 360.0) - 180.0;
	    GeometryFactory geometryFactory = new GeometryFactory();
	    /* Longitude (= x coord) first ! */
	    Polygon poly = null;
	    boolean valid = false;
	    while (!valid) {
	      ArrayList<Coordinate> points = new ArrayList<Coordinate>();
	      points.add(new Coordinate(longitude, latitude));
	      double lon = longitude;
	      double lat = latitude;
	      for (int i = 1; i < n; i++) {
	        double deltaX = (Math.random() * 10.0) - 5.0;
	        double deltaY = (Math.random() * 10.0) - 5.0;
	        lon += deltaX;
	        lat += deltaY;
	        points.add(new Coordinate(lon, lat));
	      }
	      points.add(new Coordinate(longitude, latitude));
	      poly = geometryFactory.createPolygon((Coordinate[]) points.toArray(new Coordinate[] {}));
	      valid = poly.isValid();
	    }
	    return poly;
	  }

	  public static Point createRandomPoint() {
	    double latitude = (Math.random() * 180.0) - 90.0;
	    double longitude = (Math.random() * 360.0) - 180.0;
	    GeometryFactory geometryFactory = new GeometryFactory();
	    /* Longitude (= x coord) first ! */
	    com.vividsolutions.jts.geom.Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

	    return point;
	  }
	}

