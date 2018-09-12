package nl.knokko.render;

import java.util.ArrayList;
import java.util.HashMap;

import nl.knokko.entities.Entity;
import nl.knokko.entities.util.SubModel;
import nl.knokko.render.model.RawModel;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.render.task.RenderTask;
import nl.knokko.render.task.RenderTaskArmor;
import nl.knokko.render.task.RenderTaskEquipment;
import nl.knokko.render.task.RenderTaskSubModel;
import nl.knokko.render.task.RenderTaskWeapon;
import nl.knokko.textures.ModelTexture;

public class MasterRenderer {
	
	private HashMap<RawModel, HashMap<ModelTexture, ArrayList<RenderTask>>> entities = new HashMap<RawModel, HashMap<ModelTexture, ArrayList<RenderTask>>>();
	private HashMap<RawModel, HashMap<ModelTexture, ArrayList<RenderTask>>> equipment = new HashMap<RawModel, HashMap<ModelTexture, ArrayList<RenderTask>>>();
	
	private HashMap<Entity, HashMap<SubModel, RenderTaskWeapon>> weapons = new HashMap<Entity, HashMap<SubModel, RenderTaskWeapon>>();
	private HashMap<Entity, HashMap<SubModel, RenderTaskArmor>> armor = new HashMap<Entity, HashMap<SubModel, RenderTaskArmor>>();
	
	public void render(Renderer renderer){
		renderer.render(entities);
		renderer.render(equipment);
	}
	
	public void processEntity(Entity entity){
		ArrayList<RenderTask> tasks = RenderTask.createTasks(entity);
		for(RenderTask task : tasks){
			if(task instanceof RenderTaskEquipment)
				processEquipment((RenderTaskEquipment) task);
			else
				processTask(task);
		}
	}
	
	public void changeWeapon(Entity entity, SubModel hand, TexturedModel old){
		HashMap<SubModel, RenderTaskWeapon> map = weapons.get(entity);
		if(map != null){
			RenderTaskWeapon task = map.get(hand);
			if(task != null){
				if(old != null){
					ArrayList<RenderTask> list = equipment.get(old.getModel()).get(old.getTexture());
					list.remove(task);
				}
				if(hand.getHeldItemModel() != null)
					putEquipment(task);
			}
			else
				processEquipment(new RenderTaskWeapon(getSubModelTask(hand)));
		}
		else if(hand.getHeldItemModel() != null){
			processEquipment(new RenderTaskWeapon(getSubModelTask(hand)));
		}
	}
	
	private RenderTaskSubModel getSubModelTask(SubModel subModel){
		ArrayList<RenderTask> list = entities.get(subModel.getModel().getModel()).get(subModel.getModel().getTexture());
		for(RenderTask task : list){
			if(task instanceof RenderTaskSubModel && ((RenderTaskSubModel) task).getSubModel() == subModel)
				return (RenderTaskSubModel) task;
		}
		return null;
	}
	
	private void processTask(RenderTask task){
		HashMap<ModelTexture, ArrayList<RenderTask>> textureMap = entities.get(task.getModel().getModel());
		if(textureMap == null){
			textureMap = new HashMap<ModelTexture, ArrayList<RenderTask>>();
			entities.put(task.getModel().getModel(), textureMap);
		}
		ArrayList<RenderTask> list = textureMap.get(task.getModel().getTexture());
		if(list != null)
			list.add(task);
		else {
			ArrayList<RenderTask> newList = new ArrayList<RenderTask>();
			newList.add(task);
			textureMap.put(task.getModel().getTexture(), newList);
		}
	}
	
	private void processEquipment(RenderTaskEquipment task){
		putEquipment(task);
		if(task instanceof RenderTaskArmor){
			HashMap<SubModel, RenderTaskArmor> armorMap = armor.get(task.getEntity());
			if(armorMap == null){
				armorMap = new HashMap<SubModel, RenderTaskArmor>();
				armorMap.put(task.getOwnerTask().getSubModel(), (RenderTaskArmor) task);
				armor.put(task.getEntity(), armorMap);
			}
			else
				armorMap.put(task.getOwnerTask().getSubModel(), (RenderTaskArmor) task);
		}
		if(task instanceof RenderTaskWeapon){
			HashMap<SubModel, RenderTaskWeapon> weaponMap = weapons.get(task.getEntity());
			if(weaponMap == null){
				weaponMap = new HashMap<SubModel, RenderTaskWeapon>();
				weaponMap.put(task.getOwnerTask().getSubModel(), (RenderTaskWeapon) task);
				weapons.put(task.getEntity(), weaponMap);
			}
			else
				weaponMap.put(task.getOwnerTask().getSubModel(), (RenderTaskWeapon) task);
		}
	}
	
	private void putEquipment(RenderTaskEquipment task){
		HashMap<ModelTexture, ArrayList<RenderTask>> textureMap = equipment.get(task.getModel().getModel());
		if(textureMap == null){
			textureMap = new HashMap<ModelTexture, ArrayList<RenderTask>>();
			equipment.put(task.getModel().getModel(), textureMap);
		}
		ArrayList<RenderTask> list = textureMap.get(task.getModel().getTexture());
		if(list != null)
			list.add(task);
		else {
			ArrayList<RenderTask> newList = new ArrayList<RenderTask>();
			newList.add(task);
			textureMap.put(task.getModel().getTexture(), newList);
		}
	}
	
	public void removeEntity(Entity entity){
		for(HashMap<ModelTexture, ArrayList<RenderTask>> map : entities.values()){
			for(ArrayList<RenderTask> tasks : map.values()){
				for(int i = 0; i < tasks.size(); i++){
					RenderTask task = tasks.get(i);
					if(task.getEntity() == entity){
						tasks.remove(i);
						i--;
					}
				}
			}
		}
		for(HashMap<ModelTexture, ArrayList<RenderTask>> map : equipment.values()){
			for(ArrayList<RenderTask> tasks : map.values()){
				for(int i = 0; i < tasks.size(); i++){
					RenderTask task = tasks.get(i);
					if(task.getEntity() == entity){
						tasks.remove(i);
						i--;
					}
				}
			}
		}
		HashMap<SubModel, RenderTaskWeapon> weaponMap = weapons.get(entity);
		if(weaponMap != null){
			weaponMap.clear();
			weapons.remove(entity);
		}
		HashMap<SubModel, RenderTaskArmor> armorMap = armor.get(entity);
		if(armorMap != null){
			armorMap.clear();
			armor.remove(entity);
		}
	}
	
	public void clearEntities(){
		entities.clear();
		equipment.clear();
		weapons.clear();
		armor.clear();
	}
	
	public void cleanUp(){}
}
