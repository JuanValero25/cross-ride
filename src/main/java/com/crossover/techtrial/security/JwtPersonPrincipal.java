package com.crossover.techtrial.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.crossover.techtrial.model.Person;

public class JwtPersonPrincipal implements UserDetails {

	private static final long serialVersionUID = -3979428238068893841L;
	private Long id;
	private String name;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	public static JwtPersonPrincipal create(Person person) {
		List<GrantedAuthority> authorities = person.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleName().toString())).collect(Collectors.toList());
		JwtPersonPrincipal principal = new JwtPersonPrincipal();
		principal.id = person.getId();
		principal.name = person.getName();
		principal.email = person.getEmail();
		principal.password = person.getRegistrationNumber();
		principal.authorities = authorities;
		return principal;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object person) {
		if (this == person) {
			return true;
		}
		if (person == null || getClass() != person.getClass()) {
			return false;
		}
		JwtPersonPrincipal that = (JwtPersonPrincipal) person;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
