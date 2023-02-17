/*
 * integration-common
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.util;

public class NameVersion extends Stringable {
    private String name;
    private String version;

    public NameVersion() {
    }

    public NameVersion(final String name, final String version) {
        this.name = name;
        this.version = version;
    }

    public NameVersion(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

}
