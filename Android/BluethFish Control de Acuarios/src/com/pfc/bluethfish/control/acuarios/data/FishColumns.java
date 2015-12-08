package com.pfc.bluethfish.control.acuarios.data;

/**
 * @author Fermín Conejo
 *
 */

import android.provider.BaseColumns;

public interface FishColumns extends BaseColumns {
	
	public final static String ColOrder = "orden";
	public final static String ColFamily = "family";
	public final static String ColScientificName = "scientificName";
	public final static String ColCommonName = "commonName";
	public final static String ColDescription = "description";
	public final static String ColSize = "size";
	public final static String ColHabitat = "habitat";
	public final static String ColMaintenance = "maintenance";
	public final static String ColFood = "food";
	public final static String ColAquariumSize = "aquariumSize";
	public final static String ColGenderDifference = "genderDifference";
	public final static String ColReproduction = "reproduction";
	public final static String ColAssociation = "association";
	public final static String ColWaterCondition = "waterCondition";
	public final static String ColGHmin = "ghmin";
	public final static String ColGHmax = "ghmax";
	public final static String ColPHmin = "phmin";
	public final static String ColPHmax = "phmax";
	public final static String ColTmin = "tmin";
	public final static String ColTmax = "tmax";
	public final static String ColComplexity = "complexity";
	public final static String ColImage = "image";

}
