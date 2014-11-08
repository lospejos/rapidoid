package org.rapidoid.app.example;

/*
 * #%L
 * rapidoid-app
 * %%
 * Copyright (C) 2014 Nikolche Mihajlovski
 * %%
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
 * #L%
 */

import static org.rapidoid.html.Bootstrap.*;
import static org.rapidoid.html.HTML.*;
import static org.rapidoid.pages.bootstrap.BootstrapWidget.*;

import org.rapidoid.html.tag.DivTag;
import org.rapidoid.model.Items;
import org.rapidoid.model.Model;
import org.rapidoid.pages.bootstrap.TableWidget;

public class TasksScreen {

	String title = "My Tasks";

	Object content() {

		Items items = Model.beanItems(new Task("aa", Priority.MEDIUM), new Task("bb", Priority.HIGH), new Task("aa",
				Priority.MEDIUM), new Task("bb", Priority.HIGH), new Task("aa", Priority.MEDIUM), new Task("bb",
				Priority.HIGH), new Task("aa", Priority.MEDIUM), new Task("bb", Priority.HIGH), new Task("aa",
				Priority.MEDIUM), new Task("bb", Priority.HIGH));

		DivTag caption = row(cols(12, h3("List of tasks:")));
		TableWidget table = new TableWidget(items);

		return arr(caption, rowFull(table));
	}

}
