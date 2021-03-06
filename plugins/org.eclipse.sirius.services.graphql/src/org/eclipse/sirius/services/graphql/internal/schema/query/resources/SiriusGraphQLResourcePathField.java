/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.services.graphql.internal.schema.query.resources;

import java.util.Optional;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

import graphql.Scalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLNonNull;

/**
 * The path field of the resources.
 *
 * @author sbegaudeau
 */
public final class SiriusGraphQLResourcePathField {

    /**
     * The name of the path field.
     */
    private static final String PATH_FIELD = "path"; //$NON-NLS-1$

    /**
     * The constructor.
     */
    private SiriusGraphQLResourcePathField() {
        // Prevent instantiation
    }

    /**
     * Returns the path field.
     *
     * @return The path field
     */
    public static GraphQLFieldDefinition build() {
        // @formatter:off
        return GraphQLFieldDefinition.newFieldDefinition()
                .name(PATH_FIELD)
                .type(new GraphQLNonNull(Scalars.GraphQLString))
                .dataFetcher(SiriusGraphQLResourcePathField.getPathDataFetcher())
                .build();
        // @formatter:on
    }

    /**
     * Returns the path data fetcher.
     *
     * @return The path data fetcher
     */
    private static DataFetcher<String> getPathDataFetcher() {
        // @formatter:off
        return environment -> Optional.of(environment.getSource())
                .filter(IResource.class::isInstance)
                .map(IResource.class::cast)
                .map(IResource::getProjectRelativePath)
                .map(IPath::toString)
                .orElse(null);
        // @formatter:on
    }
}
