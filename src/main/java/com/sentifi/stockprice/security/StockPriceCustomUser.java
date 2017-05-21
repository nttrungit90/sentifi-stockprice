package com.sentifi.stockprice.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class StockPriceCustomUser extends User{
	private static final long serialVersionUID = 1733777081200873001L;

    private Integer id;
    private String firstName;
    private String lastName;
    private Integer groupId;

	public StockPriceCustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
			Integer id, String firstName, String lastName, Integer groupId) {
		super(username, password, authorities);
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.groupId = groupId;
	}



	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
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

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
