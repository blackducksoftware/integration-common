/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import java.util.Objects;
import java.util.Optional;

public class NameVersion extends Stringable {
    private String name;
    private String version;

    public NameVersion() {
    }

    public NameVersion(final String name, final String version) {
        this.name = Optional.ofNullable(name).orElse("");
        this.version = Optional.ofNullable(version).orElse("");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NameVersion that = (NameVersion) o;
        return Objects.equals(name, that.name) && Objects.equals(version, that.version); // ignore case?
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, version);
    }
}
