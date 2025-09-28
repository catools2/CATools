package org.catools.web.table;

/**
 * Represents a cell in a web table with its associated metadata.
 * <p>
 * This record encapsulates the essential properties of a table cell including its position,
 * column header, content value, and visibility state. It provides an immutable representation
 * of web table cell data that can be used for parsing, validation, and manipulation of
 * web-based tabular data.
 * </p>
 *
 * <h3>Usage Examples:</h3>
 *
 * <h4>Creating a basic table cell:</h4>
 * <pre>{@code
 * CWebTableCell cell = new CWebTableCell(0, "Name", "John Doe", true);
 * System.out.println("Cell value: " + cell.value()); // Output: Cell value: John Doe
 * }</pre>
 *
 * <h4>Creating a hidden cell:</h4>
 * <pre>{@code
 * CWebTableCell hiddenCell = new CWebTableCell(5, "Internal ID", "12345", false);
 * if (!hiddenCell.visible()) {
 *     System.out.println("This cell is not visible to users");
 * }
 * }</pre>
 *
 * <h4>Working with cell collections:</h4>
 * <pre>{@code
 * List<CWebTableCell> row = Arrays.asList(
 *     new CWebTableCell(0, "ID", "1001", true),
 *     new CWebTableCell(1, "Name", "Alice Smith", true),
 *     new CWebTableCell(2, "Email", "alice@example.com", true),
 *     new CWebTableCell(3, "Status", "Active", true)
 * );
 *
 * // Filter visible cells
 * List<CWebTableCell> visibleCells = row.stream()
 *     .filter(CWebTableCell::visible)
 *     .collect(Collectors.toList());
 *
 * // Find cell by header
 * Optional<CWebTableCell> emailCell = row.stream()
 *     .filter(cell -> "Email".equals(cell.header()))
 *     .findFirst();
 * }</pre>
 *
 * @param index   the zero-based column index of the cell within the table row
 * @param header  the column header name associated with this cell, may be null if no header is available
 * @param value   the textual content of the cell, may be null or empty
 * @param visible whether the cell is visible in the user interface (false for hidden columns)
 *
 * @since 1.0
 * @author CATools Team
 */
public record CWebTableCell(int index, String header, String value, boolean visible) {
}
