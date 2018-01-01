package com.ca.lextest.db.model;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NaturalId;

import java.util.Set;

@Entity
//@Table(name = "user")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

	@Id
	//@Column(name = "id")
    //@GeneratedValue(strategy = GenerationType.AUTO)
	//Id are incremented and assigned by a database IDENTITY column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NaturalId /*Set unique and nullability=false*/
	@Column(updatable = false)
    private String email;
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private Integer active;

   
    //FetchType : Lorsque tu as une collection dans un objet, tu as deux possibilités :
    //	- EAGER : Soit on effectue la jointure sql, dès que l'on récupère l'objet et donc initialise la collection. C'est le mode "eager".
    //	- LAZY  : Soit on n'effectue la jointure sql que à la demande, c'est à dire dès que l'on aura besoin de la collection. C'est le mode "lazy".
    // on recommande évidement le mode "lazy" car on ne fait pas des jointures inutilement surtout si on n'a pas besoin de cette collection
    //
    //CascadeType :
    //			CascadeType.PERSIST 	effectue en cascade l'opération de persistance (création) sur les entités associées si persist() est appelée ou si l'entité est supervisée (par le gestionnaire d'entités)
    //			CascadeType.MERGE 	effectue en cascade l'opération de fusion sur les entités associées si merge() est appélée ou si l'entité est supervisée
    //			CascadeType.REMOVE 	effectue en cascade l'opération de suppression sur les entités associées si delete() est appelée
    //			CascadeType.REFRESH 	effectue en cascade l'opération de rafraîchissement sur les entités associées si refresh() est appelée
    //			CascadeType.ALL 	tous ceux du dessus 
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    //private Collection<Role> roles;
    private Set<Role> roles;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.active = 1;
    }
    
    public User(String firstName, String lastName, String email, String password, Integer active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.active = active;
    }

    public User(String firstName, String lastName, String email, String password, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
    
    public User(String firstName, String lastName, String email, String password, Integer active, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.active = active;
        this.roles = roles;
    }

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

    /*/!\ hashCode() and equals() overwrite is mandatory for the right entity persistence management */
	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(email);
        //hcb.append(roles);
        return hcb.toHashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User that = (User) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(email, that.email);
        //eb.append(roles, that.roles);
        return eb.isEquals();
    }

	@Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + "*********" + '\'' +
                ", active='" + active + '\'' +
                ", roles=" + roles +
                '}';
    }
}
