package ru.zhem.entity;

import java.util.Set;

public class ZhemUser extends BaseEntity {

    private String phone;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private Set<Role> roles;

}

