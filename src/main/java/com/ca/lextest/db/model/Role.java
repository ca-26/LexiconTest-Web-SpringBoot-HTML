package com.ca.lextest.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NaturalId;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


@Entity
//@Table(name = "role")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Role {

	@Id
	//@Column(name="id")
	//@GeneratedValue(strategy = GenerationType.AUTO)
	//Id are incremented and assigned by a database IDENTITY column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@NaturalId
	@Column(updatable = false)
    private String name;

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    /*/!\ hashCode() and equals() overwrite is mandatory for the right entity persistence management */
    
    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(name);
        return hcb.toHashCode();
    } 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Role)) {
            return false;
        }
        Role that = (Role) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(name, that.name);
        return eb.isEquals();
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}