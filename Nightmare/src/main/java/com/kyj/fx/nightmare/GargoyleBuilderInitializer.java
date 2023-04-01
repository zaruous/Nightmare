/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.main.initalize
 *	작성일   : 2018. 6. 1.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare;

import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

/**
 * 
 * 디폴트 빌더 설정을 통해 <br/>
 * 어플리케이션의 기본 속성에 대한 설정을 변경 처리한다. <br/>
 * 
 * 해당 어플리케이션은 FxLoader 클래스를 통해 로드한 클래스에 한해 적용되는점을 참조해야한다. <br/>
 * 
 * @author KYJ
 *
 */
public class GargoyleBuilderInitializer implements BuilderFactory {

	public class GargoyleTableViewBuilder extends TableView<Object> implements Builder<TableView<?>> {

		@Override
		public TableView<?> build() {
			FxUtil.installClipboardKeyEvent(this);
			getSelectionModel().setCellSelectionEnabled(true);
			return this;
		}

	}
		
	public class GargoyleTreeViewBuilder extends TreeView<Object> implements Builder<TreeView<?>> {

		@Override
		public TreeView<?> build() {
			FxUtil.installClipboardKeyEvent(this);
			return this;
		}

	}
	
	public class GargoyleListViewBuilder extends ListView<Object> implements Builder<ListView<?>> {

		@Override
		public ListView<?> build() {
			FxUtil.installClipboardKeyEvent(this);
			return this;
		}

	}

//	public static Builder<TableView<?>> tableBuilder = new Builder<TableView<?>>() {
//		@Override
//		public TableView<?> build() {
//
//			TableView<?> tv = new GargoyleTableViewBuilder().build();
//			// TableView<?> tableView = new TableView<>();
//			FxUtil.installClipboardKeyEvent(tv);
//			return tv;
//		}

//	};

//	public static Builder<TreeView> treeViewBuilder = new Builder<TreeView>() {
//		@Override
//		public TreeView build() {
//			TreeView tv = new TreeView();
//			FxUtil.installClipboardKeyEvent(tv);
//			return tv;
//		}
//	};

//	public static Builder<ListView> listViewBuilder = new Builder<ListView>() {
//		@Override
//		public ListView build() {
//			ListView tv = new ListView();
//			FxUtil.installClipboardKeyEvent(tv);
//			return tv;
//		}
//	};

	@Override
	public Builder<?> getBuilder(Class<?> type) {

		if (TableView.class == type) {
			return new GargoyleTableViewBuilder();
		} else if (TreeView.class == type) {
			return new GargoyleTreeViewBuilder();
		} else if (ListView.class == type) {
			return new GargoyleListViewBuilder();
		} else {
			JavaFXBuilderFactory javaFXBuilderFactory = new JavaFXBuilderFactory();
			Builder<?> builder = javaFXBuilderFactory.getBuilder(type);
			if (builder == null)
				return null;
		}
		return null;
	}

}
