package util.file;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import util.config.CodeConfig;

public class ImageManager {
	private CodeConfig codeConfig = new CodeConfig();
	private File origFile;
	private Image origImage;
	private double resize_width;
	private double resize_height;
	private int maxImageSize;
	private double resizeRatio;
	
	public ImageManager(File f){
		this.origFile = f;
		System.out.println(this.origFile.getAbsolutePath());
		this.maxImageSize = codeConfig.getMaxImageSize();
		this.origImage = new ImageIcon(f.getAbsolutePath()).getImage();
		sizeInit();
	}
	
	public void sizeInit(){
		if(this.origImage.getWidth(null) < maxImageSize && this.origImage.getHeight(null) < maxImageSize){
			
		}else if(this.origImage.getWidth(null) >= this.origImage.getHeight(null)){
			// 가로가 더 길 경우
			this.resizeRatio = this.origImage.getWidth(null) / (double)this.maxImageSize;
			this.resize_width = this.maxImageSize;
			this.resize_height = this.origImage.getHeight(null) / this.resizeRatio;
		}else{
			// 세로가 더 길 경우
			this.resizeRatio = this.origImage.getHeight(null) / this.maxImageSize;
			this.resize_width = this.origImage.getWidth(null) / this.resizeRatio;
			this.resize_height = this.maxImageSize;
		}
	}
	
	public void resize(){
		try {
            BufferedImage buffer_original_image = ImageIO.read(this.origFile);
            BufferedImage buffer_thumbnail_image = new BufferedImage((int)this.resize_width, (int)this.resize_height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphic = buffer_thumbnail_image.createGraphics();
            graphic.drawImage(buffer_original_image, 0, 0, (int)this.resize_width, (int)this.resize_height, null);
            ImageIO.write(buffer_thumbnail_image, "jpg", this.origFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
