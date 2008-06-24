package codalyze.ui.web.providers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ProduceMime;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import codalyze.core.entity.SQLQuery;

import net.sf.json.JSONObject;

@Provider
@ProduceMime({"text/plain", "text/javascript", "application/json"})
public class ObjectJsonProvider implements MessageBodyWriter<SQLQuery> {

	@Override
	public long getSize(SQLQuery arg0) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2) {
		return SQLQuery.class.isAssignableFrom(arg0);
	}

	@Override
	public void writeTo(SQLQuery body, Class<?> arg1, Type arg2,
			Annotation[] arg3, MediaType arg4,
			MultivaluedMap<String, Object> arg5, OutputStream stream)
			throws IOException, WebApplicationException {
		
		stream.write(JSONObject.fromObject(body).toString(3).getBytes());
	}

}
