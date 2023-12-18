/**
 * 
 */
package com.kyj.fx.nightmare.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

/**
 * @author calla
 *
 */
public class ChartBuilder {

	// 문자열 데이터를 파싱하여 List<Map<String, Object>> 형식으로 변환
	protected List<Map<String, Object>> parseData(String dataStr) {
		List<Map<String, Object>> data = new ArrayList<>();
		String[] entries = dataStr.split(";");

		for (String entry : entries) {
			String[] keyValue = entry.split(",");
			if (keyValue.length == 2) {
				Map<String, Object> map = new HashMap<>();
				map.put(keyValue[0].trim(), Double.parseDouble(keyValue[1].trim()));
				data.add(map);
			}
		}

		return data;
	}

	public LineChart<String, Number> createLineChart(List<Map<String, Object>> data) {
		// X축과 Y축을 생성합니다.
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();

		// LineChart 인스턴스를 생성합니다.
		LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		ObservableList<XYChart.Series<String, Number>> list = FXCollections.observableArrayList();
		// 데이터를 기반으로 시리즈를 생성합니다.
		String[] seriesNames = new String[] { "default" };
		for (String key : seriesNames) {
			// 각 키(범주)에 대한 시리즈를 생성합니다.
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName(key); // 시리즈의 이름을 설정합니다.

			// 각 데이터 맵을 순회하며 시리즈에 데이터를 추가합니다.
			for (Map<String, Object> row : data) {

				Iterator<Entry<String, Object>> iterator = row.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> next = iterator.next();
					String category = next.getKey();
					Number value = (Number) next.getValue();
					series.getData().add(new XYChart.Data<>(category, value));
				}

//				String category = key;
//				Number value = (Number) row.get(key);
//				series.getData().add(new XYChart.Data<>(category, value));
			}

			// 생성된 시리즈를 차트에 추가합니다.
			list.add(series);
		}
		lineChart.setData(list);
		return lineChart;
	}

	public BarChart<String, Number> createBarChart(List<Map<String, Object>> data) {
		// X축과 Y축을 생성합니다.
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();

		// LineChart 인스턴스를 생성합니다.
		BarChart<String, Number> lineChart = new BarChart<>(xAxis, yAxis);
		ObservableList<Series<String, Number>> chartDataList = FXCollections.observableArrayList();
		lineChart.setData(chartDataList);
		// 데이터를 기반으로 시리즈를 생성합니다.
		String[] seriesNames = new String[] { "default" };
		for (String key : seriesNames) {
			// 각 키(범주)에 대한 시리즈를 생성합니다.
			BarChart.Series<String, Number> series = new BarChart.Series<>();
			series.setName(key); // 시리즈의 이름을 설정합니다.
			ObservableList<Data<String, Number>> dataSeries = FXCollections.observableArrayList();
//			series.setData(observableArrayList);
			// 각 데이터 맵을 순회하며 시리즈에 데이터를 추가합니다.
			for (Map<String, Object> row : data) {
				Iterator<Entry<String, Object>> iterator = row.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> next = iterator.next();
					String category = next.getKey();
					Number value = (Number) next.getValue();
					dataSeries.add(new XYChart.Data<>(category, value));
				}

			}
			// 생성된 시리즈를 차트에 추가합니다.
			series.setData(dataSeries);
			chartDataList.add(series);
			lineChart.setData(chartDataList);
		}

		return lineChart;
	}

	public AreaChart<String, Number> createAreaChart(List<Map<String, Object>> data) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();

		AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);

		String[] seriesNames = new String[] { "default" };
		for (String key : seriesNames) {
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName(key);

			for (Map<String, Object> row : data) {

				Iterator<Entry<String, Object>> iterator = row.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> next = iterator.next();
					String category = next.getKey();
					Number value = (Number) next.getValue();
					series.getData().add(new XYChart.Data<>(category, value));
				}

//				String category = key;
//				Number value = (Number) row.get(key);
//				series.getData().add(new XYChart.Data<>(category, value));
			}

			areaChart.getData().add(series);
		}

		return areaChart;
	}

	public ScatterChart<String, Number> createScatterChart(List<Map<String, Object>> data) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();

		ScatterChart<String, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
		String[] seriesNames = new String[] { "default" };
		for (String key : seriesNames) {
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName(key);

			for (Map<String, Object> row : data) {

				Iterator<Entry<String, Object>> iterator = row.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> next = iterator.next();
					String category = next.getKey();
					Number value = (Number) next.getValue();
					series.getData().add(new XYChart.Data<>(category, value));
				}

//				String category = key;
//				Number value = (Number) row.get(key);
//				series.getData().add(new XYChart.Data<>(category, value));
			}

			scatterChart.getData().add(series);
		}

		return scatterChart;
	}

	public BubbleChart<Number, Number> createBubbleChart(List<Map<String, Object>> data) {
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();

		BubbleChart<Number, Number> bubbleChart = new BubbleChart<>(xAxis, yAxis);
		String[] seriesNames = new String[] { "default" };
		for (String key : seriesNames) {
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			series.setName(key);

			for (Map<String, Object> row : data) {

				Iterator<Entry<String, Object>> iterator = row.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> next = iterator.next();

					Number category = Double.parseDouble(next.getKey());
					Number value = (Number) next.getValue();
					series.getData().add(new XYChart.Data<>(category, value, 10));
				}

//				String category = key;
//				Number value = (Number) row.get(key);
//				// BubbleChart의 경우, 버블의 크기를 설정할 수 있습니다.
//				// 여기서는 모든 버블의 크기를 동일하게 설정합니다.
//				series.getData().add(new XYChart.Data<>(category, value, 10)); // 마지막 인자는 버블 크기입니다.
			}

			bubbleChart.getData().add(series);
		}

		return bubbleChart;
	}

	public PieChart createPieChart(List<Map<String, Object>> data) {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

		for (Map<String, Object> row : data) {
			for (String key : row.keySet()) {
				Number value = (Number) row.get(key);
				pieChartData.add(new PieChart.Data(key, value.doubleValue()));
			}
		}

		PieChart pieChart = new PieChart(pieChartData);
		return pieChart;
	}

}
