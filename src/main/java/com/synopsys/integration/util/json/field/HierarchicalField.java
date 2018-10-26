/**
 * integration-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.util.json.field;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class HierarchicalField extends Field {
    private final List<String> pathToField;
    private final FieldContentIdentifier contentIdentifier;
    private final Type type;

    public HierarchicalField(final List<String> pathToField, final String innerMostFieldName, final FieldContentIdentifier contentIdentifier, final String label, final Type type) {
        super(innerMostFieldName, label);
        this.pathToField = pathToField;
        this.contentIdentifier = contentIdentifier;
        this.type = type;
    }

    /**
     * @return an unmodifiable list of fields representing the path to the parent element of the inner most field defined by this class
     */
    public List<String> getPathToParent() {
        return Collections.unmodifiableList(pathToField);
    }

    /**
     * @return an unmodifiable list of fields representing the path to a field nested within an object
     */
    public List<String> getPathToInnerField() {
        final List<String> fullList = new ArrayList<>(pathToField.size());
        for (final String pathElement : pathToField) {
            fullList.add(pathElement);
        }
        fullList.add(getKey());
        return Collections.unmodifiableList(fullList);
    }

    public FieldContentIdentifier getContentIdentifier() {
        return contentIdentifier;
    }

    public Type getType() {
        return type;
    }
}
