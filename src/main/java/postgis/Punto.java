package postgis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Entity (name ="app")
@Setter
@Getter

public class Punto {
	
	public Punto() {
	}
	
	@Id
	@Column(name ="p_id", columnDefinition ="integer")			
	public Integer  pId;
	
	 //@Column(columnDefinition = "geometry")
	@Column(name="the_geom", columnDefinition = "geometry(Point,4326)")
	private  com.vividsolutions.jts.geom.Point  position;
	
//	@Column(columnDefinition = "the_geom")
//	@Type(type = "org.hibernate.spatial.GeometryType") // "org.hibernatespatial.GeometryUserType" seems to be for older
//														// versions of Hibernate Spatial
//	public com.vividsolutions.jts.geom.Point coordinates;
	
}

/*
 * @Type(type = "org.hibernate.spatial.GeometryType") private Geometry geom;
 */