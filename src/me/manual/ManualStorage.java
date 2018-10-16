package me.manual;

import me.rpg.core.GameConst;

/**
 * Class that serves as a storage of all manual pages except the main page.
 * All methods are static and represent the biggest part of the manual.
 * @author kleinesfilmröllchen
 * @version 0.0.0008
 * @since 0.0.0008
 *
 */
final class ManualStorage {
	
	public static final String nl = System.lineSeparator();
	
	public static ManualPage getBattlePages() {
		ManualPage battlePage = new ManualPage("Kampf", 
				"Das Kampfsystem nimmt eine zentrale Rolle im JRPG ein. Du kannst gegen verschiedene Gegner kämpfen." + nl
				+ "Um einen Kampf zu starten, musst du zunächst \"a\" in der Aktionsauswahl eingeben." + nl
				+ "Danach wird dir angezeigt, ob es Gegner in diesem Raum gibt und wieviele." + nl
				+ "Du musst dich im gleichen Raum wie ein Gegner befinden, um ihn angreifen zu können." + nl
				+ "Es wird dir dann angezeigt, ob du gegen einen Gegner kämpfen möchtest." + nl
				+ "Hier kannst du \"j\" oder \"n\" eingeben. " + nl
				+ "Wenn du \"n\" eingibst, wirst du zum nächsten Gegner in diesem Raum weitergeleitet, wo die gleiche Anfrage kommt." + nl
				+ "Wählst du \"j\" aus, dann beginnt der Kampf." + nl + nl
				+ "Das Kampfsystem ist rundenbasiert. Im Kampf hast du in jeder Runde die Möglichkeit, deine Aktion per Eingabe auszuwählen." + nl
				+ "Nach deiner Auswahl wählt der Gegner seine Aktion aus, und die Kampfrunde wird ausgeführt." + nl
				+ "Hier soll jede Aktion nur kurz beschrieben werden; genauere Beschreibungen finden sich in den Unterseiten." + nl
				+ "a - Du führst einen normalen Angriff aus." + nl
				+ "d - Du greifst nicht an und verteidigst aktiv." + nl
				+ "s - Du zeigst die Stats von dir und vom Gegner an. Diese Aktion ist eine Informationsaktion." + nl
				+ "f - Du unternimmst einen Fluchtversuch." + nl
				+ "m - Du setzt eine magische Fähigkeit ein (in Entwicklung, bald verfügbar)." + nl
				+ "i - Du setzt ein Item ein (in Entwicklung, bald verfügbar)." + nl, 
				new String[] {"Kampfsystem", "Battle", "kämpfen", "bekämpfen", "Gegner", "Waffen"});
		
		ManualPage attackSubPage = new ManualPage("Angriff-Verteidigung", 
				"Angriff und Verteidigung sind die beiden zentralen Aspekte des Kampfes." + nl
				+ "In fast jeder Runde greifen sich die Kampfpartner gegenseitig an." + nl
				+ "Ein Angriff, der von einem Angreifer aus gegen einen Verteidiger erfolgt (z.B. Spieler gegen Dummy), wird folgendermaßen berechnet:" + nl
				+ "        S = A - (D * Dm)" + nl
				+ "Dabei ist S der erlittene Schaden des Verteidigers, A der Angriff des Angreifers, D die Verteidigung des Verteidigers" + nl
				+ "und Dm der Verteidigungsfaktor des Verteidigers." + nl
				+ "Die ersten beiden Parameter sind Kampfkonstanten, die sich wie folgt errechnen:" + nl
				+ "        A = As + Ae + Ak  und  D = Ds + De + Dk" + nl
				+ "As ist der Angriff - Statwert, Ae der Angriffszusatzwert durch Ausrüstung (z.B. Waffen) und Ak der Kampfmodifikator." + nl
				+ "Dieser kann sich ständig ändern und stellt kampftemporäre Verbesserungen/Verschlechterungen des Werts dar," + nl
				+ "die z.B. durch magische Fähigkeiten erzielt werden. Für die Verteidigungswerte gilt genau das gleiche, also" + nl
				+ "Ds = Statwert, De = Ausrüstungswert, Dk = Kampfmodifikator." + nl + nl
				+ "Der Verteidigungsfaktor ist ein Spezialwert und hängt von der Aktion des Verteidigers ab:" + nl
				+ "- Bei aktivem Angriff des Verteidigers: 1" + nl
				+ "- Bei aktiver Verteidigung des Verteidigers: Verteidigungsfaktor-Stat des Verteidigers (standardmäßig " + GameConst.DEFENSE_MULTIPLIER + ")" + nl
				+ "- Bei sonstiger Aktion des Verteidigers: Verteidigungsfaktor-Stat des Verteidigers / 2 (mindestens 1)" + nl + nl
				+ "In einer Runde greift fast immer jeder jeden an, also ist einmal der Spieler der Angreifer und einmal der Spieler der Verteidiger." + nl
				+ "Die allgemeine Kampfformel muss immer ein Ergebnis größer als 0 liefern," + nl
				+ "ansonsten erhält der Verteidiger keinen Schaden und der Angriff wird als nutzlos angezeigt.", 
				new String[] {"Angriff", "Verteidigung", "Angriffberechnung", "Angriffsberechnung", "Verteidigungsberechnung", "Verteidigungberechnung", "Verteidigungsfaktor", "attack", "defence", "defense"});
		attackSubPage.setHeadPage(battlePage);
		battlePage.addPage(attackSubPage);
		
		ManualPage flightSubPage = new ManualPage("Flucht", 
				"Die Flucht ist eine von drei Möglichkeiten, dem Kampf zu entkommen." + nl
				+ "In einem Fluchtversuch ist deine Mobilität und die des Gegners entscheidend." + nl
				+ "Die Berechnung zweier Werte a1 und a2 erfolgt zunächst folgendermaßen:" + nl
				+ "        a1 = r1 * (Mg / Ms)   und   a2 = r2" + nl
				+ "Dabei sind r1 und r2 Zufallswerte zwischen 1 und 0, Mg ist der Mobilitäts-Stat des Gegners und Ms der Mobilitäts-Stat des Spielers." + nl
				+ "Nach dieser Rechnung wird geprüft, ob" + nl
				+ "        a1 <= a2 * Pf" + nl
				+ "Dabei ist Pf = " + GameConst.FLIGHT_P + " die Spielkonstante der Fluchtwahrscheinlichkeit." + nl
				+ "Falls die obige Aussage wahr ist, ist die Flucht erfolgreich, andernfalls nicht." + nl
				+ "Zur Erklärung seien die folgenden Aussagen getroffen:" + nl
				+ "- Die Flucht wird wahrscheinlicher, je größer deine Mobilität und je kleiner die Mobilität des Gegners ist." + nl
				+ "- Die Flucht ist niemals sicher; es gibt bei noch so kleinem a1 immer die Möglichkeit, dass die Flucht fehlschlägt." + nl
				+ "- Die Spielkonstante der Fluchtwahrscheinlichkeit hat eine große Bedeutung; ihre Größe bestimmt über die allgemeine Fluchtwahrschienlichkeit.", 
				new String[] {"fliehen", "flee", "flight", "abhauen", "escape"});
		flightSubPage.setHeadPage(battlePage);
		battlePage.addPage(flightSubPage);
		
		ManualPage magicSubPage = new ManualPage("Magie-Zeichensystem", 
				"Das zeichenbasierte Magiesystem erlaubt es dir, eine Vielzahl von magischen Beschwörungen auszuführen." + nl
				+ "Es gibt vier verschiedene Zeichentypen: Elementzeichen (1), Effektzeichen (2), Modifikationszeichen (3) und Zusatzzeichen (4)." + nl
				+ "1. Es gibt sechs mögliche Elementzeichen: Feuer, Wasser, Erde, Luft, Licht, Dunkelheit." + nl
				+ "   Die Elementzeichen bestimmen eine grundlegende Eigenschaft der Aktion, die ausgeführt wird:" + nl
				+ "   Feuer      - Verwandelt einen einmaligen Effekt dieser Beschwörung in einen dauerhaften und verstärkt alle bereits bestehenden dauerhaften Effekte" + nl
				+ "   Wasser     - Schädigt weitere Gegner, auf die nicht gezielt wurde." + nl
				+ "   Erde       - Verstärkt diese Beschwörung in allen Aspekten." + nl
				+ "   Luft       - Sehr geringe Verfehlwahrscheinlichkeit (falls eine solche existiert)." + nl
				+ "   Licht      - Verstärkt eigene positive Effekte." + nl
				+ "   Dunkelheit - Verstärkt gegnerische negative Effekte." + nl
				+ "2. Die Effektzeichen bestimmen die ausgeführte Aktion und sind somit maßgeblich." + nl
				+ "   Kugel       - Starker, normaler Angriff." + nl
				+ "   Strahl      - Schwächerer Angriff, der jedoch leichter trifft." + nl
				+ "   Schlag      - Greift alle Gegner an." + nl
				+ "   Aura        - Regeneriert dich auf 100% deines Lebens." + nl
				+ "   Energiefeld - Verstärkt deinen Angriff um 100%." + nl
				+ "   Schild      - Verstärkt deine Verteidigung um 100%." + nl
				+ "   Ziel        - Verbessert deine Genauigkeit." + nl
				+ "3. Die Modifikationszeichen verstärken bestimmte Aspekte deiner Beschwörung: Angriff, Verteidigung," + nl
				+ "   positiver Effekt, negativer Effekt, Genauigkeit, Mobilität." + nl
				+ "4. Zusatzzeichen erlauben es dir, weitere Zeichen eines bestimmten Typs hinzuzufügen." + nl
				+ "   Es gibt Modifikations-Zusatzzeichen und Effekt-Zusatzzeichen." + nl + nl
				+ "Alle Zeichen können einmal hinzugefügt werden; mit Ausnahme der Zusatzzeichen, welche beliebig oft eingesetzt werden können.", 
				new String[] {"Magie", "Zeichen", "Magic", "Zauber", "Zauberer", "Runen", "runes", "Spruch", "Zauberspruch"});
		magicSubPage.setHeadPage(battlePage);
		battlePage.addPage(magicSubPage);
		
		return battlePage;
	}
	
