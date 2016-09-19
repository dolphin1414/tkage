package tk.serjmusic.models;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BlogComment.class)
public abstract class BlogComment_ extends tk.serjmusic.models.AbstractEntity_ {

	public static volatile SingularAttribute<BlogComment, Date> dateCreated;
	public static volatile SingularAttribute<BlogComment, User> author;
	public static volatile SingularAttribute<BlogComment, BlogEntry> blogEntry;
	public static volatile SingularAttribute<BlogComment, String> content;

}

