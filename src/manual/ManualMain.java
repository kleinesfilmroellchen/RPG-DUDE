package manual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import rpg.core.RPG_MAIN;

/**
 * The main class for the manual. Responsible for everything regarding the
 * manual.
 * 
 * @author kleinesfilmröllchen
 * @version 0.0.0008
 * @since 0.0.0008
 *
 */
public class ManualMain {

	/**
	 * Class for the main page; has a predefined shape.
	 * 
	 * @author kleinesfilmröllchen
	 * @version 0.0.0008
	 * @since 0.0.0008
	 */
	static class MainPage implements PageInterface {

		public MainPage() {
			this.subpages.add(ManualStorage.getBattlePages());
			this.subpages.add(ManualStorage.getBasicPages());
			this.subpages.add(ManualStorage.getCommandPages());
			this.subpages.add(ManualStorage.getMovementPages());

			// very end: set all end pages
			this.subpages.forEach(page -> {
				((ManualPage) page).setHeadPage(this);
			});
		}

		private ArrayList<PageInterface> subpages = new ArrayList<PageInterface>();

		@Override
		public String getText() {
			return "----- Hauptseite -----" + ManualStorage.nl
					+ "Dies ist die Hauptseite des Handbuchs. Von hier hast du Zugriff auf alle Hauptthemen:" + ManualStorage.nl
					+ " - Grundlagen" + ManualStorage.nl + " - Befehle" + ManualStorage.nl + " - Bewegen" + ManualStorage.nl + " - Kampf";
		}

		@Override
		public String getName() {
			return "Hauptseite";
		}

		/**
		 * This implementation doesn't provide page adding; as all necessary
		 * pages are added automatically in the constructor.
		 */
		@Override
		public void addPage(PageInterface newPage) {
		}

		@SuppressWarnings("unchecked")
		@Override
		public ArrayList<PageInterface> getAllSubPages() {
			return (ArrayList<PageInterface>) this.subpages.clone();
		}

		@Override
		public PageInterface subPage(int index) throws ArrayIndexOutOfBoundsException {
			return this.subpages.get(index);
		}

		@Override
		public PageInterface subPage(String nameMatcher) {
			return this.subpages.stream().filter(page -> page.getName().equals(nameMatcher)).findFirst().orElse(null);
		}

		@Override
		public int pageAmount() {
			return this.subpages.size();
		}

		/**
		 * This implementation doesn't provide keywords, because the main page
		 * has no higher page.
		 */
		@Override
		public ArrayList<String> getKeys() {
			return null;
		}

		public String toString() {
			return "MainPage";
		}

	}

	/**
	 * Stores main page as the top of the page hierarchy. Links to all subpages
	 * (and their subpages and so on).
	 */
	public static MainPage mainPage;

	/**
	 * Local scanner reference to {@link RPG_MAIN#input}.
	 */
	private static Scanner input;

