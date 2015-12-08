package com.pfc.bluethfish.control.acuarios.data;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/**
 * @author Fermín Conejo
 *
 */

public class DatabaseAdapter {
	
	private final Context context;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	
	public DatabaseAdapter(Context context) {
		this.context = context;
	}
	
	public DatabaseAdapter(){
		super();
		this.context=null;
	}
	
	public DatabaseAdapter open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getDataBase();
		return this;
	}
 
	public void close() {
		if(dbHelper != null){
		   dbHelper.close();
		}
		if(db != null){
			db.close();
		}
	}

	/**CURSORES BIBLIOTECA DE PECES**/
	/**
	 * Cursor que devuelve una lista con los diferentes "Orden" de peces de agua dulce
	 * de la base de datos 
	 * 
	 */
	public Cursor getCursorFreshwaterFishOrder() {
		return db.query(FishTable.FreshwaterFishTable, FishTable.colsOrder, 
				null, null, FishColumns.ColOrder, null, null, null);
	}
	
	/**
	 * Cursor que recibe un orden y devuelve todas las familias pertenecienste a ese orden
	 * 
	 * @param order
	 * @return
	 */
	public Cursor getCursorFreshwaterFishFamily(String order) {
		return db.query(FishTable.FreshwaterFishTable, FishTable.colsFamily, 
				FishColumns.ColOrder + " = " + "'" + order + "'" , null, FishColumns.ColFamily, null, null, null);
	}
	
	/**
	 * Cursor que recibe una familia y devuelve todos los nombre de peces pertenecientes a esa familia
	 * 
	 * @param family
	 * @return
	 */
	public Cursor getCursorFreshwaterFishName(String family) {
		return db.query(FishTable.FreshwaterFishTable, FishTable.colsName, 
				FishColumns.ColFamily + " = " + "'" + family + "'" , null, null, null, FishColumns.ColScientificName, null);
	}
	
	/**
	 * Cursor que recive un id y devuelve toda la informacion del pece con ese id
	 * 
	 * @param id
	 * @return
	 */
	public Cursor getCursorFreshwaterFishData(String id) {
		return db.query(FishTable.FreshwaterFishTable, FishTable.colsFish, 
				FishColumns._ID +" = "+id, null, null, null, null, null);
	}
	
	/**
	 * Cursor que devuelve una lista de todos los peces
	 * 
	 * 
	 * @return
	 */
	public Cursor getCursorFreshwaterFishCustomList() {
		return db.query(FishTable.FreshwaterFishTable, FishTable.colsCustomNameList, 
				null, null, null, null, FishColumns.ColScientificName, null);
	}
	
	/**
	 * Cursor que recibe el nombre cientifico de un pez y devuelve su id
	 * @param scName
	 * @return
	 */
	public Cursor getCursorFishId(String scName) {
		return db.query(FishTable.FreshwaterFishTable, FishTable.colsName,
				FishColumns.ColScientificName + " = " +"'"+ scName +"'", null, null, null, null);
	}
		
	
	/** CURSORES BIBLIOTECA DE PLANTAS **/
	
	/**
	 * Cursor que lista todos los orden de plantas
	 * 
	 * @return
	 */
	public Cursor getCursorFreshwaterPlantOrder() {
		return db.query(PlantTable.FreshwaterPlantTable, PlantTable.colsPlantOrder, 
				null, null, PlantColumns.ColPlantOrder, null, null, null);
	}

	/**
	 * Cursor que recibe un orden y devuelve todas las familias pertenecienste a ese orden
	 * 
	 * @param order
	 * @return
	 */
	public Cursor getCursorFreshwaterPlantFamily(String order) {
		return db.query(PlantTable.FreshwaterPlantTable, PlantTable.colsPlantFamily, 
				PlantColumns.ColPlantOrder + " = " + "'" + order + "'" , null, PlantColumns.ColPlantFamily, null, null, null);
	}
	
	/**
	 * Cursor que recibe una familia y devuelve todos los nombre de plantas pertenecientes a esa familia
	 * 
	 * @param family
	 * @return
	 */
	public Cursor getCursorFreshwaterPlantName(String family) {
		return db.query(PlantTable.FreshwaterPlantTable, PlantTable.colsPlantName, 
				PlantColumns.ColPlantFamily+ " = " + "'" + family + "'" , null, null, null, PlantColumns.ColPlantScientificName, null);
	}
	
	
	/**
	 * Cursor que recive un id y devuelve toda la informacion de la planta con ese id
	 * 
	 * @param id
	 * @return
	 */
	public Cursor getCursorFreshwaterPlantData(String id) {
		return db.query(PlantTable.FreshwaterPlantTable, PlantTable.colsPlant, 
				PlantColumns._ID +" = "+id, null, null, null, null, null);
	}
	
	/**
	 * Cursor que devuelve una lista de todas las plantas
	 * 
	 * 
	 * @return
	 */
	public Cursor getCursorFreshwaterPlantCustomList() {
		return db.query(PlantTable.FreshwaterPlantTable, PlantTable.colsPlantCustomNameList, 
				null, null, null, null, PlantColumns.ColPlantScientificName, null);
	}
	
	/**
	 * Cursor que recibe el nombre cientifico de una planta y devuelve su id
	 * @param scName
	 * @return
	 */
	public Cursor getCursorPlantId(String scName) {
		return db.query(PlantTable.FreshwaterPlantTable, PlantTable.colsPlantName,
				PlantColumns.ColPlantScientificName + " = " +"'"+ scName +"'", null, null, null, null);
	}
	
	/** CURSORES MIS ACUARIOS**/
	
	/**
	 * Cursor que lista todos los acuarios de la base de datos
	 * @return
	 */
	public Cursor getCursorAquariumsName(){
		return db.query(AquariumTable.AquariumTable,AquariumTable.colsAquariumsName,
				null, null,null, null,null, null);
	}
	
	/**
	 * Cursor que recibe un id y el acuario con dicho id es eliminado
	 * @param id
	 * @return
	 */
	public Cursor getCursorAquariumDeleteName(String id){
		return db.query(AquariumTable.AquariumTable, AquariumTable.colsAquariumDeleteName,
				AquariumColumns._ID + " = " + id, null, null, null, null,null);
	}
	
	/**
	 * Cursor que recibe un id y el acuario de dicho id es modificado
	 * @param id
	 * @return
	 */
	public Cursor getCursorAquariumUpdate(String id){
		return db.query(AquariumTable.AquariumTable, AquariumTable.colsAquariumUpdate,
				AquariumColumns._ID + " = " + id, null, null, null, null,null);
	}
	
	/**
	 * Cursor que recibe un id y devuelve los datos del acuario con dicho id
	 * @param id
	 * @return
	 */
	public Cursor getCursorAquariumsData(String id) {
		return db.query(AquariumTable.AquariumTable, AquariumTable.colsAquariumData, 
				AquariumColumns._ID +" = "+id, null, null, null, null, null);
	}
	
	/**
	 * Cursor que recibe el id de un acuario y el id de un pez y si existe una tupla con dichos datos la devuelve
	 * @param aquarium_id
	 * @param fish_id
	 * @return
	 */
	public Cursor getCursorAquariumFishExist(int aquarium_id, int fish_id){
		return db.query(AquariumFishListTable.FishListTable, AquariumFishListTable.colsFishExists,
				AquariumFishListColumns.ColFishListAquariumId +" = "+ aquarium_id +" AND " + AquariumFishListColumns.ColFishListFishId + " = "+ fish_id,
				null, null, null, null, null);
		
	}
	
	/**
	 * Cursor que recibe el id de un acuario y el id de un pez y devuelve el número de peces que hay de dicho pez en dicho acuario
	 * @param aquarium_id
	 * @param fish_id
	 * @return
	 */
	public Cursor getCursorAquariumFishNumber(int aquarium_id, int fish_id){
		return db.query(AquariumFishListTable.FishListTable, AquariumFishListTable.colFishNumber,
				AquariumFishListColumns.ColFishListAquariumId + " = " + aquarium_id + " AND "+AquariumFishListColumns.ColFishListFishId+ " = "+ fish_id,
				null, null, null, null,null);
	}
	
	/**
	 * Cursor para listar los peces que hay en el acuario cuyo id coincide con el pasado por parametro
	 * @param id
	 * @return
	 */
	public Cursor getCursorAquariumFishList(int id){
		String queryAquariumFishList = "SELECT "+ FishColumns.ColScientificName + ", " + FishColumns.ColCommonName +
				", " +AquariumFishListTable.FishListTable+"."+ AquariumFishListColumns.ColFishListFishNumber+" FROM " + FishTable.FreshwaterFishTable +
				" JOIN " + AquariumFishListTable.FishListTable + " ON " +
				FishTable.FreshwaterFishTable+"."+FishColumns._ID+ " = " + AquariumFishListTable.FishListTable+"."+AquariumFishListColumns.ColFishListFishId+
				" JOIN " + AquariumTable.AquariumTable + " ON " +
				AquariumFishListTable.FishListTable+"."+AquariumFishListColumns.ColFishListAquariumId + " = " + AquariumTable.AquariumTable+"."+AquariumColumns._ID +
				" WHERE "+AquariumTable.AquariumTable+"."+AquariumColumns._ID +" = ? "+
				" ORDER BY " + FishColumns.ColScientificName;
		return db.rawQuery(queryAquariumFishList,new String[]{String.valueOf(id)});
	}
	
	/**
	 * Cusor para sacar el valor medio del PhMin, PhMax, Tmin, Tmax de la lista de peces del acuario cuyo id es el pasado por parametro
	 * @param id
	 * @return
	 */
	public Cursor getCursorAquariumAvgFishList(int id){
		String queryAquariumAvgFishList = "SELECT AVG("  + FishColumns.ColPHmin + "), AVG(" + FishColumns.ColPHmax + "), " +
				"AVG(" + FishColumns.ColTmin + "), AVG(" + FishColumns.ColTmax + ") "+
				" FROM " + FishTable.FreshwaterFishTable +
				" JOIN " + AquariumFishListTable.FishListTable + " ON " +
				FishTable.FreshwaterFishTable+"."+FishColumns._ID+ " = " + AquariumFishListTable.FishListTable+"."+AquariumFishListColumns.ColFishListFishId+
				" JOIN " + AquariumTable.AquariumTable + " ON " +
				AquariumFishListTable.FishListTable+"."+AquariumFishListColumns.ColFishListAquariumId + " = " + AquariumTable.AquariumTable+"."+AquariumColumns._ID +
				" WHERE "+AquariumTable.AquariumTable+"."+AquariumColumns._ID +" = ? ";
		
		return db.rawQuery(queryAquariumAvgFishList, new String[]{String.valueOf(id)});
	}
	
	/**
	 * Cursor que recibe el id de un acuario y el id de una planta y si existe una tupla con dichos datos la devuelve
	 * @param aquarium_id
	 * @param plant_id
	 * @return
	 */
	public Cursor getCursorAquariumPlantExist(int aquarium_id, int plant_id){
		return db.query(AquariumPlantListTable.PlantListTable, AquariumPlantListTable.colsPlantExists,
				AquariumPlantListColumns.ColPlantListAquariumId +" = "+ aquarium_id +" AND " + AquariumPlantListColumns.ColPlantListPlantId + " = "+ plant_id,
				null, null, null, null, null);
		
	}
	
	/**
	 * Cursor que recibe el id de un acuario y el id de una planta y devuelve el número de plantas que hay de dicha plantas en dicho acuario
	 * @param aquarium_id
	 * @param plant_id
	 * @return
	 */
	public Cursor getCursorAquariumPlantNumber(int aquarium_id, int plant_id){
		return db.query(AquariumPlantListTable.PlantListTable, AquariumPlantListTable.colPlantNumber,
				AquariumPlantListColumns.ColPlantListAquariumId + " = " + aquarium_id + " AND "+AquariumPlantListColumns.ColPlantListPlantId+ " = "+ plant_id,
				null, null, null, null,null);
	}
	
	/**
	 * Cursor para listar las plantas que hay en el acuario cuyo id coincide con el pasado por parametro
	 * @param id
	 * @return
	 */
	public Cursor getCursorAquariumPlantList(int id){
		String queryAquariumPlantList = "SELECT "+ PlantColumns.ColPlantScientificName + ", " + PlantColumns.ColPlantCommonName +
				", " + AquariumPlantListTable.PlantListTable+"."+ AquariumPlantListColumns.ColPlantListPlantNumber+" FROM " + PlantTable.FreshwaterPlantTable +
				" JOIN " + AquariumPlantListTable.PlantListTable + " ON " +
				PlantTable.FreshwaterPlantTable+"."+PlantColumns._ID+ " = " + AquariumPlantListTable.PlantListTable+"."+AquariumPlantListColumns.ColPlantListPlantId+
				" JOIN " + AquariumTable.AquariumTable + " ON " +
				AquariumPlantListTable.PlantListTable+"."+AquariumPlantListColumns.ColPlantListAquariumId + " = " + AquariumTable.AquariumTable+"."+AquariumColumns._ID +
				" WHERE "+AquariumTable.AquariumTable+"."+AquariumColumns._ID +" = ? "+
				" ORDER BY " + PlantColumns.ColPlantScientificName;
		return db.rawQuery(queryAquariumPlantList,new String[]{String.valueOf(id)});
	}
	
	/**
	 * Cusor para sacar el valor medio del PhMin, PhMax, Tmin, Tmax de la lista de plantas del acuario cuyo id es el pasado por parametro
	 * @param id
	 * @return
	 */
	public Cursor getCursorAquariumAvgPlantList(int id){
		String queryAquariumAvgPlantList = "SELECT AVG("  + PlantColumns.ColPlantPHmin + "), AVG(" + PlantColumns.ColPlantPHmax + "), " +
				"AVG(" + PlantColumns.ColPlantTmin + "), AVG(" + PlantColumns.ColPlantTmax + ") "+
				" FROM " + PlantTable.FreshwaterPlantTable +
				" JOIN " + AquariumPlantListTable.PlantListTable + " ON " +
				PlantTable.FreshwaterPlantTable+"."+PlantColumns._ID+ " = " + AquariumPlantListTable.PlantListTable+"."+AquariumPlantListColumns.ColPlantListPlantId+
				" JOIN " + AquariumTable.AquariumTable + " ON " +
				AquariumPlantListTable.PlantListTable+"."+AquariumPlantListColumns.ColPlantListAquariumId + " = " + AquariumTable.AquariumTable+"."+AquariumColumns._ID +
				" WHERE "+AquariumTable.AquariumTable+"."+AquariumColumns._ID +" = ? ";
		
		return db.rawQuery(queryAquariumAvgPlantList, new String[]{String.valueOf(id)});
	}

	/**
	 * Cursor que recibe el id de un acuario y los datos de ese acuario son utilizados para configurar los sensores asociados a él
	 * @param id
	 * @return
	 */
	public Cursor getCursorConfig(String id){
		return db.query(AquariumTable.AquariumTable, AquariumTable.colsConfig,
				AquariumColumns._ID + " = " + id, null, null, null, null,null);
	}
}
