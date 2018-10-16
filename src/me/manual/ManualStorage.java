package me.manual;

import me.rpg.core.GameConst;

/**
 * Class that serves as a storage of all manual pages except the main page.
 * All methods are static and represent the biggest part of the manual.
 * @author kleinesfilmr�llchen
 * @version 0.0.0008
 * @since 0.0.0008
 *
 */
final class ManualStorage {
	
	public static final String nl = System.lineSeparator();
	
	public static ManualPage getBattlePages() {
		ManualPage battlePage = new ManualPage("Kampf", 
				"Das Kampfsystem nimmt eine zentrale Rolle im JRPG ein. Du kannst gegen verschiedene Gegner k�mpfen." + nl
				+ "Um einen Kampf zu starten, musst du zun�chst \"a\" in der Aktionsauswahl eingeben." + nl
				+ "Danach wird dir angezeigt, ob es Gegner in diesem Raum gibt und wieviele." + nl
				+ "Du musst dich im gleichen Raum wie ein Gegner befinden, um ihn angreifen zu k�nnen." + nl
				+ "Es wird dir dann angezeigt, ob du gegen einen Gegner k�mpfen m�chtest." + nl
				+ "Hier kannst du \"j\" oder \"n\" eingeben. " + nl
				+ "Wenn du \"n\" eingibst, wirst du zum n�chsten Gegner in diesem Raum weitergeleitet, wo die gleiche Anfrage kommt." + nl
				+ "W�hlst du \"j\" aus, dann beginnt der Kampf." + nl + nl
				+ "Das Kampfsystem ist rundenbasiert. Im Kampf hast du in jeder Runde die M�glichkeit, deine Aktion per Eingabe auszuw�hlen." + nl
				+ "Nach deiner Auswahl w�hlt der Gegner seine Aktion aus, und die Kampfrunde wird ausgef�hrt." + nl
				+ "Hier soll jede Aktion nur kurz beschrieben werden; genauere Beschreibungen finden sich in den Unterseiten." + nl
				+ "a - Du f�hrst einen normalen Angriff aus." + nl
				+ "d - Du greifst nicht an und verteidigst aktiv." + nl
				+ "s - Du zeigst die Stats von dir und vom Gegner an. Diese Aktion ist eine Informationsaktion." + nl
				+ "f - Du unternimmst einen Fluchtversuch." + nl
				+ "m - Du setzt eine magische F�higkeit ein (in Entwicklung, bald verf�gbar)." + nl
				+ "i - Du setzt ein Item ein (in Entwicklung, bald verf�gbar)." + nl, 
				new String[] {"Kampfsystem", "Battle", "k�mpfen", "bek�mpfen", "Gegner", "Waffen"});
		
		ManualPage attackSubPage = new ManualPage("Angriff-Verteidigung", 
				"Angriff und Verteidigung sind die beiden zentralen Aspekte des Kampfes." + nl
				+ "In fast jeder Runde greifen sich die Kampfpartner gegenseitig an." + nl
				+ "Ein Angriff, der von einem Angreifer aus gegen einen Verteidiger erfolgt (z.B. Spieler gegen Dummy), wird folgenderma�en berechnet:" + nl
				+ "        S = A - (D * Dm)" + nl
				+ "Dabei ist S der erlittene Schaden des Verteidigers, A der Angriff des Angreifers, D die Verteidigung des Verteidigers" + nl
				+ "und Dm der Verteidigungsfaktor des Verteidigers." + nl
				+ "Die ersten beiden Parameter sind Kampfkonstanten, die sich wie folgt errechnen:" + nl
				+ "        A = As + Ae + Ak  und  D = Ds + De + Dk" + nl
				+ "As ist der Angriff - Statwert, Ae der Angriffszusatzwert durch Ausr�stung (z.B. Waffen) und Ak der Kampfmodifikator." + nl
				+ "Dieser kann sich st�ndig �ndern und stellt kampftempor�re Verbesserungen/Verschlechterungen des Werts dar," + nl
				+ "die z.B. durch magische F�higkeiten erzielt werden. F�r die Verteidigungswerte gilt genau das gleiche, also" + nl
				+ "Ds = Statwert, De = Ausr�stungswert, Dk = Kampfmodifikator." + nl + nl
				+ "Der Verteidigungsfaktor ist ein Spezialwert und h�ngt von der Aktion des Verteidigers ab:" + nl
				+ "- Bei aktivem Angriff des Verteidigers: 1" + nl
				+ "- Bei aktiver Verteidigung des Verteidigers: Verteidigungsfaktor-Stat des Verteidigers (standardm��ig " + GameConst.DEFENSE_MULTIPLIER + ")" + nl
				+ "- Bei sonstiger Aktion des Verteidigers: Verteidigungsfaktor-Stat des Verteidigers / 2 (mindestens 1)" + nl + nl
				+ "In einer Runde greift fast immer jeder jeden an, also ist einmal der Spieler der Angreifer und einmal der Spieler der Verteidiger." + nl
				+ "Die allgemeine Kampfformel muss immer ein Ergebnis gr��er als 0 liefern," + nl
				+ "ansonsten erh�lt der Verteidiger keinen Schaden und der Angriff wird als nutzlos angezeigt.", 
				new String[] {"Angriff", "Verteidigung", "Angriffberechnung", "Angriffsberechnung", "Verteidigungsberechnung", "Verteidigungberechnung", "Verteidigungsfaktor", "attack", "defence", "defense"});
		attackSubPage.setHeadPage(battlePage);
		battlePage.addPage(attackSubPage);
		
		ManualPage flightSubPage = new ManualPage("Flucht", 
				"Die Flucht ist eine von drei M�glichkeiten, dem Kampf zu entkommen." + nl
				+ "In einem Fluchtversuch ist deine Mobilit�t und die des Gegners entscheidend." + nl
				+ "Die Berechnung zweier Werte a1 und a2 erfolgt zun�chst folgenderma�en:" + nl
				+ "        a1 = r1 * (Mg / Ms)   und   a2 = r2" + nl
				+ "Dabei sind r1 und r2 Zufallswerte zwischen 1 und 0, Mg ist der Mobilit�ts-Stat des Gegners und Ms der Mobilit�ts-Stat des Spielers." + nl
				+ "Nach dieser Rechnung wird gepr�ft, ob" + nl
				+ "        a1 <= a2 * Pf" + nl
				+ "Dabei ist Pf = " + GameConst.FLIGHT_P + " die Spielkonstante der Fluchtwahrscheinlichkeit." + nl
				+ "Falls die obige Aussage wahr ist, ist die Flucht erfolgreich, andernfalls nicht." + nl
				+ "Zur Erkl�rung seien die folgenden Aussagen getroffen:" + nl
				+ "- Die Flucht wird wahrscheinlicher, je gr��er deine Mobilit�t und je kleiner die Mobilit�t des Gegners ist." + nl
				+ "- Die Flucht ist niemals sicher; es gibt bei noch so kleinem a1 immer die M�glichkeit, dass die Flucht fehlschl�gt." + nl
				+ "- Die Spielkonstante der Fluchtwahrscheinlichkeit hat eine gro�e Bedeutung; ihre Gr��e bestimmt �ber die allgemeine Fluchtwahrschienlichkeit.", 
				new String[] {"fliehen", "flee", "flight", "abhauen", "escape"});
		flightSubPage.setHeadPage(battlePage);
		battlePage.addPage(flightSubPage);
		
		ManualPage magicSubPage = new ManualPage("Magie-Zeichensystem", 
				"Das zeichenbasierte Magiesystem erlaubt es dir, eine Vielzahl von magischen Beschw�rungen auszuf�hren." + nl
				+ "Es gibt vier verschiedene Zeichentypen: Elementzeichen (1), Effektzeichen (2), Modifikationszeichen (3) und Zusatzzeichen (4)." + nl
				+ "1. Es gibt sechs m�gliche Elementzeichen: Feuer, Wasser, Erde, Luft, Licht, Dunkelheit." + nl
				+ "   Die Elementzeichen bestimmen eine grundlegende Eigenschaft der Aktion, die ausgef�hrt wird:" + nl
				+ "   Feuer      - Verwandelt einen einmaligen Effekt dieser Beschw�rung in einen dauerhaften und verst�rkt alle bereits bestehenden dauerhaften Effekte" + nl
				+ "   Wasser     - Sch�digt weitere Gegner, auf die nicht gezielt wurde." + nl
				+ "   Erde       - Verst�rkt diese Beschw�rung in allen Aspekten." + nl
				+ "   Luft       - Sehr geringe Verfehlwahrscheinlichkeit (falls eine solche existiert)." + nl
				+ "   Licht      - Verst�rkt eigene positive Effekte." + nl
				+ "   Dunkelheit - Verst�rkt gegnerische negative Effekte." + nl
				+ "2. Die Effektzeichen bestimmen die ausgef�hrte Aktion und sind somit ma�geblich." + nl
				+ "   Kugel       - Starker, normaler Angriff." + nl
				+ "   Strahl      - Schw�cherer Angriff, der jedoch leichter trifft." + nl
				+ "   Schlag      - Greift alle Gegner an." + nl
				+ "   Aura        - Regeneriert dich auf 100% deines Lebens." + nl
				+ "   Energiefeld - Verst�rkt deinen Angriff um 100%." + nl
				+ "   Schild      - Verst�rkt deine Verteidigung um 100%." + nl
				+ "   Ziel        - Verbessert deine Genauigkeit." + nl
				+ "3. Die Modifikationszeichen verst�rken bestimmte Aspekte deiner Beschw�rung: Angriff, Verteidigung," + nl
				+ "   positiver Effekt, negativer Effekt, Genauigkeit, Mobilit�t." + nl
				+ "4. Zusatzzeichen erlauben es dir, weitere Zeichen eines bestimmten Typs hinzuzuf�gen." + nl
				+ "   Es gibt Modifikations-Zusatzzeichen und Effekt-Zusatzzeichen." + nl + nl
				+ "Alle Zeichen k�nnen einmal hinzugef�gt werden; mit Ausnahme der Zusatzzeichen, welche beliebig oft eingesetzt werden k�nnen.", 
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
				new String[]{"command", "commands", "cmd", "Auftrag", "Auftr�ge", "Eingabe", "Eingaben", "input", "inputs"});
		return basicPage;
	}

	public static ManualPage getMovementPages() {
		ManualPage movementPage = new ManualPage("Bewegen",
				"Bewegung ist sehr relevant im JRPG. In diesem Zusammenhang sollen auch die R�ume besprochen werden." + nl
				+ "Um dich zu bewegen, gib \"m\" als Befehl ein. Es wird eine Auswahl kommen, die dich nach der Richtung fragt." + nl
				+ "Du kannst \"n\" f�r Norden, \"s\" f�r S�den, \"o\" f�r Osten und \"w\" f�r Westen eingeben." + nl
				+ "Die Eingabe bestimmt, wie deine Raumnummer ver�ndert werden soll.",
				new String[] {"Move", "Laufen", "Raum", "R�ume", "rooms"});
		return movementPage;
	}
}
