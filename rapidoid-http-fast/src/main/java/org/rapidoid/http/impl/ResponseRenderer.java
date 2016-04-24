package org.rapidoid.http.impl;

/*
 * #%L
 * rapidoid-http-fast
 * %%
 * Copyright (C) 2014 - 2016 Nikolche Mihajlovski and contributors
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

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.cls.Cls;
import org.rapidoid.commons.MediaType;
import org.rapidoid.http.HttpUtils;
import org.rapidoid.http.Resp;
import org.rapidoid.http.customize.PageRenderer;
import org.rapidoid.http.customize.ViewRenderer;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class ResponseRenderer extends RapidoidThing {

	public static byte[] render(ReqImpl req, Resp resp) {

		Object content = resp.content();

		if (content != null) {
			content = wrapGuiContent(content);
			resp.model().put("content", content);
			resp.content(content);
		}

		ViewRenderer viewRenderer = req.http().custom().viewRenderer();
		U.must(viewRenderer != null, "A view renderer wasn't configured!");

		PageRenderer pageRenderer = req.http().custom().pageRenderer();
		U.must(pageRenderer != null, "A page renderer wasn't configured!");

		boolean rendered;
		String viewName = resp.view();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Object[] renderModel = content != null ? new Object[]{resp.model(), content} : new Object[]{resp.model()};

		try {
			rendered = viewRenderer.render(viewName, renderModel, out);
		} catch (Throwable e) {
			throw U.rte("Error while rendering view: " + viewName, e);
		}

		String renderResult = rendered ? new String(out.toByteArray()) : null;

		if (renderResult == null) {
			Object cnt = U.or(content, "");
			renderResult = new String(HttpUtils.responseToBytes(cnt, MediaType.HTML_UTF_8, null));
		}

		try {
			Object result = U.or(pageRenderer.renderPage(req, resp, renderResult), "");
			return HttpUtils.responseToBytes(result, MediaType.HTML_UTF_8, null);

		} catch (Exception e) {
			throw U.rte("Error while rendering page!", e);
		}
	}

	private static Object wrapGuiContent(Object content) {
		if (Msc.hasRapidoidGUI()) {
			Object[] items = null;

			if (content instanceof Collection<?>) {
				items = U.array((Collection<?>) content);

			} else if (content instanceof Object[]) {
				items = (Object[]) content;
			}

			if (items != null) {
				return Cls.newInstance(Cls.get("org.rapidoid.html.ElementGroup"), new Object[]{items});
			}
		}

		return content;
	}

}
