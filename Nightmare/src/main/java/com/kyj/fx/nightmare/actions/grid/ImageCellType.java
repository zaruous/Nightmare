/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCellEditor;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.grid.ImageCellType.ImageControl;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 */
public class ImageCellType extends SpreadsheetCellType<ImageControl> {
	private static Logger LOGGER = LoggerFactory.getLogger(DefaultSpreadSheetView.class);

	/**
	 * Creates a cell that hold a String at the specified position, with the
	 * specified row/column span.
	 *
	 * @param row        row number
	 * @param column     column number
	 * @param rowSpan    rowSpan (1 is normal)
	 * @param columnSpan ColumnSpan (1 is normal)
	 * @param value      the value to display
	 * @return a {@link SpreadsheetCell}
	 */
	public SpreadsheetCell createCell(final int row, final int column, final int rowSpan, final int columnSpan,
			final Image value) {
		SpreadsheetCell cell = new SpreadsheetCellBase(row, column, rowSpan, columnSpan, this);
		cell.setGraphic(new ImageControl(value));
		// cell.setItem(new ImageControl(value));
		return cell;
	}

	@Override
	public SpreadsheetCellEditor createEditor(SpreadsheetView view) {
		return new ImageViewCellEditor(view);
	}

	@Override
	public String toString(ImageControl object) {
		return object.toString();
	}

	@Override
	public boolean match(Object value) {
		return true;
	}

	@Override
	public ImageControl convertValue(Object value) {
		if (value != null && value instanceof ImageControl) {
			return (ImageControl) value;
		}
		return null;
	}

	@Override
	public boolean match(Object value, Object... options) {
		return match(value);
	}

	static class ImageControl extends Control {

		ImageViewSkin imageViewSkin;

		public ImageControl() {
			imageViewSkin = new ImageViewSkin(this);
		}

		public ImageControl(Image image) {
			this();
			imageViewSkin.setImage(image);
		}

		/**
		 * @return the image
		 */
		public Image getImage() {
			return imageViewSkin.getImage();
		}

		/**
		 * @param image the image to set
		 */
		public void setImage(Image image) {
			imageViewSkin.setImage(image);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see javafx.scene.control.Control#createDefaultSkin()
		 */
		@Override
		protected Skin<?> createDefaultSkin() {

			return imageViewSkin;
		}

	}

	static class ImageViewSkin implements Skin<ImageControl> {

		private ImageControl imageControl;

		private ImageView iv;

		/**
		 * @return the image
		 */
		public Image getImage() {
			return iv.getImage();
		}

		/**
		 * @param image the image to set
		 */
		public void setImage(Image image) {
			if (image != null)
				iv.setImage(image);
		}

		public ImageViewSkin(ImageControl imageControl) {
			this.imageControl = imageControl;
			iv = new ImageView();
		}

		@Override
		public ImageControl getSkinnable() {
			return imageControl;
		}

		@Override
		public Node getNode() {
			return iv;
		}

		@Override
		public void dispose() {

		}

	}

	static class ImageViewCellEditor extends SpreadsheetCellEditor {

		private ImageControl iv;

		public ImageViewCellEditor(SpreadsheetView view) {
			super(view);
			this.iv = new ImageControl();
		}

		@Override
		public void startEdit(Object value) {

			if (value != null && value instanceof ImageControl) {
				iv.setImage((Image) value);
			}
		}

		@Override
		public Control getEditor() {
			return iv;
		}

		@Override
		public String getControlValue() {
			return iv.toString();
		}

		@Override
		public void end() {

		}

		@Override
		public void startEdit(Object item, String format, Object... options) {
			LOGGER.debug("start edit : {} {} {}", item, format, options);

		}

	}
}