	/**
	 * Main function for the manual. Called from {@link RPG_MAIN}.
	 */
	public static void manualInit() {
		System.out.println("Herzlich Willkommen zum JRPG - Handbuch!" + ManualStorage.nl
				+ "Falls du hier aus Versehen gelandet bist (oder dir mein Geschwafel zu viel wird :-)), kannst du jederzeit 'exit' eingeben." + ManualStorage.nl
				+ "Hilfe zum Handbuch bekommst du, wenn du 'help' eingibst.");
		input = RPG_MAIN.input;

		mainPage = new MainPage();
		PageInterface currentPage = mainPage;

		while (true) {
			String typed = input.next();

			System.out.println();

			// exit from manual
			if (typed.equalsIgnoreCase("exit")) {
				System.out.println("Das Handbuch wird geschlossen! Bis zum nächsten Mal!");
				return;
			}

			// commands and search
			switch (typed.toLowerCase()) {
			case "help":
				printHelp();
				break;

			case "up":
				// go up in hierarchy
				// if page is a manual page (i.e. has a head page)
				if (currentPage.getClass() == ManualPage.class) {
					// goto the head page
					System.out.println("Gehe zur nächsthöheren Seite...");
					currentPage = ((ManualPage) currentPage).getHeadPage();
				} else {
					System.out.println("Diese Seite hat keine höhere Seite!");
				}
				break;

			case "home":
			case "top":
				// home and top have same function
				System.out.println("Gehe zur Startseite...");
				currentPage = mainPage;
				break;

			case "r":
			case "repeat":
			case "redo":
				// repeat page text
				System.out.println(currentPage.getText());
				break;

			case "path":
				// prints page path to the current page
				String pathstring = "";
				PageInterface iteratePage = currentPage;
				// while this page has a headpage
				while (iteratePage.getClass() == ManualPage.class) {
					// add page name at beginning of path
					pathstring = iteratePage.getName() + "/" + pathstring;
					// new page = headpage
					iteratePage = ((ManualPage) iteratePage).getHeadPage();
				}
				// add last page (main page) to pathstring
				pathstring = iteratePage.getName() + "/" + pathstring;

				System.out.println(pathstring);
				break;

			default:

				//// Searching
				boolean keywordSearch = false;

				ArrayList<PageInterface> currentSubPages = (ArrayList<PageInterface>) currentPage.getAllSubPages();
				Set<PageInterface> matchingSubPages = new HashSet<PageInterface>(currentSubPages.size());
				matchingSubPages.addAll(currentSubPages);

				matchingSubPages = matchingSubPages.stream()
						.filter(page -> page.getName().toLowerCase().contains(typed))
						.collect(Collectors.toCollection(HashSet::new));

				// only one page just by name...
				if (matchingSubPages.size() == 1) {
					// set this page as new current page!
					currentPage = matchingSubPages.iterator().next();
					// page output
					System.out.println(currentPage.getText());

					break;
				}
				// not only one: search keywords as well
				else {
					Set<PageInterface> keyMatchingSubPages = new HashSet<PageInterface>(currentSubPages.size());
					keyMatchingSubPages.addAll(currentSubPages);

					keyMatchingSubPages = keyMatchingSubPages.stream()
							.filter(page -> page.getKeys().stream().anyMatch(key -> key.toLowerCase().contains(typed)))
							.collect(Collectors.toCollection(HashSet::new));

					matchingSubPages.addAll(keyMatchingSubPages);

					keywordSearch = keyMatchingSubPages.size() > 0;
				}

				//// Results
				// found results
				if (matchingSubPages.size() > 0) {
					System.out.println("Es wurden folgende Suchergebnisse "
							+ (keywordSearch ? "(mit Schlüsselwörtern) " : "") + "gefunden:");
					for (PageInterface foundPage : matchingSubPages) {
						System.out.println(foundPage.getName());
					}

				} else if (matchingSubPages.size() == 0) {
					System.out.println("Die Suche nach \"" + typed
							+ "\" hatte keine Ergebnisse. Wenn du dir unsicher bist, rufe die Hilfe mit \"help\" auf.");
				}
				break;
			}
		}
	}

	/**
	 * Prints the manual help.
	 */
	public static void printHelp() {
		System.out.println("----- Handbuch - Hilfe -----" + ManualStorage.nl
				+ "Dies ist das Handbuch zum JRPG. Wenn du mehr Informationen zum JRPG möchtest, öffne das Hauptspiel und gebe \"i\" nach der Namensabfrage ein." + ManualStorage.nl
				+ "In diesem Handbuch navigierst du mithilfe deiner Texteingabe." + ManualStorage.nl
				+ "Das gesamte Handbuch besteht aus Handbuchseiten. Du befindest dich immer auf einer Handbuchseite." + ManualStorage.nl
				+ "Jede Handbuchseite hat einen Titel (wie Handbuch - Hilfe), einen Eintrag, eine Überseite und eventuell Unterseiten." + ManualStorage.nl
				+ "Unterseiten sind genauso Handbuchseiten, die sich meist mit einem Thema genauer befassen und ebenfalls Unterseiten haben können." + ManualStorage.nl
				+ "Wenn du eine Eingabe tätigst, wird in den allermeisten Fällen eine Suche durch alle Unterseiten ausgeführt." + ManualStorage.nl
				+ "Angezeigt werden dann die Seiten, die mit der Suche übereinstimmen. Wenn die Suche genau einem Seitennamen entspricht, wird diese Seite aufgerufen." + ManualStorage.nl
				+ "Groß-und Kleinschreibung wird nicht beachtet. Wenn du ein '_' vor deine Suche setzt, wird unter allen Seiten gesucht." + ManualStorage.nl
				+ "Es gibt einige \"Befehle\", also spezielle Zeichenketten, die du immer eingeben kannst:" + ManualStorage.nl
				+ " - \"exit\" beendet die Anwendung." + ManualStorage.nl + " - \"help\" ruft diese Hilfe auf." + ManualStorage.nl
				+ " - \"up\" ruft die nächsthöhere Seite auf." + ManualStorage.nl
				+ " - \"home\" ruft die Startseite des Handbuchs auf." + ManualStorage.nl
				+ " - \"r\" (für replay, repeat oder redo) zeigt erneut den Haupttext der momentanen Seite an." + ManualStorage.nl
				+ " - \"path\" zeigt den Pfad dieser Handbuchseite ausgehend von der Startseite an.");
	}

}
