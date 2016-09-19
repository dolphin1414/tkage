package tk.serjmusic.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ extends tk.serjmusic.models.AbstractEntity_ {

	public static volatile SingularAttribute<User, String> imageLink;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SetAttribute<User, BlogComment> comments;
	public static volatile SingularAttribute<User, byte[]> imageFile;
	public static volatile SetAttribute<User, BlogEntry> blogs;
	public static volatile SetAttribute<User, UserRole> roles;
	public static volatile SingularAttribute<User, Boolean> isBanned;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> username;

}

