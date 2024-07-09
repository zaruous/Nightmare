/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCellEditor;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.control.spreadsheet.StringConverterWithFormat;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * 
 */
public class LocalDateTimeCellType extends SpreadsheetCellType<LocalDateTime> {

	public LocalDateTimeCellType() {
		this(new StringConverterWithFormat<LocalDateTime>() {
			@Override
			public String toStringFormat(LocalDateTime item, String format) {
				if (("").equals(format) && item != null) { //$NON-NLS-1$
					return item.toString();
				} else if (item != null) {
					return item.format(DateTimeFormatter.ofPattern(format));
				} else {
					return ""; //$NON-NLS-1$
				}
			}

			@Override
			public String toString(LocalDateTime item) {
				return toStringFormat(item, ""); //$NON-NLS-1$
			}

			@Override
			public LocalDateTime fromString(String str) {
				try {
					return LocalDateTime.parse(str);
				} catch (Exception e) {
					return null;
				}
			}
		});
	}

	public LocalDateTimeCellType(StringConverterWithFormat<LocalDateTime> converter) {
		super(converter);
	}

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
			final LocalDateTime value) {
		SpreadsheetCellBase cell = new SpreadsheetCellBase(row, column, rowSpan, columnSpan, this);
		cell.setItem(value);
		return cell;
	}

	@Override
	public SpreadsheetCellEditor createEditor(SpreadsheetView view) {
		TextFieldEditor stringEditor = new TextFieldEditor(view, (StringConverterWithFormat<LocalDateTime>) converter);
		return stringEditor;
	}

	@Override
	public String toString(LocalDateTime object) {
		return converter.toString(object);
	}

	@Override
	public boolean match(Object value, Object... options) {
		if (value instanceof LocalDateTime)
			return true;
		else {
			try {
				LocalDateTime temp = converter.fromString(value == null ? null : value.toString());
				return temp != null;
			} catch (Exception e) {
				return false;
			}
		}
	}

	@Override
	public LocalDateTime convertValue(Object value) {
		if (value instanceof LocalDateTime)
			return (LocalDateTime) value;
		else {
			try {
				return converter.fromString(value == null ? null : value.toString());
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	public static class TextFieldEditor extends SpreadsheetCellEditor {
        /***************************************************************************
         * * Private Fields * *
         **************************************************************************/
        private final TextField tf;
        private StringConverterWithFormat<LocalDateTime> converter;
        /***************************************************************************
         * * Constructor * *
         **************************************************************************/
        /**
         * Constructor for the StringEditor.
         * @param view The SpreadsheetView
         * @param converter 
         */
        public TextFieldEditor(SpreadsheetView view, StringConverterWithFormat<LocalDateTime> converter) {
            super(view);
            tf = new TextField();
            this.converter = converter;
        }

        /***************************************************************************
         * * Public Methods * *
         **************************************************************************/
        @Override
        public void startEdit(Object value, String format, Object... options) {

        	if (value instanceof LocalDateTime) {
                tf.setText(converter.toStringFormat((LocalDateTime) value, format));
            }
        	else if (value instanceof String || value == null) {
                tf.setText((String) value);
            }
            attachEnterEscapeEventHandler();

            tf.requestFocus();
            tf.selectAll();
        }

        @Override
        public String getControlValue() {
            return tf.getText();
        }

        @Override
        public void end() {
            tf.setOnKeyPressed(null);
        }

        @Override
        public TextField getEditor() {
            return tf;
        }

        /***************************************************************************
         * * Private Methods * *
         **************************************************************************/

        private void attachEnterEscapeEventHandler() {
            tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        endEdit(true);
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        endEdit(false);
                    }
                }
            });
        }
    }

}
