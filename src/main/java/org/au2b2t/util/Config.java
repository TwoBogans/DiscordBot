package org.au2b2t.util;

import lombok.Getter;

@Getter
public class Config {

    private final String token;
    private final long userRole;

    public Config() {
        this.token = "";
        this.userRole = 0L;
    }
}
