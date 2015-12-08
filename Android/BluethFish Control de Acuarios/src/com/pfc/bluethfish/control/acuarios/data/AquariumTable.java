package com.pfc.bluethfish.control.acuarios.data;

/**
 * @author Fermín Conejo
 *
 */

public class AquariumTable implements AquariumColumns {
	
	public static final String AquariumTable = "Aquariums";
	
	public static final String[] colsAquariumsName = { AquariumColumns._ID,
		AquariumColumns.ColAquariumName,
		AquariumColumns.ColAquariumImage };
	
	public static final String[] colsAquariumDeleteName = { AquariumColumns._ID,
		AquariumColumns.ColAquariumName,};
	
	public static final String[] colsAquariumUpdate ={ AquariumColumns._ID,
		AquariumColumns.ColAquariumName,
		AquariumColumns.ColAquariumHeight,
		AquariumColumns.ColAquariumLength,
		AquariumColumns.ColAquariumWidth,
		AquariumColumns.ColAquariumLightOn,
		AquariumColumns.ColAquariumLightOff,
		AquariumColumns.ColAquariumImage};
	
	public static final String[] colsAquariumData = { AquariumColumns._ID,
		AquariumColumns.ColAquariumName,
		AquariumColumns.ColAquariumHeight,
		AquariumColumns.ColAquariumLength,
		AquariumColumns.ColAquariumWidth,
		AquariumColumns.ColAquariumLiters,
		AquariumColumns.ColAquariumTmin,
		AquariumColumns.ColAquariumTmax,
		AquariumColumns.ColAquariumPhMin,
		AquariumColumns.ColAquariumPhMax,
		AquariumColumns.ColAquariumLightOn,
		AquariumColumns.ColAquariumLightOff,
		AquariumColumns.ColAquariumImage };
	
	public static final String[] colsConfig = { AquariumColumns._ID,
		AquariumColumns.ColAquariumTmin,
		AquariumColumns.ColAquariumTmax,
		AquariumColumns.ColAquariumPhMin,
		AquariumColumns.ColAquariumPhMax,
		AquariumColumns.ColAquariumLightOn,
		AquariumColumns.ColAquariumLightOff };
	
}