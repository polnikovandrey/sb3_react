package com.mcfly.template.domain.user_role;

import java.util.Arrays;
import java.util.Objects;

public enum RoleName {

    ROLE_ADMIN("ROLE_ADMIN"), ROLE_USER("ROLE_USER");

    private final String name;

    RoleName(String name) {
        this.name = name;
    }

    public static RoleName valueOfName(String name) {
        return Arrays.stream(values())
                .filter(roleName -> Objects.equals(name, roleName.getName()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "RoleName{" +
                "name='" + name + '\'' +
                '}';
    }
}
