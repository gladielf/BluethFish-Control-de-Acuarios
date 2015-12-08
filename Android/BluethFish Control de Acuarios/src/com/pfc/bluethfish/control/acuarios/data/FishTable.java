package com.pfc.bluethfish.control.acuarios.data;


/**
 * @author Fermín Conejo
 *
 */

public class FishTable implements FishColumns {
	
	public static final String FreshwaterFishTable = "Freshwater_Fish";

	public static final String[] colsOrder ={ FishColumns._ID, 
		FishColumns.ColOrder, 
		FishColumns.ColImage};
	
	public static final String[] colsFamily ={ FishColumns._ID, 
		FishColumns.ColFamily, 
		FishColumns.ColImage};
	
	public static final String[] colsName = {FishColumns._ID, 
		FishColumns.ColScientificName, 
		FishColumns.ColCommonName };
	
	public static final String[] colsCustomNameList ={FishColumns._ID,
		FishColumns.ColScientificName,
		FishColumns.ColCommonName,
		FishColumns.ColImage };
	
	public static final String[] colsFish ={ FishColumns._ID, 
		FishColumns.ColScientificName, 
		FishColumns.ColCommonName, 
		FishColumns.ColDescription, 
		FishColumns.ColSize, 
		FishColumns.ColHabitat, 
		FishColumns.ColMaintenance, 
		FishColumns.ColFood, 
		FishColumns.ColAquariumSize, 
		FishColumns.ColGenderDifference, 
		FishColumns.ColReproduction, 
		FishColumns.ColAssociation, 
		FishColumns.ColWaterCondition, 
		FishColumns.ColGHmin, 
		FishColumns.ColGHmax, 
		FishColumns.ColPHmin, 
		FishColumns.ColPHmax, 
		FishColumns.ColTmin, 
		FishColumns.ColTmax, 
		FishColumns.ColComplexity, 
		FishColumns.ColImage };

}
