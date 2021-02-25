package produse;

/**
 * Method of painting enum
 * @author Saceleanu Andrei
 *
 */
public enum Culori {
	ULEI,TEMPERA,ACRILIC;
	
	/**
	 * Gives an enum value based on a string description
	 * @param type string description of method of painting
	 * @return a corresponding enum value
	 */
	public static Culori getCulori(String type) {
		switch(type) {
			case "ulei":return Culori.ULEI;
			case "acrilic":return Culori.ACRILIC;
			case "tempera":return Culori.TEMPERA;
			default:throw new IllegalStateException();
		}
	}
}
