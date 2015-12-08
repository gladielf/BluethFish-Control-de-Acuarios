package com.pfc.bluethfish.control.acuarios.data;


/**
 * @author Fermín Conejo
 *
 */

public class AquariumFishListTable implements AquariumFishListColumns{
	
	public static final String FishListTable = "Aquarium_Fish_List";
	
	public static final String[] colsFishExists ={
		AquariumFishListColumns.ColFishListAquariumId,
		AquariumFishListColumns.ColFishListFishId };
	
	public static final String[] colFishNumber ={
		AquariumFishListColumns.ColFishListFishNumber };
	
}