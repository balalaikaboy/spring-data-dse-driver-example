/*
 * Copyright 2013-2016 the original author or authors.
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
package com.datastax.examples;

import com.datastax.examples.entities.User;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Simple repository interface for {@link User} instances. The interface is used to declare so called query methods,
 * methods to retrieve single entities or collections of them.
 * <p>
 * Changed for the intent of demonstrating usage with DSE.
 * <p>
 * from: https://github.com/spring-projects/spring-data-examples/blob/master/cassandra/example/src/main/java/example/springdata/cassandra/basic/BasicUserRepository.java
 * <p>
 * Original authors:
 *
 * @author Thomas Darimont
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Sample method annotated with {@link Query}. This method executes the CQL from the {@link Query} value.
     *
     * @param id
     * @return
     */
    @Query("SELECT * from users where id in(?0)")
    User findUserByIdIn(long id);

    /**
     * Derived query method. This query corresponds with {@code SELECT * FROM users WHERE uname = ?0}.
     * {@link User#username} is not part of the primary so it requires a secondary index.
     *
     * @param username
     * @return
     */
    User findUserByUsername(String username);
}