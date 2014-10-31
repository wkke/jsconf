/**
 * Copyright 2013 Yves Galante
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.jsconf.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.jsconf.core.ConfigurationFactory;
import org.jsconf.core.test.bean.MyConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ChildTest {

	@Autowired
	private MyConfig myConfig;

	@Autowired
	private MyConfig myConfigChild;

	@Test
	public void testChild() {
		assertEquals("Tic", this.myConfig.getValue());
		assertNotSame(this.myConfig, this.myConfig.getAChild());
		assertNotNull(this.myConfig.getAChild());
		assertEquals("Tac", this.myConfig.getAChild().getValue());
		assertSame(this.myConfigChild, this.myConfig.getAChild());
	}

	@Configuration
	static class ContextConfiguration01 {
		@Bean(name = "context")
		public static ConfigurationFactory configurationFactory() {
			return new ConfigurationFactory().withResourceName("org/jsconf/core/test/app_child");
		}
	}
}
