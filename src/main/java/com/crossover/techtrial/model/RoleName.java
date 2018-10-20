package com.crossover.techtrial.model;

public enum RoleName {
	ROLE_ADMIN(1L), ROLE_PERSON(2L);

	private transient Long id;

	private RoleName(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
