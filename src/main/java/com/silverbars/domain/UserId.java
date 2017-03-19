package com.silverbars.domain;

import java.util.Objects;

final public class UserId {
    private final String userId;

    public static UserId of(String userId) {
        return new UserId(userId);
    }

    public UserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserId{" +
                "userId='" + userId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId1 = (UserId) o;
        return Objects.equals(userId, userId1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