	public static ManualPage getBasicPages() {
		ManualPage basicPage = new ManualPage("Grundlagen", "Grundlagenseite ist noch im Aufbau!" + nl, new String[]{"basic", "basics"});
		return basicPage;
	}

	public static ManualPage getCommandPages() {
		ManualPage basicPage = new ManualPage("Befehle", "Befehlsseite ist noch im Aufbau!" + nl,
				new String[]{"command", "commands", "cmd", "Auftrag", "Aufträge", "Eingabe", "Eingaben", "input", "inputs"});
		return basicPage;
	}

	public static ManualPage getMovementPages() {
		ManualPage movementPage = new ManualPage("Bewegen",
				"Bewegung ist sehr relevant im JRPG. In diesem Zusammenhang sollen auch die Räume besprochen werden." + nl
				+ "Um dich zu bewegen, gib \"m\" als Befehl ein. Es wird eine Auswahl kommen, die dich nach der Richtung fragt." + nl
				+ "Du kannst \"n\" für Norden, \"s\" für Süden, \"o\" für Osten und \"w\" für Westen eingeben." + nl
				+ "Die Eingabe bestimmt, wie deine Raumnummer verändert werden soll.",
				new String[] {"Move", "Laufen", "Raum", "Räume", "rooms"});
		return movementPage;
	}
}
