/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.filetree
 *	작성일   : 2023. 4. 1.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.filetree;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import com.kyj.fx.nightmare.Utils;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.eqtree.EtConfigurationTreeDVO;
import com.kyj.fx.nightmare.eqtree.EtConfigurationTreeItem;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

/**
 * @author (zaruous@naver.com)
 *
 */
public class DefaultFileTreeView extends TreeView<Path> {

	private ObjectProperty<Path> rootPath = new SimpleObjectProperty<Path>();
	private ObjectProperty<TreeItem<Path>> originalRoot = new SimpleObjectProperty<>();

	public DefaultFileTreeView() {
		FxUtil.installClipboardKeyEvent(this);

		this.rootPath.addListener(new ChangeListener<Path>() {

			@Override
			public void changed(ObservableValue<? extends Path> arg0, Path arg1, Path arg2) {
				DefaultFileTreeView.this.changed(arg0, arg1, arg2);
			}
		});
		this.setCellFactory(TextFieldTreeCell.forTreeView(new StringConverter<Path>() {

			@Override
			public Path fromString(String arg0) {
				return Path.of(arg0);
			}

			@Override
			public String toString(Path arg0) {
				return arg0.getFileName().toString();
			}
		}));

		this.rootProperty().addListener(new ChangeListener<TreeItem<Path>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<Path>> arg0, TreeItem<Path> arg1, TreeItem<Path> arg2) {
				rootPath.setValue(arg2.getValue());
			}
		});
		this.setShowRoot(true);

		this.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			if (ev.isConsumed())
				return;

			if (ev.getClickCount() == 2) {
				ev.consume();

				TreeItem<Path> selectedItem = this.getSelectionModel().getSelectedItem();
				if (selectedItem == null)
					return;
				Path currentPath = selectedItem.getValue();
				if (Files.isRegularFile(currentPath)) {
					return;
				}

				if (selectedItem.getChildren().size() == 0) {
					try {

						Files.walk(currentPath, 1).skip(1).sorted(Utils.PATH_NAME_COMPARE).forEach(path -> {
							File file = path.toFile();
							ImageView value = createImageIconView(file);
							DefaultFileTreeItem child = new DefaultFileTreeItem(path);
							child.setGraphic(value);
							selectedItem.getChildren().add(child);
						});
					} catch (IOException e) {
						e.printStackTrace();
					}
					selectedItem.setExpanded(true);
				}
			}
		});
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 * @param file
	 * @return
	 */
	public static ImageView createImageIconView(File file) {
		Image fxImage = null;
		if (file.exists()) {
			FileSystemView fileSystemView = FileSystemView.getFileSystemView();
			Icon icon = fileSystemView.getSystemIcon(file);

			BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
			fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
		} else {
			return new ImageView();
		}

		return new ImageView(fxImage);
	}

	public DefaultFileTreeView(Path root) {
		this();

		this.rootPath.set(root);
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	private void changed(ObservableValue<? extends Path> arg0, Path o, Path n) {
		DefaultFileTreeItem rootTree = new DefaultFileTreeItem(n);
		rootTree.setGraphic(createImageIconView(n.toFile()));
		this.setRoot(rootTree);
		originalRoot.set(rootTree);
	}

	/**
	 * 텍스트 검색어시 트리 구조를 변경시키는 용도로 사용 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @param text
	 */
	public void filter(String text) {

		/*
		 * 1. 텍스트가 공백이면 원본값 로드. 2. 텍스트가 존재하면 필터링.
		 */
		if (ValueUtil.isEmpty(text)) {
			this.setRoot(originalRoot.get());
			return;
		} else {
			// originalRoot = this.getRoot(T);
			ObservableList<TreeItem<Path>> children = originalRoot.get().getChildren();

			List<TreeItem<Path>> collect = children.stream().filter(v -> {
				if (v.getValue() == null)
					return false;

				String displayText = v.getValue().getFileName().toString();

				if (displayText.toUpperCase().contains(text.toUpperCase())) {
					return true;
				}

				return false;
			}).collect(Collectors.toList());

			TreeItem<Path> other = new TreeItem<>();
			other.getChildren().addAll(collect);

			this.setRoot(other);
		}
	}

}
