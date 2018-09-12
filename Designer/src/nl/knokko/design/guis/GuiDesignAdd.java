package nl.knokko.design.guis;

import static nl.knokko.design.guis.GuiDesignMenu.BUTTON_COLOR;
import static nl.knokko.design.guis.GuiDesignMenu.getTexture;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import nl.knokko.design.entity.IEntityPlacer;
import nl.knokko.design.guis.buttons.GuiLinkButton;
import nl.knokko.design.main.MapDesigner;
import nl.knokko.entities.EntityDecorative;
import nl.knokko.entities.tiles.TileSimpleHouse;
import nl.knokko.entities.tiles.TileTree;
import nl.knokko.guis.Gui;
import nl.knokko.guis.IGui;
import nl.knokko.guis.buttons.GuiButton;
import nl.knokko.guis.buttons.GuiOptionSliceButton;
import nl.knokko.guis.render.GuiRenderer;
import nl.knokko.guis.render.GuiTexture;
import nl.knokko.render.Renderer;
import nl.knokko.render.model.RawModel;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.textures.Material;
import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Maths;
import nl.knokko.util.Resources;
import nl.knokko.world.World;
import nl.knokko.world.forest.plants.FlowerSettings;
import nl.knokko.world.forest.plants.Flowers;
import nl.knokko.world.forest.plants.PlantSettings;
import nl.knokko.world.forest.plants.Plants;
import nl.knokko.world.forest.plants.TreeSettings;
import nl.knokko.world.forest.plants.Trees;
import nl.knokko.world.heights.Hill;
import nl.knokko.world.structures.SimpleHouses;

public class GuiDesignAdd extends Gui {
	
	private static GuiDesignAdd instance;
	
	public static GuiDesignAdd getInstance(){
		if(instance == null)
			instance = new GuiDesignAdd();
		return instance;
	}
	
	private final GuiTexture mainTexture;

