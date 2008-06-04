package codalyze.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;

import javax.ws.rs.ProduceMime;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import mondrian.olap.Axis;
import mondrian.olap.Cell;
import mondrian.olap.MondrianException;
import mondrian.olap.Result;
import net.sf.json.JSONObject;

/**
 * Transforms a Mondrian Result Object in a json Message Body; thanks to
 * @Provider and @ProduceMime annotations, Jersey aumatically selects this
 * class to translate from mondrian.olap.Result to application/json.
 * 
 * Depending on the query, output will be a 2D or 3D result set: 
 * 2D: {fields={columns:[], rows:[], pages:null}, values=[][]}
 * 3D: {fields={columns:[], rows:[], pages:[]}, values=[][][]}
 */
@Provider
@ProduceMime({"application/json"})
public class MondrianResultJsonWriter implements MessageBodyWriter<Result>{

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
		JSONObject jsonObject = JSONObject.fromObject(getResultValues(result));
		stream.write(("result=" + jsonObject.toString(3)).getBytes());
	}

	private static Object getResultValues(Result result) {
		Axis[] axes = result.getAxes();
		Object resultValues;
		if (axes.length == 2) {
			resultValues = getResultValues2D(result);
		} else {
			resultValues = getResultValues3D(result);
		}
		
		HashMap<String,Object> hashMap = new HashMap<String, Object>();
		hashMap.put("fields", "");
		hashMap.put("values", resultValues);
		return hashMap;
	}

	private static Object[][][] getResultValues3D(Result result) {
		Axis[] axes = result.getAxes();
		Axis columnAxes = axes[0];
		Axis rowAxes = axes[1];
		Axis elements = axes[2];

		Object[][][] rows = new Object[rowAxes.getPositions().size()][][];
		for (int row = 0; row < rowAxes.getPositions().size(); row++) {
			rows[row] = new Object[columnAxes.getPositions().size()][];
			for (int col = 0; col < columnAxes.getPositions().size(); col++) {
				int[] coords;

				rows[row][col] = new Object[elements.getPositions().size()];
				for (int element = 0; element < elements.getPositions().size(); element++) {
					coords = new int[] {col,row,element};
					try {
						Cell cell = result.getCell(coords);
						rows[row][col][element] = cell.isNull() ? -1 : cell.getValue();
					}
					catch (MondrianException e) {
						System.out.println("exception while getting cell " + col + "," + row + "," + element);
						e.printStackTrace();
					}
				}
			}
		}
		return rows;
	}

	private static Object[][] getResultValues2D(Result result) {
		Axis[] axes = result.getAxes();
		Axis columnAxes = axes[0];
		Axis rowAxes = axes[1];

		Object[][] rows = new Object[rowAxes.getPositions().size()][];
		for (int row = 0; row < rowAxes.getPositions().size(); row++) {
			rows[row] = new Object[columnAxes.getPositions().size()];
			for (int col = 0; col < columnAxes.getPositions().size(); col++) {
				int[] coords = new int[] {col,row};
				Cell cell = result.getCell(coords);
				rows[row][col] = cell.isNull() ? -1 : cell.getValue();
			}
		}
		return rows;
	}

}
