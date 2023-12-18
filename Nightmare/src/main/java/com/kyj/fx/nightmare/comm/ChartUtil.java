/**
 * 
 */
package com.kyj.fx.nightmare.comm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.StackPane;

/**	
 * 
 */
public class ChartUtil {

	public enum ChartType {
		BAR_CHART, LINE_CHART, AREA_CHART, SCATTER_CHART, BUBBLE_CHART, PIE_CHART
	}

	/**
	 * @param _chartType
	 * @param data
	 * @return
	 */
	public static Chart createChart(String _chartType, List<Map<String, Object>> data) {

		ChartType chartType = ChartType.valueOf(_chartType);
		ChartBuilder builder = new ChartBuilder();
		switch (chartType) {
		case LINE_CHART:
			return builder.createLineChart(data);
		case BAR_CHART:
			return builder.createBarChart(data);
		case AREA_CHART:
			return builder.createAreaChart(data);
		case SCATTER_CHART:
			return builder.createScatterChart(data);
		case BUBBLE_CHART:
			return builder.createBubbleChart(data);
		case PIE_CHART:
			return builder.createPieChart(data);
		default:
			throw new IllegalArgumentException("Invalid chart type");
		}
	}
	
	/**
	 * @param chartType
	 * @param data
	 * @return
	 */
	public static Chart createChart(ChartType chartType, List<Map<String, Object>> data) {

		ChartBuilder builder = new ChartBuilder();
		switch (chartType) {
		case LINE_CHART:
			return builder.createLineChart(data);
		case BAR_CHART:
			return builder.createBarChart(data);
		case AREA_CHART:
			return builder.createAreaChart(data);
		case SCATTER_CHART:
			return builder.createScatterChart(data);
		case BUBBLE_CHART:
			return builder.createBubbleChart(data);
		case PIE_CHART:
			return builder.createPieChart(data);
		default:
			throw new IllegalArgumentException("Invalid chart type");
		}
	}

	public static <T extends BarChart<String, Number>> T createBarChart(Consumer<T> supplier) {
		ObservableList<Series<String, Number>> data = FXCollections.observableArrayList();
		BarChart<String, Number> barChart = new BarChart<String, Number>(new CategoryAxis(), new NumberAxis(), data);
		// 팝업 라벨 생성
//        final Label label = new Label();
//        label.getStyleClass().add("chart-popup-label");
//        label.setMinSize(50, 30);
//        label.setVisible(false);
//        
//		barChart.addEventHandler(MouseEvent.MOUSE_MOVED, event->{
//				event.getPickResult().getIntersectedNode();
//			  	double mouseX = event.getX();
//	            double mouseY = event.getY();
//
//	            Axis<String> xAxis = barChart.getXAxis();
//	            Axis<Number> yAxis = barChart.getYAxis();
//	            // X, Y 값 계산
//	            String x = xAxis.getValueForDisplay(mouseX);
//	            double y = yAxis.getValueForDisplay(mouseY).doubleValue();
//
//	            // 팝업 위치 설정
//	            label.setLayoutX(mouseX + 10);
//	            label.setLayoutY(mouseY - 40);
//
//	            // 팝업 내용 설정
//	            label.setText("X: " + x + ", Y: " + String.format("%.2f", y));
//	            label.setVisible(true);
//		});
//		// 마우스가 차트 바깥으로 나갔을 때 숨기기
//		barChart.addEventHandler(MouseEvent.MOUSE_EXITED, event->label.setVisible(false));

		supplier.accept((T) barChart);
		return (T) barChart;
	}

	public static <T extends BarChart<String, Number>> T createBarChart(Consumer<T> supplier,
			Series<String, Number> series) {
		T barChart = createBarChart(supplier);
		barChart.getData().add(series);
		return barChart;
	}

	public static <T extends BarChart<String, Number>> T createBarChart(Consumer<T> supplier,
			Data<String, Number>... dataList) {
		Series<String, Number> series = new Series<>("default", FXCollections.observableArrayList(dataList));
		return (T) createBarChart(supplier, series);
	}

	/**
	 * @param <T>
	 * @param supplier
	 * @param data
	 * @param dataName  data에서 제공되는 key중에 x라벨로 처리할 key 이름
	 * @param valueName data에서 제공되는 value중에 y축으로 처리할 value 이름
	 * @return
	 */
	public static <T extends BarChart<String, Number>> T createBarChart(Consumer<T> supplier,
			List<Map<String, Object>> data, String dataName, String valueName) {
		ObservableList<Data<String, Number>> collect = data.stream().filter(v -> {
			return v.get(dataName) != null;
		}).filter(v -> {
			return v.get(valueName) instanceof Number;
		}).map(v -> {
			Data<String, Number> chtdata = new Data<>(v.get(dataName).toString(), (Number) v.get(valueName));

			StackPane stackPane = new StackPane();
			stackPane.setUserData(v.get(valueName));

			chtdata.setNode(stackPane);
			return chtdata;
		}).collect(FxCollectors.toObservableList());

		Series<String, Number> series = new Series<>("default", collect);
		return (T) createBarChart(supplier, series);
	}

	public static <T extends LineChart<String, Number>> T createLineChart(Consumer<T> supplier) {
		ObservableList<Series<String, Number>> data = FXCollections.observableArrayList();
		LineChart<String, Number> barChart = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis(), data);
		supplier.accept((T) barChart);
		return (T) barChart;
	}

	public static Series<String, Number> fromJsonFile(File f) throws IOException {

		if (!f.exists())
			throw new FileNotFoundException("file does not exists.");

		ObjectMapper objectMapper = new ObjectMapper();
		ArrayList readValue = objectMapper.readValue(f, ArrayList.class);

		Series<String, Number> series = new Series<String, Number>();

		Iterator<JsonNode> elements = readValue.iterator();
		while (elements.hasNext()) {
			Map next = (Map) elements.next();
			String seriesName = next.get("series").toString();
			series.setName(seriesName);
			List<Map> dataList = (List<Map>) next.get("data");

			for (Map datanode : dataList) {
				Iterator iterator = datanode.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry ent = (Entry) iterator.next();
					String key = ent.getKey().toString();
					Number value = (Number) ent.getValue();
					series.getData().add(new Data<String, Number>(key, value));
				}
			}
//			Iterator<Map> datalist = data.iterator();
//			while(datalist.hasNext())
//			{
//				Map datanode = datalist.next();
//				int size = datanode.size();
//				String asText = datanode.
//				JsonNode jsonNode = datanode.get(asText);
//				double asDouble = jsonNode.asDouble();
//				series.getData().add(new Data<String, Number>(asText, asDouble));
//			}
		}

//		ObservableList<Series<String, Number>> data = FXCollections.observableArrayList();
//		data.add(series);
		return series;
	}

	static class DataNode {
		String data;
		int value;
	}
}
