package manual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The standard manual page as a data storage object.
 * @author kleinesfilmröllchen
 * @version 0.0.0008
 * @since 0.0.0008
 */
class ManualPage implements PageInterface {
	
	private String name;
	private String text;
	private ArrayList<PageInterface> subpages = new ArrayList<PageInterface>();
	private ArrayList<String> keys;
	
	private PageInterface headpage;
	
	public ManualPage(String name, String text, String[] keys) {
		this.name = name;
		this.text = text;
		this.keys = new ArrayList<String>(Arrays.asList(keys));
	}
	
	public ManualPage() {
		this("", "", null);
	}
	
	/**
	 * Getter for the head page of this page.
	 */
	public PageInterface getHeadPage() {
		return this.headpage;
	}
	
	/**
	 * Sets the head page of this page.
	 */
	public void setHeadPage(PageInterface headpage) {
		this.headpage = headpage;
	}

	@Override
	public String getText() {
		String subPageList = "";
		
		if (this.subpages.size() != 0) {
			for (PageInterface page : this.subpages) {
				subPageList += "\n - " + page.getName();
			}
		}
		//nice simple format
		return "----- " + this.name + " -----\n" + this.text + "\n-- Unterseiten --" + subPageList;
	}

	@Override
	public void addPage(PageInterface newPage) {
		this.subpages.add(newPage);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageInterface> getAllSubPages() {
		return (ArrayList<PageInterface>) this.subpages.clone();
	}

	@Override
	public PageInterface subPage(int index) throws IndexOutOfBoundsException {
		return this.subpages.get(index);
	}

	@Override
	public PageInterface subPage(String nameMatcher) {
		return subpages.stream().filter(page -> page.getName().equals(nameMatcher)).findFirst().orElse(null);
	}

	@Override
	public int pageAmount() {
		return subpages.size();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public List<String> getKeys() {
		return this.keys.stream().map(key -> key.toLowerCase()).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public String toString() {
		return "ManualPage@" + this.name;
	}
	
}
