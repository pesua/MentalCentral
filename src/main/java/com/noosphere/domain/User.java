package com.noosphere.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noosphere.config.Constants;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public @NotNull @Pattern(regexp = Constants.LOGIN_REGEX) @Size(min = 1, max = 50) String getLogin() {
        return this.login;
    }

    public @NotNull @Size(min = 60, max = 60) String getPassword() {
        return this.password;
    }

    public @Size(max = 50) String getFirstName() {
        return this.firstName;
    }

    public @Size(max = 50) String getLastName() {
        return this.lastName;
    }

    public @Email @Size(min = 5, max = 254) String getEmail() {
        return this.email;
    }

    public @NotNull boolean isActivated() {
        return this.activated;
    }

    public @Size(min = 2, max = 10) String getLangKey() {
        return this.langKey;
    }

    public @Size(max = 256) String getImageUrl() {
        return this.imageUrl;
    }

    public @Size(max = 20) String getActivationKey() {
        return this.activationKey;
    }

    public @Size(max = 20) String getResetKey() {
        return this.resetKey;
    }

    public Instant getResetDate() {
        return this.resetDate;
    }

    public Set<Authority> getAuthorities() {
        return this.authorities;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(@NotNull @Pattern(regexp = Constants.LOGIN_REGEX) @Size(min = 1, max = 50) String login) {
        this.login = login;
    }

    public void setPassword(@NotNull @Size(min = 60, max = 60) String password) {
        this.password = password;
    }

    public void setFirstName(@Size(max = 50) String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@Size(max = 50) String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(@Email @Size(min = 5, max = 254) String email) {
        this.email = email;
    }

    public void setActivated(@NotNull boolean activated) {
        this.activated = activated;
    }

    public void setLangKey(@Size(min = 2, max = 10) String langKey) {
        this.langKey = langKey;
    }

    public void setImageUrl(@Size(max = 256) String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setActivationKey(@Size(max = 20) String activationKey) {
        this.activationKey = activationKey;
    }

    public void setResetKey(@Size(max = 20) String resetKey) {
        this.resetKey = resetKey;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User other = (User) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$login = this.getLogin();
        final Object other$login = other.getLogin();
        if (this$login == null ? other$login != null : !this$login.equals(other$login)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$firstName = this.getFirstName();
        final Object other$firstName = other.getFirstName();
        if (this$firstName == null ? other$firstName != null : !this$firstName.equals(other$firstName)) return false;
        final Object this$lastName = this.getLastName();
        final Object other$lastName = other.getLastName();
        if (this$lastName == null ? other$lastName != null : !this$lastName.equals(other$lastName)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        if (this.isActivated() != other.isActivated()) return false;
        final Object this$langKey = this.getLangKey();
        final Object other$langKey = other.getLangKey();
        if (this$langKey == null ? other$langKey != null : !this$langKey.equals(other$langKey)) return false;
        final Object this$imageUrl = this.getImageUrl();
        final Object other$imageUrl = other.getImageUrl();
        if (this$imageUrl == null ? other$imageUrl != null : !this$imageUrl.equals(other$imageUrl)) return false;
        final Object this$activationKey = this.getActivationKey();
        final Object other$activationKey = other.getActivationKey();
        if (this$activationKey == null ? other$activationKey != null : !this$activationKey.equals(other$activationKey))
            return false;
        final Object this$resetKey = this.getResetKey();
        final Object other$resetKey = other.getResetKey();
        if (this$resetKey == null ? other$resetKey != null : !this$resetKey.equals(other$resetKey)) return false;
        final Object this$resetDate = this.getResetDate();
        final Object other$resetDate = other.getResetDate();
        if (this$resetDate == null ? other$resetDate != null : !this$resetDate.equals(other$resetDate)) return false;
        final Object this$authorities = this.getAuthorities();
        final Object other$authorities = other.getAuthorities();
        if (this$authorities == null ? other$authorities != null : !this$authorities.equals(other$authorities))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $login = this.getLogin();
        result = result * PRIME + ($login == null ? 43 : $login.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $firstName = this.getFirstName();
        result = result * PRIME + ($firstName == null ? 43 : $firstName.hashCode());
        final Object $lastName = this.getLastName();
        result = result * PRIME + ($lastName == null ? 43 : $lastName.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        result = result * PRIME + (this.isActivated() ? 79 : 97);
        final Object $langKey = this.getLangKey();
        result = result * PRIME + ($langKey == null ? 43 : $langKey.hashCode());
        final Object $imageUrl = this.getImageUrl();
        result = result * PRIME + ($imageUrl == null ? 43 : $imageUrl.hashCode());
        final Object $activationKey = this.getActivationKey();
        result = result * PRIME + ($activationKey == null ? 43 : $activationKey.hashCode());
        final Object $resetKey = this.getResetKey();
        result = result * PRIME + ($resetKey == null ? 43 : $resetKey.hashCode());
        final Object $resetDate = this.getResetDate();
        result = result * PRIME + ($resetDate == null ? 43 : $resetDate.hashCode());
        final Object $authorities = this.getAuthorities();
        result = result * PRIME + ($authorities == null ? 43 : $authorities.hashCode());
        return result;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", login=" + this.getLogin() + ", password=" + this.getPassword() + ", firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + ", email=" + this.getEmail() + ", activated=" + this.isActivated() + ", langKey=" + this.getLangKey() + ", imageUrl=" + this.getImageUrl() + ", activationKey=" + this.getActivationKey() + ", resetKey=" + this.getResetKey() + ", resetDate=" + this.getResetDate() + ", authorities=" + this.getAuthorities() + ")";
    }
}
