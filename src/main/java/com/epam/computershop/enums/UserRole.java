package com.epam.computershop.enums;

public enum UserRole {
    GUEST_ONLY((short)-1),
    GUEST((short)0),
    USER((short)1),
    ADMIN((short)2);

    private final short ROLE_ID;

    UserRole(short roleId) {
        this.ROLE_ID = roleId;
    }

    public short getId() {
        return ROLE_ID;
    }

    public static UserRole getRoleById(short id) {
        UserRole resultRole = ADMIN;
        for (UserRole role : UserRole.values()) {
            if (role.getId() == id) {
                resultRole = role;
            }
        }
        return resultRole;
    }
}