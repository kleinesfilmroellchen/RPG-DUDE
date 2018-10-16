package me.rpg.helpers;

import me.rpg.core.GameConst;

/**
 * Outputs formatted help messages.
 * 
 * @author DJH
 * @author kleinesfilmröllchen
 * @since 0.0.0001
 * @version 0.0.0008
 */
public final class Help {
	
	/**
	 * Formatted normal help.
	 * @return a formatted string.
	 */
	public static final String getHelp() {
		
		return "Normale Befehle:\n"
				+ "(E)xit    -  Das Spiel verlassen\n"
				+ "(M)ove    -  Sich bewegen\n"
				+ "(S)tats   -  Die Stats ausgeben\n"
				+ "(D)ebug   -  In den Debug Mode gehen\n"
				+ "(A)ngriff -  Einen der Gegner im Raum angreifen\n"
				+ "(I)nfo    -  Informationen über das Spiel\n"
				+ "(U)msehen -  im momentanen Raum\n"
				+ "(L)ogging -  aktivieren oder deaktivieren\n"
				+ "Für ein detailliertes Spielhandbuch rufe das Spiel mit dem Kommandozeilenargument \"manual\" auf.";
	}
	
	/**
	 * Formatted help for battles.
	 * @return a formatted string.
	 */
	public static final String getBattleHelp() {
		
		return "Kampfbefehle:\n"
				+ "(A)ttack       - Den Gegner angreifen\n"
				+ "(D)efence      - Aktive Verteidigung verhindert große Mengen an Schaden\n"
				+ "(S)tats        - des Gegners und von dir selbst\n"
				+ "(F)liehen      - aus dem Kampf\n"
				+ "(M)agic        - Magische Fähigkeit einsetzen (kommt bald!)\n"
				+ "(I)tem         - Item einsetzen (kommt bald!)";
	}
	
	/**
	 * Formatted debug help.
	 * @return a formatted string.
	 */
	public static final String getDebugHelp() {
		
		return "Debug Befehle:\n"
				+ "(X)P           -  XP hinzufügen\n"
				+ "(L)evel        -  Level setzen\n"
				+ "(O)ne up       -  Level erhöhen mit Nachricht\n"
				+ "(Z)Zusatzleben -  zusätzliche Lebenspunkte hinzufügen\n";
	}
	
	public static final String gameInfo() {
		return "JRPG - The Java Text-based Role Play Game.\n"
				+ "Ein Programmierprojekt und Spiel von DJH und klfr.\n"
				+ "Spielversion " + GameConst.VERSION + ".\n"
				+ "Für weitere Informationen siehe \"LIESMICH\", \n"
				+ "und für versionsbezogene Informationen siehe \"CHANGELOG\".\n"
				+ "Es ist möglich das Handbuch zu öffnen, indem beim Spielstart das Kommandozeilenargument \"manual\" übergeben wird.\n\n"
				+ "Das Werk und seine Teile sind urheberrechtlich geschützt.\n"
				+ "Vervielfältigung ohne Angabe der Urheber, Modifikation, Aufführung,\n"
				+ "Decompilation mithilfe jeglicher Tools sowie Missbrauch jeglicher Art\n"
				+ "sind ohne Genehmigung der Urheber verboten und werden strafrechtlich verfolgt.\n"
				+ "© 2017-2018.";
	}
}
