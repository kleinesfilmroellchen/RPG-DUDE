package manual;

import java.util.List;

/**
 * An interface for page communication. This interface defines how pages should act, but
 * the implementations are very free to implement methods however they want.
 * @author kleinesfilmröllchen
 * @version 0.0.0008
 * @since 0.0.0008
 *
 */
interface PageInterface {
	
	/**
	 * Returns the text the page has to offer.
	 * This needs to present the user with all necessary information, including subpages, name and actual entry (the page content).
	 */
	public String getText();
	
	/**
	 * @return The name of this page; displayed in the interface.
	 */
	public String getName();
	
	/**
	 * Returns all search keywords this page has. A keyword is used to search through available pages, when the user types in a search word.
	 * The interface doesn't ever show these keywords, however.<br><br>Keywords should be lowercase only, to avoid bugs with string comparison.
	 * @return All the search keywords this page has.
	 */
	public List<String> getKeys();
	
	/**
	 * Adds a subpage to the Page's collection of subpages. Null checking is not required; so a subpage can be expected to be null.
	 * @param newPage The new page to be added.
	 */
	public void addPage(PageInterface newPage);
	
	/**
	 * Getter for all subpages this page has.
	 * @return An array list with all subpages included.
	 */
	public List<PageInterface> getAllSubPages();
	
	/**
	 * Getter for a specific page. The index must be consistent; i.e. two calls on this method with the same argument
	 * return the exact same page. There is no need for the index to resemble the internal subpage storage.
	 * @param index The index to find the page at. The smallest allowed index must be 0, and the biggest one {@code pageAmount()-1}.
	 * @return The found page.
	 * @throws ArrayIndexOutOfBoundsException If the index is invalid for some reason or another. The most common case would be if the index is outside of the above specified range.
	 */
	public PageInterface subPage(int index) throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Getter for a specific subpage belonging to this page, with the name given as an argument.
	 * @param nameMatcher The <strong>EXACT</strong> name of the page. Case- (and other things-) sensitive.
	 * @return The page found in the supages that matches the specified name. {@code null} if none with that name exists.
	 */
	public PageInterface subPage(String nameMatcher);
	
	/**
	 * Getter for the amount of subpages.
	 * @return The amount of subpages this page has. Mustn't be negative.
	 */
	public int pageAmount();
	
	public String toString();
}
