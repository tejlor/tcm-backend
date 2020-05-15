package pl.olawa.telech.tcm.logic.helper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import pl.olawa.telech.tcm.model.exception.TcmException;

@Component
public class FilePreviewGenerator {

	@Value("${tcm.repo.previewSize}")
	private int previewSize;
	
	private static final Set<String> availableTypes;

	static {
		availableTypes = new TreeSet<>();
		availableTypes.add("image/jpeg");
		availableTypes.add("image/png");
		availableTypes.add("image/gif");
		availableTypes.add("image/bmp");
	}

	public boolean isAvailable(String mimeType) {
		return availableTypes.contains(mimeType);
	}

	public byte[] createPreview(MultipartFile file) throws IOException  {
		switch (file.getContentType()) {
			case "image/jpeg":
				BufferedImage img = ImageIO.read(file.getInputStream());
				
				double scale = (double)img.getWidth() / img.getHeight();
				int thumbWidth = previewSize;
				int thumbHeight = (int) Math.round(previewSize / scale);
				if(thumbWidth > previewSize) {
					thumbHeight = previewSize;
					thumbWidth = (int) Math.round(previewSize * scale);
				}
				
				BufferedImage thumb = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
				thumb.createGraphics().drawImage(img.getScaledInstance(thumbWidth, thumbHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);
				
				var bos = new ByteArrayOutputStream();
				ImageIO.write(img, "jpg", bos);
				return bos.toByteArray();
				
			default:
				throw new TcmException("Mime type " + file.getContentType() + " is not available.");
		}
	}
}
