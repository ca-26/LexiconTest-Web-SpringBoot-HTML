package com.ca.lextest.web.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.ca.lextest.db.constraint.FieldMatch;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldMatch.List({
        @FieldMatch(first = "passwordConfirm", second = "password", message = "{Diff.user.passwordConfirm}"),
        @FieldMatch(first = "emailConfirm", second = "email", message = "{Diff.user.emailConfirm}")
})
public class UserRegistrationDto {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Size(min = 6, max=20, message = "{Size.user.password}")
    @Pattern.List({
        @Pattern(regexp = "(?=.*[0-9]).+", message = "{Pattern.must.contain.digit}"),
        @Pattern(regexp = "(?=.*[a-z]).+", message = "{Pattern.must.contain.lowercase}"),
        @Pattern(regexp = "(?=.*[A-Z]).+", message = "{Pattern.must.contain.uppercase}"),
        @Pattern(regexp = "(?=.*\\W).+", message ="{Pattern.must.contain.specialchar}"),      
        @Pattern(regexp = "(?=\\S+$).+", message = "{Pattern.musnt.contain.whitespace}")
    })
    private String password;

    @NotEmpty
    private String passwordConfirm;

    @Email
    @NotEmpty
    private String email;

    //@Email
    @NotEmpty
    private String emailConfirm;
    
	@AssertTrue(message="{NotEmpty}")
    private Boolean termsAndConditions;

    
    
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmailConfirm() {
		return emailConfirm;
	}
	public void setEmailConfirm(String emailConfirm) {
		this.emailConfirm = emailConfirm;
	}
	public Boolean getTermsAndConditions() {
		return termsAndConditions;
	}
	public void setTermsAndConditions(Boolean termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}
    
	@Override
    public String toString() {
        return "UserRegistrationDto{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", email='" + emailConfirm + '\'' +
                ", password='" + "*********" + '\'' +
                ", passwordConfirm='" + "*********" + '\'' +
                ", termsAndConditions='" + termsAndConditions + '\'' +
                '}';
    }

}
