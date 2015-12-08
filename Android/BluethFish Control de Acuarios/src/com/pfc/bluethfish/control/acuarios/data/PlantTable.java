package com.pfc.bluethfish.control.acuarios.data;


/**
 * @author Fermín Conejo
 *
 */

public class PlantTable implements PlantColumns{
	
	public static final String FreshwaterPlantTable = "Freshwater_plant";

	public static final String[] colsPlantOrder ={ PlantColumns._ID, 
		PlantColumns.ColPlantOrder, 
		PlantColumns.ColPlantImage};
	
	public static final String[] colsPlantFamily={ PlantColumns._ID,
		PlantColumns.ColPlantFamily,
		PlantColumns.ColPlantImage};
	
	public static final String[] colsPlantName={ PlantColumns._ID,
		PlantColumns.ColPlantScientificName,
		PlantColumns.ColPlantCommonName };
	
	public static final String[] colsPlantCustomNameList={ PlantColumns._ID,
		PlantColumns.ColPlantScientificName,
		PlantColumns.ColPlantCommonName,
		PlantColumns.ColPlantImage };
	
	
	public static final String[] colsPlant={ PlantColumns._ID,
		PlantColumns.ColPlantScientificName,
		PlantColumns.ColPlantCommonName,
		PlantColumns.ColPlantDescription,
		PlantColumns.ColPlantHabitat,
		PlantColumns.ColPlantMaintenance,
		PlantColumns.ColPlantSubstratum,
		PlantColumns.ColPlantAquariumPosition,
		PlantColumns.ColPlantReproduction,
		PlantColumns.ColPlantLight,
		PlantColumns.ColPlantSize,
		PlantColumns.ColPlantWaterCondition,
		PlantColumns.ColPlantGHmin,
		PlantColumns.ColPlantGHmax,
		PlantColumns.ColPlantPHmin,
		PlantColumns.ColPlantPHmax,
		PlantColumns.ColPlantCO2min,
		PlantColumns.ColPlantCO2max,
		PlantColumns.ColPlantTmin,
		PlantColumns.ColPlantTmax,
		PlantColumns.ColPlantComplexity,
		PlantColumns.ColPlantImage};

}