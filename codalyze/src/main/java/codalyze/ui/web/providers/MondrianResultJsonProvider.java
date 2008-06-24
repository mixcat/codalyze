package codalyze.ui.web.providers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.ProduceMime;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import mondrian.olap.Result;
import net.sf.json.JSONObject;
import codalyze.ui.web.serializers.MondrianResultJsonSerializer;

/**
 * Transforms a Mondrian Result Object in a json Message Body
 * 
 * Depending on the query, output will be a 2D or 3D result set: 
 * 2D: {fields={columns:[], rows:[], pages:null}, values=[][]}
 * 3D: {fields={columns:[], rows:[], pages:[]}, values=[][][]}
 */
@Provider
@ProduceMime({"application/json"})
public class MondrianResultJsonProvider implements MessageBodyWriter<Result>{

	@Override
	public long getSize(Result arg0) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2) {
		return Result.class.isAssignableFrom(arg0);
	}

	@Override
	public void writeTo(Result result, Class<?> arg1, Type type, Annotation[] arg3, MediaType mediaType,
					MultivaluedMap<String, Object> valueMap, OutputStream stream) throws IOException, WebApplicationException {
		JSONObject jsonObject = JSONObject.fromObject(MondrianResultJsonSerializer.serialize(result));
		stream.write(("result=" + jsonObject.toString(3)).getBytes());
	}



}
