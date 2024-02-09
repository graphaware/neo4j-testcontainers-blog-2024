/*
 * Copyright (c) Graph Aware Limited - All Rights Reserved
 * This file is part of GraphAware Hume
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package org.junit.runners.model;

// DO NOT REMOVE THIS CLASS UNTIL AT LEAST testcontainers 2.0
// Explanation: We need to fake some JUnit4 classes, as testcontainers 1.x needs them;
// Since we wanted to get rid of JUnit4, org.junit has been added as an exclusion of testcontainers dependencies, but if you remove this class,
//testcontainers won't start/won't work


@SuppressWarnings("unused")
public class Statement {
}