	private GuiDesignAdd() {
		mainTexture = new GuiTexture(getTexture(), new Vector2f(-0.25f, 0.45f), new Vector2f(0.25f, 0.8f));
		addTexture(mainTexture);
		addButton(new GuiLinkButton(new Vector2f(-0.25f, 0.85f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Back", GuiDesignMenu.class));
		addButton(new GuiButton(new Vector2f(-0.25f, 0.6f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Decoration"){

			@Override
			public void click(int x, int y, int button, IGui gui) {
				MapDesigner.setCurrentGui(new Decoration());
			}
		});
		addButton(new GuiButton(new Vector2f(-0.25f, 0.35f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Obstacle"){

			@Override
			public void click(int x, int y, int button, IGui gui) {
				MapDesigner.setCurrentGui(new Obstacle());
			}
		});
		addButton(new GuiButton(new Vector2f(-0.25f, 0.1f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Building"){

			@Override
			public void click(int x, int y, int button, IGui gui) {
				MapDesigner.setCurrentGui(new Building());
			}
		});
		addButton(new GuiButton(new Vector2f(-0.25f, -0.15f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Ground"){

			@Override
			public void click(int x, int y, int button, IGui gui) {
				MapDesigner.setCurrentGui(new Ground());
			}
		});
	}
	
	@Override
	public void render(GuiRenderer renderer){
		GuiDesignMenu.getInstance().render(renderer);
		super.render(renderer);
	}
	
	@Override
	public void update(){
		super.update();
		if(!mainTexture.isHit(Mouse.getX(), Mouse.getY(), this))
			MapDesigner.setCurrentGui(GuiDesignMenu.getInstance());
	}
	
	private class Decoration extends AddGui {
		
		private Decoration(){
			ownTexture = new GuiTexture(getTexture(), new Vector2f(0.25f, 0.475f), new Vector2f(0.25f, 0.275f));
			addTexture(ownTexture);
			addButton(new GuiButton(new Vector2f(0.25f, 0.6f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Flower"){

				@Override
				public void click(int x, int y, int button, IGui gui) {
					MapDesigner.setCurrentGui(new Flower());
				}
			});
			addButton(new GuiButton(new Vector2f(0.25f, 0.35f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Plant"){

				@Override
				public void click(int x, int y, int button, IGui gui) {
					MapDesigner.setCurrentGui(new Plant());
				}
			});
		}
		
		private class Flower extends Gui {
			
			private GuiButton modelButton;
			private GuiButton textureButton;
			
			private RawModel model;
			private ModelTexture texture;
			
			private Flower(){
				addTexture(new GuiTexture(getTexture(), new Vector2f(), new Vector2f(0.9f, 0.8f)));
				addButton(new GuiButton(new Vector2f(-0.7f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Back"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(Decoration.this);
					}
				});
				modelButton = new GuiButton(new Vector2f(-0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Model"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(new Model());
					}
				};
				textureButton = new GuiButton(new Vector2f(0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Texture"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(new Texture());
					}
				};
				addButton(modelButton);
				addButton(textureButton);
				addButton(new GuiButton(new Vector2f(0.7f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Place"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						if(Flower.this.model != null && Flower.this.texture != null){
							MapDesigner.closeCurrentGui();
							MapDesigner.setEntityPlacer(new IEntityPlacer(){

								@Override
								public void click(World world, float x, float y, float z, java.util.Random random) {
									world.spawnEntity(new EntityDecorative(new TexturedModel(Flower.this.model, Flower.this.texture), new Vector3f(x, y, z), -10 + random.nextFloat() * 20, random.nextFloat() * 360, -10 + random.nextFloat() * 20, 0.9f + random.nextFloat() * 0.2f));
								}

								@Override
								public void update(World world, Random random) {}

								@Override
								public void render(World world, Renderer renderer) {}
							});
						}
					}
				});
			}
			
			private class Model extends Gui {
				
				private GuiTexture mainTexture;
				
				private Model(){
					mainTexture = new GuiTexture(getTexture(), new Vector2f(-0.25f, -0.1f), new Vector2f(0.3f, 0.9f));
					addTexture(mainTexture);
					addButton(new GuiButton(new Vector2f(-0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Close"){

						@Override
						public void click(int x, int y, int button, IGui gui) {
							MapDesigner.setCurrentGui(Flower.this);
						}
					});
					addButton(new ModelButton(new Vector2f(-0.25f, 0.4f), new Vector2f(0.2f, 0.1f), "Default", Flowers.DEFAULT_1));
				}
				
				@Override
				public void update(){
					super.update();
					if(!mainTexture.isHit(Mouse.getX(), Mouse.getY(), this))
						MapDesigner.setCurrentGui(Flower.this);
				}
				
				@Override
				public void render(GuiRenderer renderer){
					Flower.this.render(renderer);
					super.render(renderer);
				}
				
				private class ModelButton extends GuiButton {
					
					private RawModel flowerModel;
					private String text;

					public ModelButton(Vector2f position, Vector2f scale, String text, FlowerSettings settings) {
						super(position, scale, BUTTON_COLOR, Color.BLACK, text);
						flowerModel = Resources.getFlowerModel(settings);
						this.text = text;
					}

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(Flower.this);
						Flower.this.model = flowerModel;
						Flower.this.modelButton.getTextures()[0].setText(text, Color.BLACK);
					}
					
				}
			}
			
			private class Texture extends Gui {
				
				private GuiTexture mainTexture;
				
				private Texture(){
					mainTexture = new GuiTexture(getTexture(), new Vector2f(0.25f, -0.1f), new Vector2f(0.3f, 0.9f));
					addTexture(mainTexture);
					addButton(new GuiButton(new Vector2f(0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Close"){

						@Override
						public void click(int x, int y, int button, IGui gui) {
							MapDesigner.setCurrentGui(Flower.this);
						}
					});
					addButton(new TextureButton(new Vector2f(0.3f, 0.4f), new Vector2f(0.2f, 0.1f), "Yellow", Flowers.YELLOW_FLOWER));
					addButton(new TextureButton(new Vector2f(0.3f, 0.15f), new Vector2f(0.2f, 0.1f), "Red", Flowers.RED_FLOWER));
					addButton(new TextureButton(new Vector2f(0.3f, -0.1f), new Vector2f(0.2f, 0.1f), "Magenta", Flowers.MAGENTA_FLOWER));
					addButton(new TextureButton(new Vector2f(0.3f, -0.35f), new Vector2f(0.2f, 0.1f), "Blue", Flowers.BLUE_FLOWER));
					addButton(new TextureButton(new Vector2f(0.3f, -0.6f), new Vector2f(0.2f, 0.1f), "Green", Flowers.GREEN_FLOWER));
					addButton(new TextureButton(new Vector2f(0.3f, -0.85f), new Vector2f(0.2f, 0.1f), "Orange", Flowers.ORANGE_FLOWER));
				}
				
				@Override
				public void update(){
					super.update();
					if(!mainTexture.isHit(Mouse.getX(), Mouse.getY(), this))
						MapDesigner.setCurrentGui(Flower.this);
				}
				
				@Override
				public void render(GuiRenderer renderer){
					Flower.this.render(renderer);
					super.render(renderer);
				}
				
				private class TextureButton extends GuiButton {
					
					private ModelTexture flowerTexture;
					private String text;

					public TextureButton(Vector2f position, Vector2f scale, String text, ModelTexture texture) {
						super(position, scale, BUTTON_COLOR, Color.BLACK, text);
						flowerTexture = texture;
						this.text = text;
					}

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(Flower.this);
						Flower.this.texture = flowerTexture;
						Flower.this.textureButton.getTextures()[0].setText(text, Color.BLACK);
					}
					
				}
			}
		}
		
		private class Plant extends Gui {
			
			private GuiButton modelButton;
			private GuiButton textureButton;
			
			private RawModel model;
			private ModelTexture texture;
			
			private Plant(){
				addTexture(new GuiTexture(getTexture(), new Vector2f(), new Vector2f(0.9f, 0.8f)));
				addButton(new GuiButton(new Vector2f(-0.7f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Back"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(Decoration.this);
					}
				});
				modelButton = new GuiButton(new Vector2f(-0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Model"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(new Model());
					}
				};
				textureButton = new GuiButton(new Vector2f(0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Texture"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(new Texture());
					}
				};
				addButton(modelButton);
				addButton(textureButton);
				addButton(new GuiButton(new Vector2f(0.7f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Place"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						if(Plant.this.model != null && Plant.this.texture != null){
							MapDesigner.closeCurrentGui();
							MapDesigner.setEntityPlacer(new IEntityPlacer(){

								@Override
								public void click(World world, float x, float y, float z, java.util.Random random) {
									world.spawnEntity(new EntityDecorative(new TexturedModel(Plant.this.model, Plant.this.texture), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f + random.nextFloat() * 0.2f));
								}

								@Override
								public void update(World world, Random random) {}

								@Override
								public void render(World world, Renderer renderer) {}
							});
						}
					}
				});
			}
			
			private class Model extends Gui {
				
				private GuiTexture mainTexture;
				
				private Model(){
					mainTexture = new GuiTexture(getTexture(), new Vector2f(-0.25f, -0.1f), new Vector2f(0.3f, 0.9f));
					addTexture(mainTexture);
					addButton(new GuiButton(new Vector2f(-0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Close"){

						@Override
						public void click(int x, int y, int button, IGui gui) {
							MapDesigner.setCurrentGui(Plant.this);
						}
					});
					addButton(new ModelButton(new Vector2f(-0.25f, 0.4f), new Vector2f(0.2f, 0.1f), "Thin", Plants.THIN_PLANT));
					addButton(new ModelButton(new Vector2f(-0.25f, 0.15f), new Vector2f(0.2f, 0.1f), "Normal", Plants.BASIC_PLANT));
					addButton(new ModelButton(new Vector2f(-0.25f, -0.1f), new Vector2f(0.2f, 0.1f), "Thick", Plants.THICK_PLANT));
				}
				
				@Override
				public void update(){
					super.update();
					if(!mainTexture.isHit(Mouse.getX(), Mouse.getY(), this))
						MapDesigner.setCurrentGui(Plant.this);
				}
				
				@Override
				public void render(GuiRenderer renderer){
					Plant.this.render(renderer);
					super.render(renderer);
				}
				
				private class ModelButton extends GuiButton {
					
					private RawModel plantModel;
					private String text;

					public ModelButton(Vector2f position, Vector2f scale, String text, PlantSettings settings) {
						super(position, scale, BUTTON_COLOR, Color.BLACK, text);
						plantModel = Resources.getPlantModel(settings);
						this.text = text;
					}

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(Plant.this);
						Plant.this.model = plantModel;
						Plant.this.modelButton.getTextures()[0].setText(text, Color.BLACK);
					}
					
				}
			}
			
			public class Texture extends Gui {
				
				private GuiTexture mainTexture;
				private Color color = Color.GREEN;
				
				@SuppressWarnings("unused")
				public void setRed(float red){
					color = new Color((int) red, color.getGreen(), color.getBlue());
					refreshTexture();
				}
				
				@SuppressWarnings("unused")
				public float getRed(){
					return color.getRed();
				}
				
				@SuppressWarnings("unused")
				public void setGreen(float green){
					color = new Color(color.getRed(), (int) green, color.getBlue());
					refreshTexture();
				}
				
				@SuppressWarnings("unused")
				public float getGreen(){
					return color.getGreen();
				}
				
				@SuppressWarnings("unused")
				public void setBlue(float blue){
					color = new Color(color.getRed(), color.getGreen(), (int) blue);
					refreshTexture();
				}
				
				@SuppressWarnings("unused")
				public float getBlue(){
					return color.getBlue();
				}
				
				private void refreshTexture(){
					mainTexture.setBackGroundColor(color);
				}
				
				private Texture(){
					mainTexture = new GuiTexture(Resources.getFilledTexture(color, Material.ORGANIC), new Vector2f(0.25f, -0.1f), new Vector2f(0.3f, 0.9f));
					addTexture(mainTexture);
					addButton(new GuiButton(new Vector2f(0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Close"){

						@Override
						public void click(int x, int y, int button, IGui gui) {
							Plant.this.texture = Resources.getFilledTexture(color, Material.ORGANIC);
							MapDesigner.setCurrentGui(Plant.this);
						}
					});
					try {
						addButton(new GuiOptionSliceButton(new Vector2f(0.25f, 0.4f), new Vector2f(0.2f, 0.1f), Color.RED, Color.BLACK, "plant red", 0f, 255f, getClass().getMethod("setRed", float.class), getClass().getMethod("getRed"), this));
						addButton(new GuiOptionSliceButton(new Vector2f(0.25f, 0.15f), new Vector2f(0.2f, 0.1f), Color.GREEN, Color.BLACK, "plant green", 0f, 255f, getClass().getMethod("setGreen", float.class), getClass().getMethod("getGreen"), this));
						addButton(new GuiOptionSliceButton(new Vector2f(0.25f, -0.1f), new Vector2f(0.2f, 0.1f), Color.BLUE, Color.BLACK, "plant blue", 0f, 255f, getClass().getMethod("setBlue", float.class), getClass().getMethod("getBlue"), this));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				
				@Override
				public void update(){
					super.update();
					if(!mainTexture.isHit(Mouse.getX(), Mouse.getY(), this))
						MapDesigner.setCurrentGui(Plant.this);
				}
				
				@Override
				public void render(GuiRenderer renderer){
					Plant.this.render(renderer);
					super.render(renderer);
				}
			}
		}
	}
	
	private class Obstacle extends AddGui {
		
		private Obstacle(){
			ownTexture = new GuiTexture(getTexture(), new Vector2f(0.25f, 0.35f), new Vector2f(0.25f, 0.15f));
			addTexture(ownTexture);
			addButton(new GuiButton(new Vector2f(0.25f, 0.35f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Tree"){

				@Override
				public void click(int x, int y, int button, IGui gui) {
					MapDesigner.setCurrentGui(new Tree());
				}
			});
		}
		
		private class Tree extends Gui {
			
			private GuiButton modelButton;
			private GuiButton textureButton;
			
			private RawModel model;
			private ModelTexture texture;
			
			private Tree(){
				addTexture(new GuiTexture(getTexture(), new Vector2f(), new Vector2f(0.9f, 0.8f)));
				addButton(new GuiButton(new Vector2f(-0.7f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Back"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(Obstacle.this);
					}
				});
				modelButton = new GuiButton(new Vector2f(-0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Model"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(new Model());
					}
				};
				textureButton = new GuiButton(new Vector2f(0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Texture"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(new Texture());
					}
				};
				addButton(modelButton);
				addButton(textureButton);
				addButton(new GuiButton(new Vector2f(0.7f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Place"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						if(Tree.this.model != null && Tree.this.texture != null){
							MapDesigner.closeCurrentGui();
							MapDesigner.setEntityPlacer(new IEntityPlacer(){

								@Override
								public void click(World world, float x, float y, float z, java.util.Random random) {
									world.spawnEntity(new TileTree(Trees.settingsFromID(Tree.this.model.getCreationID()[1]), Tree.this.texture, x, z, random.nextFloat() * 360, 0.8f + 0.4f * random.nextFloat()));
								}

								@Override
								public void update(World world, Random random) {}

								@Override
								public void render(World world, Renderer renderer) {
									//renderer.renderSingle(new TexturedModel(Tree.this.model, Tree.this.texture), matrix);
								}
							});
						}
					}
				});
			}
			
			private class Model extends Gui {
				
				private GuiTexture mainTexture;
				
				private Model(){
					mainTexture = new GuiTexture(getTexture(), new Vector2f(-0.25f, -0.1f), new Vector2f(0.3f, 0.9f));
					addTexture(mainTexture);
					addButton(new GuiButton(new Vector2f(-0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Close"){

						@Override
						public void click(int x, int y, int button, IGui gui) {
							MapDesigner.setCurrentGui(Tree.this);
						}
					});
					addButton(new ModelButton(new Vector2f(-0.25f, 0.4f), new Vector2f(0.2f, 0.1f), "Oak", Trees.getOakModel()));
				}
				
				@Override
				public void update(){
					super.update();
					if(!mainTexture.isHit(Mouse.getX(), Mouse.getY(), this))
						MapDesigner.setCurrentGui(Tree.this);
				}
				
				@Override
				public void render(GuiRenderer renderer){
					Tree.this.render(renderer);
					super.render(renderer);
				}
				
				private class ModelButton extends GuiButton {
					
					private RawModel treeModel;
					private String text;

					public ModelButton(Vector2f position, Vector2f scale, String text, TreeSettings settings) {
						super(position, scale, BUTTON_COLOR, Color.BLACK, text);
						treeModel = Resources.getTreeModel(settings);
						this.text = text;
					}

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(Tree.this);
						Tree.this.model = treeModel;
						Tree.this.modelButton.getTextures()[0].setText(text, Color.BLACK);
					}
					
				}
			}
			
			private class Texture extends Gui {
				
				private GuiTexture mainTexture;
				
				private Texture(){
					mainTexture = new GuiTexture(getTexture(), new Vector2f(0.25f, -0.1f), new Vector2f(0.3f, 0.9f));
					addTexture(mainTexture);
					addButton(new GuiButton(new Vector2f(0.25f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Close"){

						@Override
						public void click(int x, int y, int button, IGui gui) {
							MapDesigner.setCurrentGui(Tree.this);
						}
					});
					addButton(new TextureButton(new Vector2f(0.3f, 0.4f), new Vector2f(0.2f, 0.1f), "Oak", Trees.getSpruceTexture()));
				}
				
				@Override
				public void update(){
					super.update();
					if(!mainTexture.isHit(Mouse.getX(), Mouse.getY(), this))
						MapDesigner.setCurrentGui(Tree.this);
				}
				
				@Override
				public void render(GuiRenderer renderer){
					Tree.this.render(renderer);
					super.render(renderer);
				}
				
				private class TextureButton extends GuiButton {
					
					private ModelTexture treeTexture;
					private String text;

					public TextureButton(Vector2f position, Vector2f scale, String text, ModelTexture texture) {
						super(position, scale, BUTTON_COLOR, Color.BLACK, text);
						treeTexture = texture;
						this.text = text;
					}

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(Tree.this);
						Tree.this.texture = treeTexture;
						Tree.this.textureButton.getTextures()[0].setText(text, Color.BLACK);
					}
					
				}
			}
		}
	}
	
	private class Building extends AddGui {
		
		private Building(){
			ownTexture = new GuiTexture(getTexture(), new Vector2f(0.25f, 0.1f), new Vector2f(0.25f, 0.15f));
			addTexture(ownTexture);
			addButton(new GuiButton(new Vector2f(0.25f, 0.1f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "House"){

				@Override
				public void click(int x, int y, int button, IGui gui) {
					MapDesigner.setCurrentGui(new House());
				}
				
			});
		}
		
		private class House extends Gui {
			
			private House(){
				addTexture(new GuiTexture(getTexture(), new Vector2f(), new Vector2f(0.9f, 0.8f)));
				addButton(new GuiButton(new Vector2f(-0.7f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Back"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.setCurrentGui(Building.this);
					}
				});
				addButton(new GuiButton(new Vector2f(-0.4f, 0.65f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Wooden House 1"){

					@Override
					public void click(int x, int y, int button, IGui gui) {
						MapDesigner.closeCurrentGui();
						MapDesigner.setEntityPlacer(new IEntityPlacer(){

							@Override
							public void click(World world, float x, float y, float z, Random random) {
								world.spawnEntity(new TileSimpleHouse(SimpleHouses.TEXTURE_WOOD_1, x, z));
							}

							@Override
							public void update(World world, Random random) {}

							@Override
							public void render(World world, Renderer renderer) {}
							
						});
					}
					
				});
			}
		}
	}
	
	private class Ground extends AddGui {
		
		private Ground(){
			ownTexture = new GuiTexture(getTexture(), new Vector2f(0.25f, -0.15f), new Vector2f(0.25f, 0.15f));
			addTexture(ownTexture);
			addButton(new GuiButton(new Vector2f(0.25f, -0.15f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Hill"){
				
				@Override
				public void click(int x, int y, int button, IGui gui) {
					MapDesigner.closeCurrentGui();
					MapDesigner.setEntityPlacer(new IEntityPlacer(){
						
						private TexturedModel model;
						private Matrix4f matrix = Maths.createTransformationMatrix(new Vector3f(), 0, 0, 0, 1);
						
						
						private float x;
						private float z;
						
						private float h;
						
						private float f;
						private float p;
						
						private boolean state;
						private boolean refresh = true;;

						@Override
						public void click(World world, float x, float y, float z, Random random) {
							if(state){
								new Hill(this.x, this.z, h, f, p).spawn(world);
								state = false;
							}
							else {
								this.x = x;
								this.z = z;
								this.h = 2f;
								this.f = 1.2f;
								this.p = 1.5f;
								state = true;
								refresh = true;
							}
						}

						@Override
						public void update(World world, Random random) {
							if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
								h += 0.1f;
								refresh();
							}
							if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
								h -= 0.1f;
								refresh();
							}
							if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
								p -= 0.01f;
								refresh();
							}
							if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
								p += 0.01f;
								refresh();
							}
						}

						@Override
						public void render(World world, Renderer renderer) {
							if(state){
								if(refresh){
									Hill hill = new Hill(x, z, h, f, p);
									model = new TexturedModel(Resources.createHillModel(world, hill, 0.5f), Resources.getFilledTexture(Color.WHITE, Material.METAL));
									refresh = false;
								}
								renderer.renderSingle(model, matrix);
							}
						}
						
						private void refresh(){
							float size = (float) Math.pow(h / f, 1 / p);
							if(size < 5)
								refresh = true;
							else if(new Random().nextInt(20) == 0)
								refresh = true;
						}
						
					});
				}
			});
		}
	}
	
	private class AddGui extends Gui {
		
		GuiTexture ownTexture;
		
		@Override
		public void render(GuiRenderer renderer){
			GuiDesignAdd.this.render(renderer);
			super.render(renderer);
		}
		
		@Override
		public void update(){
			super.update();
			if(!mainTexture.isHit(Mouse.getX(), Mouse.getY(), this) && !ownTexture.isHit(Mouse.getX(), Mouse.getY(), this))
				MapDesigner.setCurrentGui(GuiDesignAdd.this);
		}
		
		@Override
		public void click(int x, int y, int button){
			GuiDesignAdd.this.click(x, y, button);
			super.click(x, y, button);
		}
	}
}
