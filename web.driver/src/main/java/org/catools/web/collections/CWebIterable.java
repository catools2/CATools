package org.catools.web.collections;

import org.catools.common.collections.interfaces.CIterable;
import org.catools.common.extensions.types.interfaces.CDynamicIterableExtension;

import javax.ws.rs.NotSupportedException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A specialized iterable interface for web elements that provides indexed access to web components
 * in a collection. This interface extends {@link CIterable} with dynamic extension capabilities
 * for web-based testing scenarios.
 * 
 * <p>This interface is particularly useful for interacting with web pages that contain lists of 
 * elements such as tables, menus, or any collection of web components that can be accessed by 
 * index. It provides both traditional iterator-based access and direct indexed access to elements.
 * 
 * <p>The interface uses zero-based indexing where the first element has index 0.
 * 
 * <p><strong>Example usage:</strong>
 * <pre>{@code
 * // For a list of web elements like buttons or links
 * CWebIterable<ButtonElement> buttons = page.getButtons();
 * 
 * // Access first button (index 0)
 * ButtonElement firstButton = buttons.getRecord(0);
 * 
 * // Check if third button exists (index 2)  
 * boolean hasThirdButton = buttons.hasRecord(2);
 * 
 * // Iterate through all buttons
 * for (ButtonElement button : buttons) {
 *     button.click();
 * }
 * }</pre>
 * 
 * @param <E> the type of web elements in this iterable collection
 * 
 * @see CIterable
 * @see CDynamicIterableExtension
 */
public interface CWebIterable<E> extends CIterable<E, Iterable<E>>, CDynamicIterableExtension<E> {

  /**
   * Retrieves the web element at the specified zero-based index.
   * 
   * <p>The index is zero-based, meaning the first element has index 0, the second element 
   * has index 1, and so on. This method provides direct access to elements without 
   * needing to iterate through the collection.
   * 
   * <p><strong>Example usage:</strong>
   * <pre>{@code
   * CWebIterable<TableRow> tableRows = table.getRows();
   * 
   * // Get the first row (index 0)
   * TableRow firstRow = tableRows.getRecord(0);
   * 
   * // Get the third row (index 2)  
   * TableRow thirdRow = tableRows.getRecord(2);
   * 
   * // Process a specific row based on user input
   * int userSelectedIndex = getUserSelection();
   * if (tableRows.hasRecord(userSelectedIndex)) {
   *     TableRow selectedRow = tableRows.getRecord(userSelectedIndex);
   *     selectedRow.select();
   * }
   * }</pre>
   *
   * @param idx the zero-based index of the element to retrieve
   * @return the web element at the specified index
   * @throws IndexOutOfBoundsException if the index is out of range
   * @throws NoSuchElementException if no element exists at the specified index
   */
  E getRecord(int idx);

  /**
   * Checks whether a web element exists at the specified zero-based index.
   * 
   * <p>This method is useful for validating whether an element exists before attempting 
   * to retrieve it with {@link #getRecord(int)}. It uses zero-based indexing where 
   * the first element has index 0.
   * 
   * <p>This method should be used for bounds checking and conditional logic to avoid 
   * exceptions when accessing elements that may not exist.
   * 
   * <p><strong>Example usage:</strong>
   * <pre>{@code
   * CWebIterable<MenuItem> menuItems = menu.getItems();
   * 
   * // Check if the menu has at least 3 items before accessing the third one
   * if (menuItems.hasRecord(2)) {
   *     MenuItem thirdItem = menuItems.getRecord(2);
   *     thirdItem.click();
   * } else {
   *     System.out.println("Menu does not have a third item");
   * }
   * 
   * // Safe iteration pattern
   * for (int i = 0; menuItems.hasRecord(i); i++) {
   *     MenuItem item = menuItems.getRecord(i);
   *     if (item.isEnabled()) {
   *         item.click();
   *         break;
   *     }
   * }
   * }</pre>
   *
   * @param idx the zero-based index to check for element existence
   * @return {@code true} if an element exists at the specified index, {@code false} otherwise
   */
  boolean hasRecord(int idx);

