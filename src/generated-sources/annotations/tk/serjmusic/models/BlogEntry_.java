package tk.serjmusic.models;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BlogEntry.class)
public abstract class BlogEntry_ extends tk.serjmusic.models.AbstractEntity_ {

	public static volatile SingularAttribute<BlogEntry, String> imageLink;
	public static volatile SingularAttribute<BlogEntry, Date> dateCreated;
	public static volatile ListAttribute<BlogEntry, BlogComment> comments;
	public static volatile SingularAttribute<BlogEntry, User> author;
	public static volatile SingularAttribute<BlogEntry, String> title;
	public static volatile SingularAttribute<BlogEntry, String> content;

}

