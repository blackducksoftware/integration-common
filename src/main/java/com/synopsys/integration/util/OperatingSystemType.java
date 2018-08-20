package com.synopsys.integration.util;

public enum OperatingSystemType {
    LINUX,
    MAC,
    WINDOWS;

    public String prettyPrint() {
        return EnumUtils.prettyPrint(this);
    }

}
