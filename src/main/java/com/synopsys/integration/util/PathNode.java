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
package com.synopsys.integration.util;

import java.util.List;

public class PathNode<T> {
    private final T key;
    private PathNode<T> next;

    private PathNode(final T key) {
        this.key = key;
        this.next = null;
    }

    public static <T> PathNode<T> createPath(final List<T> list) {
        PathNode<T> previousNode = null;
        PathNode<T> firstNode = null;

        for (final T value : list) {
            final PathNode currentNode = new PathNode(value);
            if (firstNode == null) {
                firstNode = currentNode;
            }
            if (previousNode != null) {
                previousNode.next = currentNode;
            }
            previousNode = currentNode;
        }
        return firstNode;
    }

    public T getKey() {
        return key;
    }

    public PathNode getNext() {
        return next;
    }
}
