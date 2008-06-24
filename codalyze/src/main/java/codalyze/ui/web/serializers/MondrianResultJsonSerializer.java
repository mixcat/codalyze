package codalyze.ui.web.serializers;

import java.util.HashMap;

import mondrian.olap.Axis;
import mondrian.olap.Cell;
import mondrian.olap.MondrianException;
import mondrian.olap.Result;

public class MondrianResultJsonSerializer {

	public static Object serialize(Result result) {
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
