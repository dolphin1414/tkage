package tk.serjmusic.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PhotoEntry.class)
public abstract class PhotoEntry_ extends tk.serjmusic.models.AbstractEntity_ {

	public static volatile SingularAttribute<PhotoEntry, String> imageLink;
	public static volatile SingularAttribute<PhotoEntry, Boolean> isBackgroundImage;
	public static volatile SingularAttribute<PhotoEntry, String> description;
	public static volatile SingularAttribute<PhotoEntry, String> title;

}

