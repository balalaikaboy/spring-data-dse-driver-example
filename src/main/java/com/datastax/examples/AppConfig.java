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

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.dse.auth.DsePlainTextAuthProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableAutoConfiguration
public class AppConfig {

    @Configuration
    @EnableCassandraRepositories
    static class CassandraConfig extends AbstractCassandraConfiguration {
        /**
         * @return A custom auth provider for connecting with a DSE cluster configured using DseAuthenticator.
         */
        @Override
        protected AuthProvider getAuthProvider() {
            return new DsePlainTextAuthProvider("cassandra", "cassandra");
        }

        @Override
        protected String getKeyspaceName() {
            return "myks";
        }

        @Override
        public String[] getEntityBasePackages() {
            return new String[]{"com.datastax.examples.entities"};
        }

        @Override
        public SchemaAction getSchemaAction() {
            return SchemaAction.RECREATE;
        }
    }
}
