package nl.knokko.util;

public class TextureArea {
	
	public static void setLocations(TextureArea... areas){
		int x = 0;
		int width = 0;
		int height = 0;
		for(TextureArea area : areas){
			area.setMinX(x);
			area.setMinY(0);//let's keep it 0 for now
			width = x + area.getWidth();
			if(area.getHeight() > height)
				height = area.getHeight();
			x += area.getWidth();
		}
		double logWidth = Math.log10(width) / Math.log10(2);
		double logHeight = Math.log10(height) / Math.log10(2);
		if(logWidth != (int) logWidth)
			logWidth = (int) logWidth + 1;
		if(logHeight != (int) logHeight)
			logHeight = (int) logHeight + 1;
		width = (int) Math.pow(2, logWidth);
		height = (int) Math.pow(2, logHeight);
		for(TextureArea area : areas){
			area.setImageWidth(width);
			area.setImageHeight(height);
		}
	}
	
	private int x;
	private int y;
	
	private int width;
	private int height;
	
	private int imageWidth;
	private int imageHeight;

	public TextureArea(float width, float height, int pixelScale){
		this.width = (int) (width * pixelScale);
		this.height = (int) (height * pixelScale);
	}
	
	@Override
	public String toString(){
		return "TextureArea:[minX = " + getMinX() + ",minY = " + getMinY() + ",maxX = " + getMaxX() + ",maxY = " + getMaxY() + "] in (" + imageWidth + "," + imageHeight + ")";
	}
	
	public void setMinX(int x){
		this.x = x;
	}
	
	public void setMinY(int y){
		this.y = y;
	}
	
	public int getImageWidth(){
		return imageWidth;
	}
	
	public int getImageHeight(){
		return imageHeight;
	}
	
	public void setImageWidth(int imageWidth){
		this.imageWidth = imageWidth;
	}
	
	public void setImageHeight(int imageHeight){
		this.imageHeight = imageHeight;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getMinX(){
		return x;
	}
	
	public int getMinY(){
		return y;
	}
	
	public int getMaxX(){
		return x + width - 1;
	}
	
	public int getMaxY(){
		return y + height - 1;
	}
	
	public float getMinU(){
		return (float) getMinX() / imageWidth;
	}
	
	public float getMinV(){
		return (float) getMinY() / imageHeight;
	}
	
	public float getMaxU(){
		return (float) getMaxX() / imageWidth;
	}
	
	public float getMaxV(){
		return (float) getMaxY() / imageHeight;
	}
}