  /**
   * Returns the underlying iterable collection of web elements.
   * 
   * <p>This method provides access to the raw iterable collection that backs this 
   * {@code CWebIterable}. It's primarily used internally by the framework but can 
   * be useful when you need to pass the collection to methods expecting a standard 
   * {@link Iterable}.
   * 
   * <p><strong>Example usage:</strong>
   * <pre>{@code
   * CWebIterable<LinkElement> links = page.getLinks();
   * 
   * // Get the underlying iterable for compatibility with standard Java collections
   * Iterable<LinkElement> iterable = links._get();
   * 
   * // Pass to a method expecting Iterable
   * processLinks(iterable);
   * 
   * // Or use with enhanced for-loop (though direct iteration is preferred)
   * for (LinkElement link : links._get()) {
   *     link.validate();
   * }
   * }</pre>
   *
   * @return this instance cast as {@link Iterable}
   */
  @SuppressWarnings("unchecked")
  @Override
  default Iterable<E> _get() {
    return this;
  }

  /**
   * Indicates whether this iterable supports waiting mechanisms for web elements.
   * 
   * <p>This method returns {@code true} to enable waiting functionality, which is 
   * essential for web automation where elements may take time to load or become 
   * available. The framework uses this to determine whether to apply wait strategies 
   * when accessing elements.
   * 
   * <p>For web elements, waiting is typically necessary because:
   * <ul>
   *   <li>Elements may load asynchronously after page load</li>
   *   <li>Dynamic content may be added via JavaScript</li>
   *   <li>Elements may need time to become visible or interactable</li>
   * </ul>
   * 
   * <p><strong>Example behavior:</strong>
   * <pre>{@code
   * CWebIterable<ProductCard> products = page.getProducts();
   * 
   * // Because withWaiter() returns true, the framework will wait for products to load
   * // before attempting to access them
   * ProductCard firstProduct = products.getRecord(0); // Waits if necessary
   * 
   * // The waiting behavior is handled internally by the framework
   * for (ProductCard product : products) { // Each iteration may wait for elements
   *     product.addToCart();
   * }
   * }</pre>
   *
   * @return {@code true} to indicate that waiting mechanisms should be used
   */
  @Override
  default boolean withWaiter() {
    return true;
  }

  /**
   * Returns an iterator over the web elements in this collection.
   * 
   * <p>This method provides a standard {@link Iterator} implementation that allows 
   * traversing through all web elements in the collection. The iterator uses the 
   * {@link #hasRecord(int)} and {@link #getRecord(int)} methods internally to 
   * provide sequential access to elements.
   * 
   * <p>The iterator supports the standard iteration pattern and will throw 
   * {@link NoSuchElementException} if {@link Iterator#next()} is called when no 
   * more elements are available. The {@link Iterator#remove()} operation is not 
   * supported and will throw {@link NotSupportedException}.
   * 
   * <p><strong>Example usage:</strong>
   * <pre>{@code
   * CWebIterable<FormField> formFields = form.getFields();
   * 
   * // Standard iterator usage
   * Iterator<FormField> iter = formFields.iterator();
   * while (iter.hasNext()) {
   *     FormField field = iter.next();
   *     if (field.isRequired() && field.isEmpty()) {
   *         field.fillWith("default value");
   *     }
   * }
   * 
   * // Enhanced for-loop (recommended approach)
   * for (FormField field : formFields) {
   *     field.validate();
   * }
   * 
   * // Stream operations
   * formFields.stream()
   *     .filter(FormField::isVisible)
   *     .forEach(FormField::clear);
   * }</pre>
   *
   * @return an iterator over the web elements in this collection
   * @throws NoSuchElementException when {@link Iterator#next()} is called but no more elements exist
   * @throws NotSupportedException when {@link Iterator#remove()} is called
   */
  @Override
  default Iterator<E> iterator() {
    return new Iterator<>() {
      int cursor = 0;

      @Override
      public boolean hasNext() {
        return hasRecord(cursor);
      }

      @Override
      public E next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }

        return getRecord(cursor++);
      }

      @Override
      public void remove() {
        throw new NotSupportedException();
      }
    };
  }
}
