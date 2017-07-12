/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.springlets.data.jpa.domain;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

/**
 * = QEmbeddableImage
 * 
 * QClass that will be used by QueryDSL inside the projects that uses
 * the {@link EmbeddableImage} as property of some entity.
 * 
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public class QEmbeddableImage extends EntityPathBase<EmbeddableImage> {

  private static final long serialVersionUID = -1907809921L;

  private static final PathInits INITS = PathInits.DIRECT2;

  public static final QEmbeddableImage embeddableImage = new QEmbeddableImage("embeddableImage");

  public final StringPath image = createString("image");

  public QEmbeddableImage(String variable) {
    this(EmbeddableImage.class, forVariable(variable), INITS);
  }

  public QEmbeddableImage(Path<? extends EmbeddableImage> path) {
    this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
  }

  public QEmbeddableImage(PathMetadata metadata) {
    this(metadata, PathInits.getFor(metadata, INITS));
  }

  public QEmbeddableImage(PathMetadata metadata, PathInits inits) {
    this(EmbeddableImage.class, metadata, inits);
  }

  public QEmbeddableImage(Class<? extends EmbeddableImage> type, PathMetadata metadata,
      PathInits inits) {
    super(type, metadata, inits);
  }

}
