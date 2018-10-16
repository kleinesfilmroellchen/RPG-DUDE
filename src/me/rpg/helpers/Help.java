package me.rpg.helpers;

import me.rpg.core.GameConst;

/**
 * Outputs formatted help messages.
 * 
 * @author DJH
 * @author kleinesfilmr�llchen
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
				+ "(I)nfo    -  Informationen �ber das Spiel\n"
				+ "(U)msehen -  im momentanen Raum\n"
				+ "(L)ogging -  aktivieren oder deaktivieren\n"
				+ "F�r ein detailliertes Spielhandbuch rufe das Spiel mit dem Kommandozeilenargument \"manual\" auf.";
	}
	
	/**
	 * Formatted help for battles.
	 * @return a formatted string.
	 */
	public static final String getBattleHelp() {
		
		return "Kampfbefehle:\n"
				+ "(A)ttack       - Den Gegner angreifen\n"
				+ "(D)efence      - Aktive Verteidigung verhindert gro�e Mengen an Schaden\n"
				+ "(S)tats        - des Gegners und von dir selbst\n"
				+ "(F)liehen      - aus dem Kampf\n"
				+ "(M)agic        - Magische F�higkeit einsetzen (kommt bald!)\n"
				+ "(I)tem         - Item einsetzen (kommt bald!)";
	}
	
	/**
	 * Formatted debug help.
	 * @return a formatted string.
	 */
	public static final String getDebugHelp() {
		
		return "Debug Befehle:\n"
				+ "(X)P           -  XP hinzuf�gen\n"
				+ "(L)evel        -  Level setzen\n"
				+ "(O)ne up       -  Level erh�hen mit Nachricht\n"
				+ "(Z)Zusatzleben -  zus�tzliche Lebenspunkte hinzuf�gen\n";
	}
	
	public static final String gameInfo() {
		return "JRPG - The Java Text-based Role Play Game.\n"
				+ "Ein Programmierprojekt und Spiel von DJH und klfr.\n"
				+ "Spielversion " + GameConst.VERSION + ".\n"
				+ "F�r weitere Informationen siehe \"LIESMICH\", \n"
				+ "und f�r versionsbezogene Informationen siehe \"CHANGELOG\".\n"
				+ "Es ist m�glich das Handbuch zu �ffnen, indem beim Spielstart das Kommandozeilenargument \"manual\" �bergeben wird.\n\n"
				+ "Das Werk und seine Teile sind urheberrechtlich gesch�tzt.\n"
				+ "Vervielf�ltigung ohne Angabe der Urheber, Modifikation, Auff�hrung,\n"
				+ "Decompilation mithilfe jeglicher Tools sowie Missbrauch jeglicher Art\n"
				+ "sind ohne Genehmigung der Urheber verboten und werden strafrechtlich verfolgt.\n"
				+ "� 2017-2018.";
	}
}
